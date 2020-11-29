package io.art.generator.creator.provider;

import com.sun.tools.javac.util.*;
import io.art.generator.model.*;
import io.art.model.module.*;
import lombok.experimental.*;
import static com.sun.tools.javac.code.Flags.*;
import static io.art.generator.constants.GeneratorConstants.*;
import static io.art.generator.constants.GeneratorConstants.Names.*;
import static io.art.generator.constants.GeneratorConstants.TypeModels.*;
import static io.art.generator.context.GeneratorContext.*;
import static io.art.generator.creator.decorate.DecorateMethodCreator.*;
import static io.art.generator.implementor.MappersImplementor.*;
import static io.art.generator.implementor.ServerModelImplementor.*;
import static io.art.generator.model.NewClass.*;
import static io.art.generator.model.NewField.*;
import static io.art.generator.model.NewMethod.*;
import static io.art.generator.model.TypeModel.*;
import static io.art.generator.service.JavacService.*;
import static java.util.Arrays.*;

@UtilityClass
public class ProviderClassCreator {
    public NewClass createProviderClass(ModuleModel model) {
        NewClass providerClass = newClass().modifiers(PUBLIC).name(providerClassName());

        stream(IMPORTING_CLASSES)
                .map(ImportModel::classImport)
                .forEach(providerClass::addImport);

        NewField modelField = newField()
                .name(MODEL_NAME)
                .modifiers(PRIVATE | FINAL | STATIC)
                .type(MODULE_MODEL_TYPE)
                .initializer(() -> applyMethod(DECORATE_NAME, List.of(applyClassMethod(type(mainClass().asClass()), CONFIGURE_NAME))));

        NewMethod modelMethod = newMethod()
                .name(PROVIDE_NAME)
                .modifiers(PUBLIC | FINAL | STATIC)
                .returnType(MODULE_MODEL_TYPE)
                .statement(() -> returnVariable(MODEL_NAME));

        NewField servicesRegistryField = newField()
                .name(SERVICES_REGISTRY_NAME)
                .modifiers(PRIVATE | FINAL | STATIC)
                .type(SERVICE_SPECIFICATION_REGISTRY_TYPE)
                .initializer(() -> applyMethod(SERVICES_NAME));

        return providerClass
                .inner(implementCustomTypeMappers(collectCustomTypes(model.getServerModel())))
                .field(MODEL_NAME, modelField)
                .field(SERVICES_REGISTRY_NAME, servicesRegistryField)
                .method(SERVICES_NAME, implementServerModel(model.getServerModel()))
                .method(MODEL_NAME, modelMethod)
                .method(DECORATE_NAME, createDecorateMethod());
    }
}