package io.art.generator.creator.mapper;

import com.sun.tools.javac.tree.JCTree.*;
import io.art.generator.exception.*;
import lombok.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.generator.caller.MethodCaller.*;
import static io.art.generator.constants.GeneratorConstants.ExceptionMessages.*;
import static io.art.generator.constants.GeneratorConstants.MappersConstants.ArrayMappingMethods.*;
import static io.art.generator.constants.GeneratorConstants.MappersConstants.BinaryMappingMethods.*;
import static io.art.generator.constants.GeneratorConstants.MappersConstants.*;
import static io.art.generator.constants.GeneratorConstants.MappersConstants.EntityMappingMethods.*;
import static io.art.generator.constants.GeneratorConstants.Names.*;
import static io.art.generator.constants.GeneratorConstants.TypeModels.*;
import static io.art.generator.context.GeneratorContext.*;
import static io.art.generator.creator.mapper.ToModelMapperCreator.*;
import static io.art.generator.inspector.TypeInspector.*;
import static io.art.generator.model.NewLambda.*;
import static io.art.generator.model.NewParameter.*;
import static io.art.generator.model.TypeModel.*;
import static io.art.generator.selector.FromMapperMethodSelector.*;
import static io.art.generator.service.JavacService.*;
import static io.art.generator.service.NamingService.*;
import static io.art.generator.state.GenerationState.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.lang.reflect.Field;
import java.lang.reflect.*;
import java.util.*;

@RequiredArgsConstructor(access = PRIVATE)
public class FromModelMapperCreator {
    private final String modelName;

    public static JCExpression fromModelMapper(Type type) {
        String generatedMapping = getGeneratedMapper(type);
        if (nonNull(generatedMapping)) {
            return select(select(providerClassName(), generatedMapping), FROM_MODEL_NAME);
        }
        return createFromModelMapper(type);
    }

    public static JCExpression createFromModelMapper(Type type) {
        FromModelMapperCreator creator = new FromModelMapperCreator(sequenceName(MODEL_NAME));

        if (isLibraryType(type)) {
            return creator.body(type);
        }

        if (type instanceof GenericArrayType) {
            return creator.body(type);
        }

        if (type instanceof Class) {
            Class<?> typeAsClass = (Class<?>) type;
            if (typeAsClass.isArray()) {
                return creator.body(type);
            }
        }

        return newLambda()
                .parameter(newParameter(type(type), creator.modelName))
                .expression(() -> creator.body(type))
                .generate();
    }


    private JCExpression body(Type type) {
        if (type instanceof Class) {
            return body((Class<?>) type);
        }

        if (type instanceof ParameterizedType) {
            return body((ParameterizedType) type);
        }

        if (type instanceof GenericArrayType) {
            return body((GenericArrayType) type);
        }

        throw new GenerationException(format(UNSUPPORTED_TYPE, type.getTypeName()));
    }

    private JCExpression body(Class<?> modelClass) {
        if (byte[].class.equals(modelClass)) {
            return select(BINARY_MAPPING_TYPE, FROM_BINARY);
        }
        if (modelClass.isArray()) {
            if (isJavaPrimitiveType(modelClass.getComponentType())) {
                return select(ARRAY_MAPPING_TYPE, selectFromArrayJavaPrimitiveMethod(modelClass));
            }
            JCExpression parameterMapper = fromModelMapper(modelClass.getComponentType());
            return method(ARRAY_MAPPING_TYPE, FROM_ARRAY).addArguments(parameterMapper).apply();
        }
        if (isPrimitiveType(modelClass)) {
            return select(PRIMITIVE_MAPPING_TYPE, selectFromPrimitiveMethod(modelClass));
        }
        JCMethodInvocation builderInvocation = method(ENTITY_TYPE, ENTITY_BUILDER_NAME).apply();
        for (Field field : getProperties(modelClass)) {
            String fieldName = field.getName();
            Type fieldType = field.getGenericType();
            builderInvocation = forField(builderInvocation, fieldName, fieldType);
        }
        return method(builderInvocation, BUILD_METHOD_NAME).apply();
    }

    private JCExpression body(ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();
        if (!(rawType instanceof Class)) {
            throw new GenerationException(format(UNSUPPORTED_TYPE, rawType.getTypeName()));
        }
        Class<?> rawClass = (Class<?>) rawType;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (isCollectionType(rawClass)) {
            JCExpression parameterMapper = fromModelMapper(typeArguments[0]);
            return method(ARRAY_MAPPING_TYPE, selectFromCollectionMethod(rawClass))
                    .addArguments(parameterMapper)
                    .apply();
        }
        if (Map.class.isAssignableFrom(rawClass)) {
            if (isComplexType(typeArguments[0])) {
                throw new GenerationException(format(UNSUPPORTED_TYPE, typeArguments[0]));
            }
            JCExpression keyToModelMapper = toModelMapper(typeArguments[0]);
            JCExpression keyFromModelMapper = fromModelMapper(typeArguments[0]);
            JCExpression valueMapper = fromModelMapper(typeArguments[1]);
            return method(ENTITY_MAPPING_TYPE, FROM_MAP)
                    .addArguments(keyToModelMapper, keyFromModelMapper, valueMapper)
                    .apply();
        }
        JCMethodInvocation builderInvocation = method(ENTITY_TYPE, ENTITY_BUILDER_NAME).apply();
        for (Field field : getProperties(rawClass)) {
            String fieldName = field.getName();
            Type fieldType = extractGenericPropertyType(parameterizedType, field.getGenericType());
            builderInvocation = forField(builderInvocation, fieldName, fieldType);
        }
        return method(builderInvocation, BUILD_METHOD_NAME).apply();
    }

    private JCExpression body(GenericArrayType genericArrayType) {
        JCExpression parameterMapper = fromModelMapper(genericArrayType.getGenericComponentType());
        return method(ARRAY_MAPPING_TYPE, FROM_ARRAY).addArguments(parameterMapper).apply();
    }

    private JCMethodInvocation forField(JCMethodInvocation builderInvocation, String fieldName, Type fieldType) {
        if (isLazyValue(fieldType)) {
            return forLazyField(builderInvocation, fieldName, (ParameterizedType) fieldType);
        }
        List<JCExpression> arguments = dynamicArray();
        arguments.add(literal(fieldName));
        String method = (isBoolean(fieldType) ? IS_NAME : GET_NAME) + capitalize(fieldName);
        JCMethodInvocation getter = method(modelName, method).apply();
        arguments.add(newLambda().expression(() -> getter).generate());
        arguments.add(fromModelMapper(fieldType));
        return method(builderInvocation, LAZY_PUT_NAME).addArguments(arguments).apply();
    }

    private JCMethodInvocation forLazyField(JCMethodInvocation builderInvocation, String fieldName, ParameterizedType fieldType) {
        List<JCExpression> arguments = dynamicArray();
        arguments.add(literal(fieldName));
        arguments.add(method(modelName, GET_NAME + capitalize(fieldName)).apply());
        arguments.add(fromModelMapper(fieldType.getActualTypeArguments()[0]));
        return method(builderInvocation, LAZY_PUT_NAME).addArguments(arguments).apply();
    }
}
