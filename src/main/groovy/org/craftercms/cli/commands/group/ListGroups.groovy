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

package org.craftercms.cli.commands.group

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.craftercms.cli.commands.AbstractCommand
import org.craftercms.cli.options.FilterAndPaginateOptions
import picocli.CommandLine

@CommandLine.Command(name = 'list-groups', description = 'List all the groups or search for groups by keyword or sort by a field.')
class ListGroups extends AbstractCommand {

    @CommandLine.Mixin
    FilterAndPaginateOptions filterAndPaginateOptions

    @Override
    def run(Object client) {
        def path = '/studio/api/2/groups'
        def queryParams = [:]
        if (filterAndPaginateOptions.keyword) {
            queryParams.keyword = filterAndPaginateOptions.keyword
        }
        if (filterAndPaginateOptions.sort) {
            queryParams.sort = filterAndPaginateOptions.sort
        }
        queryParams.offset = (filterAndPaginateOptions.offset ?: 0).toString()
        queryParams.limit = (filterAndPaginateOptions.limit ?: 10).toString()
        def result = client.get(path, queryParams)
        if (!result) {
            return
        }

        if (!result.groups) {
            println "There are no groups"
            return
        }

        println "Total : ${result.total}"
        println "Offset: ${result.offset}"
        println "Limit : ${result.limit}"

        writeResult(result.groups)
    }

    /**
     * Get writer to output to file or standard output stream
     * @return the writer
     */
    def getWriter() {
        if (filterAndPaginateOptions.output) {
            def csvFile = new File(filterAndPaginateOptions.output)
            return csvFile.newWriter()
        }

        return new StringWriter()
    }

    /**
     * Write groups list to CSV format with a writer
     * @param groups list of groups
     */
    def writeResult(groups) {
        def writer = getWriter()
        def csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)
        csvPrinter.printRecord(['Id', 'Externally Managed', 'Name', 'Description'])

        groups.each {
            csvPrinter.printRecord([it.id, it.externallyManaged, it.name, it.desc])
        }

        if (filterAndPaginateOptions.output) {
            println "Saved to file ${filterAndPaginateOptions.output}."
        } else {
            println '----------'
            println writer.toString()
        }

        csvPrinter.close()
        writer.close()
    }
}