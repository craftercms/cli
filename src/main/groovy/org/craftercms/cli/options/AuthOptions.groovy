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

class AuthOptions {

    @CommandLine.ArgGroup(exclusive = false)
    Auth auth

    static class Credentials {

        @CommandLine.Option(names = '--password', description = 'The password for authentication')
        String password

        @CommandLine.Option(names = '--token', description = 'The token for authentication')
        String token

        @CommandLine.Option(names = '--key', description = 'The path of the private key for authentication')
        File privateKey

    }

    static class Auth {

        @CommandLine.Option(names = ['--username'], description = 'The username for authentication')
        String username

        @CommandLine.ArgGroup(multiplicity = '1')
        Credentials credentials

    }

    def getAuthType() {
        if (!auth) {
            return 'none'
        } else if (auth?.credentials?.password) {
            return 'basic'
        } else if (auth?.credentials?.token) {
            return 'token'
        } else {
            return 'key'
        }
    }

    def getUsername() {
        auth?.username
    }

    def getPassword() {
        auth?.credentials?.password
    }

    def getToken() {
        auth?.credentials?.token
    }

    def getPrivateKey() {
        auth?.credentials?.privateKey
    }

}
