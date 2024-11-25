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
import picocli.CommandLine

@CommandLine.Command(name = 'get-group-by-name', description = 'Get a group by name')
class GetGroupIDByName extends AbstractCommand {

    @CommandLine.Option(names = ['-gn', '--group-name'], required = true, description = 'Group name')
    String groupName

    def validateParameters() {
        if (!groupName) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required option group-name')
        }
    }

    @Override
    def run(client) {
        validateParameters()
        getGroupByName(client, groupName)
    }

    /**
     * Get a group by name
     * @param client HTTPClient object
     * @param groupName group name
     */

    static def getGroupByName(client, groupName) {
        def path = "/studio/api/2/groups/by_name/${groupName}"
        def result = client.get(path)
        if (!result) {
            return
        }

        if (!result.group) {
            println 'Group not found'
            return
        }

        println "Group id: ${result.group.id}"
        return result
    }

}
