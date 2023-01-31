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
import org.craftercms.cli.options.AuthOptions
import org.craftercms.cli.options.GitOptions
import org.craftercms.cli.options.SiteOptions
import picocli.CommandLine

@CommandLine.Command(name = 'create-site', description = 'Creates a project from a blueprint or a remote repository')
class CreateSite extends AbstractCommand {

    @CommandLine.Mixin
    SiteOptions siteOptions

    @CommandLine.Mixin
    AuthOptions authAware

    @CommandLine.Mixin
    GitOptions gitOptions

    @CommandLine.Option(names = ['-d', '--description'], description = 'The description for the project')
    String description

    @CommandLine.Option(names = '--blueprint', description = 'The id of the project blueprint')
    String blueprint

    @CommandLine.Option(names = ['-o', '--orphan'], description = 'Discards the history from the remote repository')
    boolean orphan

    @CommandLine.Option(names = '--singleBranch', description = 'Fetch only the given branch from the remote repository')
    boolean singleBranch

    @CommandLine.Option(names = '--sandboxBranch', description = 'The name of the branch for the local repository')
    String sandboxBranch

    @CommandLine.Option(names = ['--siteParam'], description = "Parameter for the blueprint")
    Map<String, String> siteParams

    def additionalValidations() {
        if (gitOptions) {
            gitOptions.validCombination()
        }
        if (!gitOptions || gitOptions.createOption == 'push' && !blueprint) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required option blueprint')
        }
    }

    def run(client) {
        def params = [
                site_id            : siteOptions.siteId,
                name               : siteOptions.siteId, // TODO: Add support for site name in 3.2
                authentication_type: authAware.authType,
                create_as_orphan   : orphan,
                single_branch      : singleBranch,
                create_option      : gitOptions.createOption
        ]
        if (gitOptions) {
            params.create_option = gitOptions.createOption
            params.use_remote = gitOptions.remote

            if (gitOptions.remoteName) {
                params.remote_name = gitOptions.remoteName
            }
            if (gitOptions.url) {
                params.remote_url = gitOptions.url
            }
            if (gitOptions.remoteBranch) {
                params.remote_branch = gitOptions.remoteBranch
            }
            if (authAware.username) {
                params.remote_username = authAware.username
            }
            if (authAware.password) {
                params.remote_password = authAware.password
            }
            if (authAware.token) {
                params.remote_token = authAware.token
            }
            if (authAware.privateKey) {
                // trim to avoid issues with empty lines
                params.remote_private_key = authAware.privateKey.text.trim()
            }
        }
        if (blueprint) {
            params.blueprint = blueprint
        }
        if (sandboxBranch) {
            params.sandbox_branch = sandboxBranch
        }
        if (description) {
            params.description = description
        }
        if (siteParams) {
            params.site_params = siteParams
        }

        client.post {
            request.uri.path = "/studio/api/1/services/api/1/site/create.json"
            request.body = params
        }.with {
            println message
        }
    }

}
