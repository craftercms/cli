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

class RemoteOptions {

    @CommandLine.Option(names = ['-n', '--name'], description = 'The name of the remote')
    String remoteName

    @CommandLine.Option(names = ['-b', '--branch'], description = 'The name of the remote branch')
    String remoteBranch

    @CommandLine.Spec
    CommandSpec commandSpec

    def validateAll() {
        if (!remoteName) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), "Missing required option remoteName")
        }
        if (!remoteBranch) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), "Missing required option remoteBranch")
        }
    }

}
