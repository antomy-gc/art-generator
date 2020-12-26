package io.art.generator.collector;

import io.art.core.collection.*;
import io.art.generator.exception.*;
import io.art.model.implementation.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.generator.constants.GeneratorConstants.ExceptionMessages.*;
import java.lang.reflect.*;

@UtilityClass
public class ServiceTypesCollector {
    public ImmutableSet<Type> collectModelTypes(ServerModel serverModel) {
        ImmutableSet.Builder<Type> types = immutableSetBuilder();
        ImmutableMap<String, ServiceModel<?>> services = serverModel.getServices();
        for (ServiceModel<?> service : services.values()) {
            for (Method method : service.getServiceClass().getDeclaredMethods()) {
                Type[] parameterTypes = method.getGenericParameterTypes();
                if (parameterTypes.length > 1) {
                    throw new GenerationException(MORE_THAN_ONE_PARAMETER);
                }
                types.addAll(TypeCollector.collectModelTypes(method.getGenericReturnType()));
                if (isNotEmpty(parameterTypes)) {
                    types.addAll(TypeCollector.collectModelTypes(parameterTypes[0]));
                }
            }
        }
        return types.build();
    }
}