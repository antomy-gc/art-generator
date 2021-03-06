package io.art.generator.creator.provider;

import com.sun.tools.javac.tree.*;
import io.art.core.collection.*;
import io.art.generator.caller.*;
import io.art.generator.model.*;
import io.art.model.implementation.module.*;
import lombok.*;
import lombok.experimental.*;
import static com.sun.tools.javac.code.Flags.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.handler.ExceptionHandler.*;
import static io.art.generator.caller.MethodCaller.*;
import static io.art.generator.collector.CommunicatorTypesCollector.*;
import static io.art.generator.collector.ServiceTypesCollector.*;
import static io.art.generator.collector.TypeCollector.*;
import static io.art.generator.constants.Imports.*;
import static io.art.generator.constants.JavaDialect.*;
import static io.art.generator.constants.LoggingMessages.*;
import static io.art.generator.constants.Names.*;
import static io.art.generator.constants.TypeModels.*;
import static io.art.generator.context.GeneratorContext.*;
import static io.art.generator.creator.decorate.DecorateMethodCreator.*;
import static io.art.generator.finder.ConfigureMethodFinder.*;
import static io.art.generator.implementor.CommunicatorModelImplementor.*;
import static io.art.generator.implementor.ConfiguratorModelImplementor.*;
import static io.art.generator.implementor.MappersImplementor.*;
import static io.art.generator.implementor.ServerModelImplementor.*;
import static io.art.generator.logger.GeneratorLogger.*;
import static io.art.generator.model.ImportModel.*;
import static io.art.generator.model.NewClass.*;
import static io.art.generator.model.NewField.*;
import static io.art.generator.model.NewMethod.*;
import static io.art.generator.model.TypeModel.*;
import static io.art.generator.service.JavacService.*;
import static io.art.generator.state.GeneratorState.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;
import javax.lang.model.element.Modifier;
import java.lang.reflect.*;
import java.util.*;

@UtilityClass
public class ProviderClassCreator {
    public NewClass createProviderClass(ModuleModel model) {
        NewClass providerClass = newClass().modifiers(PUBLIC)
                .addImport(classImport(moduleClass().getFullName()))
                .name(moduleClass().getName() + PROVIDER_CLASS_SUFFIX);

        stream(IMPORTING_CLASSES).map(ImportModel::classImport).forEach(providerClass::addImport);

        Set<Type> types = combineToSet(
                model.getValueModel().getCustomTypes().stream().flatMap(type -> collectModelTypes(type).stream()).collect(setCollector()),
                collectServerTypes(model.getServerModel()).toMutable(),
                collectCommunicatorTypes(model.getCommunicatorModel()).toMutable()
        );

        NewMethod mappersMethod = implementMappersMethod(immutableSetOf(types));
        ImmutableArray<NewClass> mappers = implementTypeMappers(immutableSetOf(types));
        success(GENERATED_MAPPERS);

        NewMethod servicesMethod = implementServicesMethod(model.getServerModel());
        success(GENERATED_SERVICE_SPECIFICATIONS);

        NewMethod communicatorsMethod = implementCommunicatorsMethod(model.getCommunicatorModel());
        ImmutableArray<NewClass> communicatorProxies = implementCommunicatorProxies(model.getCommunicatorModel());
        success(GENERATED_COMMUNICATOR_PROXIES);

        NewMethod configurationsMethod = implementCustomConfigurationsMethod(model.getConfiguratorModel());
        ImmutableArray<NewClass> customConfigurators = implementCustomConfigurators(model.getConfiguratorModel());
        success(GENERATED_CUSTOM_CONFIGURATION_PROXIES);

        return providerClass
                .field(createModelField())
                .method(createProvideMethod())
                .method(createDecorateMethod())
                .method(mappersMethod)
                .method(servicesMethod)
                .method(communicatorsMethod)
                .method(configurationsMethod)
                .inners(mappers)
                .inners(communicatorProxies)
                .inners(customConfigurators);
    }


    public NewClass createProviderStub(ExistedClass moduleClass) {
        return newClass().modifiers(PUBLIC)
                .addImports(stream(IMPORTING_CLASSES).map(ImportModel::classImport).collect(setCollector()))
                .addImport(classImport(moduleClass.getFullName()))
                .name(moduleClass.getName() + PROVIDER_CLASS_SUFFIX)
                .method(newMethod()
                        .name(PROVIDE_NAME)
                        .modifiers(PUBLIC | FINAL | STATIC)
                        .returnType(MODULE_MODEL_TYPE)
                        .statement(() -> throwException(NOT_IMPLEMENTED_EXCEPTION_TYPE, literal(PROVIDE_NAME))));
    }

    private NewMethod createProvideMethod() {
        return newMethod()
                .name(PROVIDE_NAME)
                .modifiers(PUBLIC | FINAL | STATIC)
                .returnType(MODULE_MODEL_TYPE)
                .statement(() -> returnVariable(MODEL_STATIC_NAME));
    }

    @SneakyThrows
    private NewField createModelField() {
        ExistedClass moduleClass = moduleClass();
        ExistedMethod configureMethod = findConfigureAnnotatedMethod(moduleClass);

        MethodCaller configureStatic = method(type(moduleClass.asClass()), configureMethod.getName());

        MethodCaller singletonMethod = method(SINGLETON_REGISTRY_TYPE, SINGLETON_NAME)
                .addArgument(classReference(moduleClass.asClass()))
                .addArgument(newReference(type(moduleClass.asClass())));
        MethodCaller configureSingleton = method(singletonMethod.apply(), configureMethod.getName());

        MethodCaller configureInstance = method(select(type(moduleClass.asClass()), INSTANCE_FIELD_NAME), configureMethod.getName());

        JCTree.JCExpression decorate = configureMethod.getDeclaration().getModifiers().getFlags().contains(Modifier.STATIC)
                ? configureStatic.apply()
                : dialect() == KOTLIN && nonNull(nullIfException(() -> moduleClass.asClass().getField(INSTANCE_FIELD_NAME)))
                ? configureInstance.apply() : configureSingleton.apply();

        return newField()
                .name(MODEL_STATIC_NAME)
                .modifiers(PRIVATE | FINAL | STATIC)
                .type(MODULE_MODEL_TYPE)
                .initializer(() -> method(DECORATE_NAME).addArguments(decorate).apply());
    }
}
