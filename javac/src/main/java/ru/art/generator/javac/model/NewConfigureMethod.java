package ru.art.generator.javac.model;

import com.sun.tools.javac.util.*;
import io.art.core.constants.*;
import io.art.model.module.*;
import io.art.model.server.*;
import io.art.server.specification.*;
import lombok.experimental.*;
import static com.sun.tools.javac.code.Flags.*;
import static ru.art.generator.javac.constants.GeneratorConstants.MethodNames.*;
import static ru.art.generator.javac.context.GenerationContext.*;
import static ru.art.generator.javac.model.NewBuilder.*;
import static ru.art.generator.javac.model.NewLambda.*;
import static ru.art.generator.javac.model.NewMethod.*;
import static ru.art.generator.javac.model.NewParameter.*;
import static ru.art.generator.javac.model.TypeModel.*;
import static ru.art.generator.javac.service.MakerService.*;

@UtilityClass
public class NewConfigureMethod {
    public static NewMethod configureMethod(ModuleModel model) {
        return newMethod()
                .modifiers(PUBLIC | STATIC)
                .name(CONFIGURE_METHOD_NAME)
                .returnType(type(ModuleModel.class.getName()))
                .statement(() -> maker().Return(
                        applyMethod(MODULE_METHOD_NAME, "serve", List.of(
                                newLambda()
                                        .parameter(newParameter(type(ServerModel.class), "server"))
                                        .expression(() -> applyMethod("server", "rsocket", List.of(
                                                newBuilder(type(ServiceMethodSpecification.class))
                                                        .method("serviceId", literal("serviceId"))
                                                        .method("methodId", literal("methodId"))
                                                        .method("outputMode", select(type(MethodProcessingMode.class), "BLOCKING"))
                                                        .method("inputMode", select(type(MethodProcessingMode.class), "BLOCKING"))
                                                        .generate()
                                        )))
                                        .generate()
                        ))
                ));
    }
}
