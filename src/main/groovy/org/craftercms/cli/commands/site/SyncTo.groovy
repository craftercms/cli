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

import org.craftercms.cli.commands.AbstractSyncCommand
import picocli.CommandLine

@CommandLine.Command(name = 'sync-to', description = 'Sync the content of a project to a remote repository')
class SyncTo extends AbstractSyncCommand {

    @CommandLine.Option(names = ['-f', '--force'], description = 'Forces the update of the remote repository')
    boolean force

    def run(client) {
        def params = [
                siteId      : siteOptions.siteId,
                remoteName  : remoteOptions.remoteName,
                remoteBranch: remoteOptions.remoteBranch
        ]
        if (force) {
            params.force = force
        }

        client.post {
            request.uri.path = '/studio/api/2/repository/push_to_remote.json'
            request.body = params
        }.with {
            println response.message
        }
    }

}
