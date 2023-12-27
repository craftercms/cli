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

package org.craftercms.cli.options

import picocli.CommandLine

class UserOptions {

    @CommandLine.Option(names = ['-un', '--username'], description = 'Username. Required if --bulk-file is not specified.')
    String username

    @CommandLine.Option(names = ['-pw', '--password'], description = 'User\'s password. Required if --bulk-file is not specified.')
    String password

    @CommandLine.Option(names = ['-fn', '--firstName'], description = 'User\'s first name. Required if --bulk-file is not specified.')
    String firstName

    @CommandLine.Option(names = ['-ln', '--lastName'], description = 'User\'s last name. Required if --bulk-file is not specified.')
    String lastName

    @CommandLine.Option(names = ['-em', '--email'], description = 'User\'s email address. Required if --bulk-file is not specified.')
    String email

    @CommandLine.Option(names = ['-en', '--enabled'], description = 'Indicates if the user is enabled. Default is true.')
    String enabled
}
