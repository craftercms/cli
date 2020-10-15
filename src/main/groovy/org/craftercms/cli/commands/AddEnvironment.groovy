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

import groovyx.net.http.HttpException
import picocli.CommandLine

@CommandLine.Command(name = 'add-environment', description = 'Adds the configuration to connect to Crafter CMS')
class AddEnvironment extends AbstractCommand {

    @CommandLine.Option(names = ['-u', '--url'], required = true, description = 'The URL of the server')
    String url

    @CommandLine.Option(names = '--username', required = true, description = 'The username for authentication')
    String username

    @CommandLine.Option(names = '--password', required = true, interactive = true,
            description = 'The password for authentication')
    String password

    @Override
    void run() {
        try {
            login([url: url, username: username, password: password])
            saveConfig(url, username, password)

            println "Added"
        } catch (HttpException e) {
            println e.body.message
        } catch (e) {
            println e.message
        }
    }

    def run(client) {
        // All logic is in run because there is no info to build a client
    }

}
