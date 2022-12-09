/*
 * Copyright 2022 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exactpro.th2.cachecommon.entities.configuration

class CustomConfigurationClass {
    var arangoHost: String = "localhost"
    var arangoPort: Int = 8529
    var arangoUser: String = "root"
    var arangoPassword: String = "openSesame"
    var arangoDbName: String = "_system"

    var hostname: String = "localhost"
    var port: Int = 8080
    var responseTimeout: Int = 60000

    var checkRequestsAliveDelay: Long = 2000
    val enableCaching: Boolean = true
    val notModifiedObjectsLifetime: Int = 3600
    val rarelyModifiedObjects: Int = 500

    override fun toString(): String {
        return "CustomConfigurationClass(arangoHost='$arangoHost', arangoPort=$arangoPort, arangoUser='$arangoUser', arangoPassword='$arangoPassword', arangoDbName='$arangoDbName', hostname='$hostname', port=$port, responseTimeout=$responseTimeout, checkRequestsAliveDelay=$checkRequestsAliveDelay, enableCaching=$enableCaching, notModifiedObjectsLifetime=$notModifiedObjectsLifetime, rarelyModifiedObjects=$rarelyModifiedObjects)"
    }
}

class Configuration(customConfiguration: CustomConfigurationClass) {
    val arangoHost: Variable =
        Variable("arangoHost", customConfiguration.arangoHost, "localhost")

    val arangoPort: Variable =
        Variable("arangoPort", customConfiguration.arangoPort.toString(), "8529")

    val arangoUser: Variable =
        Variable("arangoUser", customConfiguration.arangoUser, "root")

    val arangoPassword: Variable =
        Variable("arangoPassword", customConfiguration.arangoPassword, "openSesame", false)

    val arangoDbName: Variable =
        Variable("arangoDbName", customConfiguration.arangoDbName, "_system")

    val hostname: Variable =
        Variable("hostname", customConfiguration.hostname, "localhost")

    val port: Variable =
        Variable("port", customConfiguration.port.toString(), "8080")

    val responseTimeout: Variable =
        Variable("responseTimeout", customConfiguration.responseTimeout.toString(), "60000")

    val checkRequestsAliveDelay: Variable =
        Variable("checkRequestsAliveDelay", customConfiguration.checkRequestsAliveDelay.toString(), "2000")

    val enableCaching: Variable = Variable("enableCaching", customConfiguration.enableCaching.toString(), "true")

    val notModifiedObjectsLifetime: Variable =
        Variable("notModifiedObjectsLifetime", customConfiguration.notModifiedObjectsLifetime.toString(), "3600")

    val rarelyModifiedObjects: Variable =
        Variable("rarelyModifiedObjects", customConfiguration.rarelyModifiedObjects.toString(), "500")
}
