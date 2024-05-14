/* Copyright (C) 2007-2024 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.cli.commands.security

import org.craftercms.cli.commands.AbstractCommand
import org.craftercms.cli.options.AccessTokenOptions
import picocli.CommandLine

@CommandLine.Command(name = 'create-access-token', description = 'Creates an access token for the user')
class CreateAccessToken extends AbstractCommand {

    @CommandLine.Mixin
    AccessTokenOptions tokenOptions

    @Override
    def run(client) {
        createAccessToken(client, tokenOptions)
    }

    static def createAccessToken(client, options) {
        createAccessToken(client, options, false)
    }

    static def createAccessToken(client, options, returnToken) {
        def params = [
            label: options.label,
            expiresAt: options.expiresAt
        ]
        def path = '/studio/api/2/security/tokens'
        def result = client.post(path, params)

        if (result) {
            if (returnToken) {
                return result.token.token
            }
            println "${result.response.message}. Token: ${result.token.token}"
        }
    }

    def additionalValidations() {
        if (tokenOptions) {
            tokenOptions.validate()
        }
    }
}
