/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.cli.commands

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.craftercms.cli.utils.HttpClient
import picocli.CommandLine
import picocli.CommandLine.Model.CommandSpec

abstract class AbstractCommand implements Runnable {

    @CommandLine.Option(names = ['--config'], description = 'The folder to store configurations',
            paramLabel = 'path')
    File configFolder = new File(System.getProperty('user.home'))

    @CommandLine.Option(names = ['-p', '--profile'], description = 'The name of the profile')
    String profile

    @CommandLine.Option(names = ['-e', '--environment'], required = true, description = 'The name of the environment')
    String environment

    @CommandLine.Spec
    CommandSpec commandSpec

    void run() {
        additionalValidations()
        try {
            def config = loadConfig()
            def client = HttpClient.getInstance(config)
            run(client)
        } catch (Exception e) {
            println e.message
        }
    }

    abstract def run(client)

    def additionalValidations() {

    }

    def saveConfig(config) {
        def configFile = new File("$configFolder/.crafter${profile ? "/$profile" : ''}/${environment}")
        if (configFile.exists()) {
            throw new IllegalArgumentException('Environment already exists')
        }
        new FileTreeBuilder(configFolder)
                .dir(".crafter${profile ? "/$profile" : ''}") {
                    file(environment, JsonOutput.toJson(config))
                }
    }

    def loadConfig() {
        def configFile = new File("$configFolder/.crafter${profile ? "/$profile" : ''}/${environment}")
        if (!configFile.exists()) {
            throw new IllegalArgumentException("Invalid profile or environment")
        }
        new JsonSlurper()
                .parseText(configFile.text)
    }

}
