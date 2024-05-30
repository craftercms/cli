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

package org.craftercms.cli.commands.site

import org.craftercms.cli.commands.AbstractCommand
import org.craftercms.cli.options.SiteOptions
import picocli.CommandLine

// Command to publish content
@CommandLine.Command(name = 'publish-content', description = 'Publish content from a project or site.')
class PublishContent extends AbstractCommand {

    @CommandLine.Mixin
    SiteOptions siteOptions

    @CommandLine.Option(names = ['--items'], description = 'The items to publish')
    String items

    @CommandLine.Option(names = ['--optionalDependencies'], description = 'The optional dependencies')
    String optionalDependencies

    @CommandLine.Option(names = ['--publishingTarget'], description = 'The publishing target (live, staging)')
    String publishingTarget

    @CommandLine.Option(names = ['--schedule'], description = 'The schedule to publish.')
    String schedule

    @CommandLine.Option(names = ['--comment'], description = 'The comment to add')
    String comment

    def additionalValidations() {
        if (!items) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required option items')
        }
        if (!publishingTarget) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required option publishingTarget')
        }
        if (publishingTarget != 'live' && publishingTarget != 'staging') {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Invalid publishing target. Use live or staging')
        }
    }

    def run(client) {
        // Tokenize items and optionalDependencies
        def items = this.items.tokenize(',')
        def optionalDependencies = this.optionalDependencies ? this.optionalDependencies.tokenize(',') : null

        def path = '/studio/api/2/workflow/publish.json'
        def command = [
                siteId              : siteOptions.siteId,
                items               : items,
                optionalDependencies: optionalDependencies,
                publishingTarget    : publishingTarget,
                schedule            : schedule,
                comment             : comment
        ]
        def result = client.post(path, command)
        if (!result) {
            return
        }
        if (schedule) {
            println(result.response.message)
            println "The selected content has been submitted to be published to ${publishingTarget} at ${schedule}"
        } else {
            println(result.response.message)
            println "The selected content has been submitted to be published to ${publishingTarget}"
        }
    }
}
