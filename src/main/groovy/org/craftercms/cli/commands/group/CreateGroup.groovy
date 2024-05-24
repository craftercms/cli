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

package org.craftercms.cli.commands.group

import org.craftercms.cli.commands.AbstractCommand
import org.craftercms.cli.options.GroupOptions
import picocli.CommandLine
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVFormat
import java.nio.file.Paths

@CommandLine.Command(name = 'create-group', description = 'Creates a group from command parameters or bulk create groups from a CSV file')
class CreateGroup extends AbstractCommand {

    @CommandLine.Mixin
    GroupOptions groupOptions

    @CommandLine.Option(names = ['-bf', '--bulk-file'],
            description = 'Absolute path of the CSV file to bulk create groups. CSV file should have the header: name,desc')
    String bulkFile

    @Override
    def run(client) {
        if (bulkFile) {
            Paths.get(bulkFile).withReader { reader ->
                CSVFormat format = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .build()
                CSVParser csv = new CSVParser(reader, format)
                for (record in csv.iterator()) {
                    createGroup(client, record.toMap())
                }
            }
        } else if (!hasValidGroupOptions(groupOptions)) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required options to create group.')
        } else {
            createGroup(client, groupOptions)
        }
    }

    /**
     * Create a new group
     * @param client HTTPClient object
     * @param options create group options
     */
    def createGroup(client, options) {
        def params = [
                name: options.name,
                desc: options.desc
        ]

        def path = '/studio/api/2/groups'
        def result = client.post(path, params)
        if (result) {
            println "${result.response.message}. Group: ${options.name}"
        }
    }

    /**
     * Check if the options has required fields
     * @param options command options
     * @return true if valid, false otherwise
     */
    def hasValidGroupOptions(options) {
        return options && options.name
    }
}