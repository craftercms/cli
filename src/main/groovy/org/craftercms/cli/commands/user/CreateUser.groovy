/*
 * Copyright (C) 2007-2024 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.cli.commands.user

import org.craftercms.cli.commands.AbstractCommand
import org.craftercms.cli.options.UserOptions
import picocli.CommandLine
import org.apache.commons.csv.CSVParser
import static org.apache.commons.csv.CSVFormat.*
import java.nio.file.Paths

@CommandLine.Command(name = 'create-user', description = 'Creates a user from command parameters or bulk create users from a CSV file')
class CreateUser extends AbstractCommand {

    @CommandLine.Mixin
    UserOptions userOptions

    @CommandLine.Option(names = ['-bf', '--bulk-file'],
            description = 'Absolute path of the CSV file to bulk create users. CSV file should have the header: username,password,firstName,lastName,email,enabled')
    String bulkFile

    @Override
    def run(client) {
        if (bulkFile) {
            Paths.get(bulkFile).withReader { reader ->
                CSVParser csv = new CSVParser(reader, DEFAULT.withHeader())
                for (record in csv.iterator()) {
                    createUser(client, record.toMap())
                }
            }
        } else if (!hasValidUserOptions(userOptions)) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required options to create user.')
        } else {
            createUser(client, userOptions)
        }
    }

    def createUser(client, options) {
        def params = [
                username: options.username,
                password: options.password,
                firstName: options.firstName,
                lastName: options.lastName,
                email: options.email,
                enabled: options.enabled ?: true
        ]

        def path = '/studio/api/2/users'
        def result = client.post(path, params)
        if (result) {
            println "${result.response.message}. User: ${options.username}"
        }
    }

    def hasValidUserOptions(options) {
        return options && options.username && options.password
                && options.email && options.firstName && options.lastName
    }
}
