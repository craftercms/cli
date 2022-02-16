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

package org.craftercms.cli.commands.site

import org.craftercms.cli.commands.AbstractCommand
import picocli.CommandLine

@CommandLine.Command(name = 'list-sites', description = 'List the sites that the current user can access')
class ListSites extends AbstractCommand {

    def run(client) {
        client.get {
            request.uri.path = '/studio/api/2/users/me/sites.json'
        }.with {
            if (sites) {
                sites.each {
                    println " ${it.name} (${it.siteId})"
                }
            } else {
                println "There are no sites"
            }
        }
    }

}
