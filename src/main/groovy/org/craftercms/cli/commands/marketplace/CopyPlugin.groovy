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

package org.craftercms.cli.commands.marketplace

import org.craftercms.cli.commands.AbstractCommand
import org.craftercms.cli.options.SiteOptions
import picocli.CommandLine

@CommandLine.Command(name = 'copy-plugin', description = 'Copies a plugin from a local folder')
class CopyPlugin extends AbstractCommand {

    @CommandLine.Mixin
    SiteOptions siteOptions

    @CommandLine.Option(names = '--path', description = 'The path for the plugin folder', required = true)
    String path

    def run(client) {
        def params = [
                siteId : siteOptions.siteId,
                path   : path
        ]
        client.post {
            request.uri.path = "/studio/api/2/marketplace/copy"
            request.body = params
        }.with {
            println response.message
        }
    }

}
