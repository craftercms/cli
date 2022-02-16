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

package org.craftercms.cli.options

import picocli.CommandLine
import picocli.CommandLine.Model.CommandSpec

class GitOptions {

    @CommandLine.Mixin
    RemoteOptions remoteOptions

    @CommandLine.ArgGroup
    CreateOptions createOptions

    @CommandLine.Option(names = ['-r', '--remote'], description = 'Enable the options for using a remote repository')
    boolean remote

    @CommandLine.Option(names = ['-u', '--url'], description = 'The URL of the remote repository')
    String url

    @CommandLine.Spec
    CommandSpec commandSpec

    def validCombination() {
        if (remote && !url) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required option url')
        }
    }

    def getCreateOption() {
        if (!createOptions) {
            return 'none'
        } else if (createOptions.clone) {
            return 'clone'
        } else {
            return 'push'
        }
    }

    def getRemoteName() {
        remoteOptions.remoteName
    }

    def getRemoteBranch() {
        remoteOptions.remoteBranch
    }

    static class CreateOptions {

        @CommandLine.Option(names = '--clone', description = 'Create a site cloning a remote repository')
        boolean clone

        @CommandLine.Option(names = '--push', description = 'Create a site and push to a remote repository')
        boolean push

        def getCreateOption() {
            if (clone) {
                return 'clone'
            } else {
                return 'push'
            }
        }

    }

}
