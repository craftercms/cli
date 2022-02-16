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

import groovyx.net.http.HttpException
import picocli.CommandLine

@CommandLine.Command(name = 'add-environment', description = 'Adds the configuration to connect to CrafterCMS')
class AddEnvironment extends AbstractCommand {

    @CommandLine.Option(names = ['-u', '--url'], required = true, description = 'The URL of the server')
    String url

    @CommandLine.ArgGroup(multiplicity = "1")
    AuthOptions auth

    @Override
    void run() {
        try {
            def config = [:]
            config.url = url
            if (auth.accessToken) {
                config.token = auth.accessToken
            }
            if (auth.basic?.username && auth.basic?.password) {
                config.username = auth.basic?.username
                config.password = auth.basic?.password
            }
            def client = getClient(config)
            test(client)
            saveConfig(config)

            println "Environment added"
        } catch (HttpException e) {
            println e.body.message
        } catch (e) {
            println e.message
        }
    }

    def test(client) {
        try {
            client.get {
                request.uri.path = '/studio/api/2/users/me.json'
            }.with {
                def user = authenticatedUser
                println "Authenticated as ${user.firstName} ${user.lastName} (${user.username})"
            }
        } catch (e) {
            throw new RuntimeException("Error authenticating user, please check your credentials", e)
        }
    }

    def run(client) {
        // All logic is in run because there is no info to build a client
    }

    static class BasicAuthOptions {

        @CommandLine.Option(names = '--username', required = true, description = 'The username for authentication')
        String username

        @CommandLine.Option(names = '--password', required = true, interactive = true,
                description = 'The password for authentication')
        String password

    }

    static class AuthOptions {

        @CommandLine.ArgGroup(exclusive = false)
        BasicAuthOptions basic

        @CommandLine.Option(names = '--token', required = false, interactive = true,
                description = 'The access token for authentication')
        String accessToken

    }

}
