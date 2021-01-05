package io.art.generator.constants;

import io.art.communicator.constants.CommunicatorModuleConstants.*;
import io.art.communicator.proxy.*;
import io.art.communicator.registry.*;
import io.art.communicator.specification.*;
import io.art.configurator.custom.*;
import io.art.core.checker.*;
import io.art.core.collection.*;
import io.art.core.constants.*;
import io.art.core.lazy.*;
import io.art.core.model.*;
import io.art.core.singleton.*;
import io.art.core.source.*;
import io.art.launcher.*;
import io.art.model.configurator.*;
import io.art.model.customizer.*;
import io.art.model.implementation.communicator.*;
import io.art.model.implementation.configurator.*;
import io.art.model.implementation.module.*;
import io.art.model.implementation.server.*;
import io.art.rsocket.communicator.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.model.*;
import io.art.server.implementation.*;
import io.art.server.registry.*;
import io.art.server.specification.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.value.constants.ValueModuleConstants.ValueType.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapping.*;
import reactor.core.publisher.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public interface Imports {
    String[] IMPORTING_CLASSES = new String[]{
            Optional.class.getName(),
            List.class.getName(),
            Set.class.getName(),
            Collection.class.getName(),
            Queue.class.getName(),
            Deque.class.getName(),
            Map.class.getName(),
            UUID.class.getName(),
            Duration.class.getName(),
            LocalDateTime.class.getName(),
            ZonedDateTime.class.getName(),
            Stream.class.getName(),

            Flux.class.getName(),
            Mono.class.getName(),

            MethodProcessingMode.class.getName(),
            LazyValue.class.getName(),
            SingletonsRegistry.class.getName(),
            NullityChecker.class.getName(),

            ImmutableMap.class.getName(),
            ImmutableArray.class.getName(),
            ImmutableSet.class.getName(),

            PrimitiveMapping.class.getName(),
            ArrayMapping.class.getName(),
            EntityMapping.class.getName(),
            BinaryMapping.class.getName(),
            LazyValueMapping.class.getName(),
            OptionalMapping.class.getName(),

            ArrayValue.class.getName(),
            BinaryValue.class.getName(),
            Entity.class.getName(),
            Primitive.class.getName(),
            Value.class.getName(),
            ValueType.class.getName(),
            PrimitiveType.class.getName(),

            ValueToModelMapper.class.getName(),
            ValueFromModelMapper.class.getName(),

            ServiceSpecification.class.getName(),
            ServiceMethodSpecification.class.getName(),
            ServiceMethodIdentifier.class.getName(),
            ServiceMethodImplementation.class.getName(),
            ServiceSpecificationRegistry.class.getName(),

            ModuleLauncher.class.getName(),

            ModuleModel.class.getName(),
            ModuleModelConfigurator.class.getName(),
            ServerModuleModel.class.getName(),
            ServerModelConfigurator.class.getName(),

            ModuleCustomizer.class.getName(),
            ValueCustomizer.class.getName(),
            ServerCustomizer.class.getName(),

            ConfigurationSource.class.getName(),
            CustomConfigurationRegistry.class.getName(),
            CustomConfigurator.class.getName(),
            ConfiguratorCustomizer.class.getName(),
            ConfiguratorModuleModel.class.getName(),

            CommunicatorProxyRegistry.class.getName(),
            CommunicatorModuleModel.class.getName(),
            CommunicatorCustomizer.class.getName(),
            CommunicatorSpecification.class.getName(),
            CommunicatorProxy.class.getName(),
            CommunicationProtocol.class.getName(),

            RsocketCommunicator.class.getName(),
            CommunicationMode.class.getName(),
            RsocketSetupPayload.class.getName(),
            RsocketProtocol.class.getName()
    };
}
