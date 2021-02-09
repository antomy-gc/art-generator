package ru

import io.art.extensions.communicator
import io.art.extensions.module
import io.art.extensions.scheduleFixedRate
import io.art.launcher.ModuleLauncher.launch
import io.art.model.annotation.Configurator
import ru.ExampleProvider.provide
import ru.communicator.MyClient
import ru.configuration.MyConfig
import ru.model.Model
import ru.model.Request
import ru.model.Response
import ru.service.MyService
import java.time.Duration.ofSeconds

val myClient: MyClient by communicator()

object Example {
    @Configurator
    fun configure() = module {
        value {
            model(Request::class)
            model(Model::class)
            model(Response::class)
        }
        configure { configuration(MyConfig::class) }
        serve { rsocket(MyService) }
        communicate { rsocket(MyClient::class) to MyService }
        onLoad { scheduleFixedRate(ofSeconds(30)) { myClient.myMethod2(Request()) } }
    }

    @JvmStatic
    fun main(vararg arguments: String) = launch(provide())
}
