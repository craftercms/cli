/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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
import groovyx.net.http.HttpException
import picocli.CommandLine
import picocli.CommandLine.Model.CommandSpec

import static groovyx.net.http.ContentTypes.JSON
import static groovyx.net.http.HttpBuilder.configure

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
            def client = login(config)
            run(client)
        } catch (HttpException e) {
            println "Status Code ${e.statusCode}"
            e.body.with {
                if (message) {
                    // API 1 format
                    println "Error Message: ${message}"
                } else {
                    // API 2 format
                    println "Error Code: ${response.code}"
                    println "Error Message: ${response.message}"
                    if (response.remedialAction) {
                        println "Error Action: ${response.remedialAction}"
                    }
                    if (response.documentationUrl) {
                        println "Error Documentation: ${response.documentationUrl}"
                    }
                }
            }
        } catch (e) {
            println e.message
        }
    }

    abstract def run(client)

    def additionalValidations() {

    }

    def getClient(url) {
        return configure {
            request.uri = url
            request.contentType = JSON[0]
            request.cookie('XSRF-TOKEN', 'CRAFTER_CLI_TOKEN')
            request.headers['X-XSRF-TOKEN'] = 'CRAFTER_CLI_TOKEN'

        }
    }

    def login(config) {
        def client = getClient(config.url)
        client.post {
            request.uri.path = "/studio/api/1/services/api/1/security/login.json"
            request.body = [
                    username: config.username,
                    password: config.password
            ]
        }
        client
    }

    def saveConfig(url, username, password) {
        def configFile = new File("$configFolder/.crafter${profile ? "/$profile" : ''}/${environment}")
        if (configFile.exists()) {
            throw new IllegalArgumentException('Environment already exists')
        }
        new FileTreeBuilder(configFolder)
                .dir(".crafter${profile ? "/$profile" : ''}") {
                    file(environment, JsonOutput.toJson([url: url, username: username, password: password]))
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
