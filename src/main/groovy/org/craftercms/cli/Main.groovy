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

package org.craftercms.cli

import org.craftercms.cli.commands.AddEnvironment
import org.craftercms.cli.commands.marketplace.CopyPlugin
import org.craftercms.cli.commands.site.AddRemote
import org.craftercms.cli.commands.site.CreateSite
import org.craftercms.cli.commands.site.ListRemotes
import org.craftercms.cli.commands.site.ListSites
import org.craftercms.cli.commands.site.SyncFrom
import org.craftercms.cli.commands.site.SyncTo
import picocli.CommandLine

@CommandLine.Command(
        name = 'crafter-cli', usageHelpAutoWidth = true,
        versionProvider = VersionProvider.class, mixinStandardHelpOptions = true,
        subcommands = [CommandLine.HelpCommand, AddEnvironment, AddRemote, CreateSite, ListRemotes, SyncFrom, SyncTo,
                        ListSites, CopyPlugin])
class Main {

    static def main(args) {
        System.exit(new CommandLine(new Main()).execute(args))
    }

    static class VersionProvider implements CommandLine.IVersionProvider {

        String[] getVersion() throws Exception {
            return [ getClass().getResource('version.txt').text ]
        }

    }

}