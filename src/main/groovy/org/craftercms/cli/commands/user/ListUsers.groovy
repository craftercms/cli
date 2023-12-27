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

package org.craftercms.cli.commands.user

import org.craftercms.cli.commands.AbstractCommand
import picocli.CommandLine
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

@CommandLine.Command(name = 'list-users', description = 'Get all Studio users')
class ListUsers extends AbstractCommand {

    @CommandLine.Option(names = ['-si', '--site-id'], description = 'The site ID to filter users for a particular site')
    String siteId

    @CommandLine.Option(names = ['-k', '--keyword'], description = 'The keyword to filter users')
    String keyword

    @CommandLine.Option(names = ['-os', '--offset'], description = 'Offset of first record in the response. Default is 0.')
    String offset

    @CommandLine.Option(names = ['-l', '--limit'], description = 'Number of records to return. Default is 10.')
    String limit

    @CommandLine.Option(names = ['-s', '--sort'],
            description = 'The fields to use for sorting, plus the asc or desc keyword case-insensitive. Multiple fields are separated by commas. Example: column1 ASC, column2 DESC.')
    String sort

    @CommandLine.Option(names = ['-o', '--output'], description = 'CSV output file to output the users list')
    String output

    @Override
    def run(Object client) {
        def path = '/studio/api/2/users'
        def queryParams = [:]
        if (siteId) {
            queryParams.siteId = siteId
        }
        if (keyword) {
            queryParams.keyword = keyword
        }
        if (sort) {
            queryParams.sort = sort
        }
        queryParams.offset = (offset ?: 0).toString()
        queryParams.limit = (limit ?: 10).toString()
        def result = client.get(path, queryParams)
        if (!result) {
            return
        }

        if (!result.users) {
            println "There are no users"
            return
        }

        println "Total : ${result.total}"
        println "Offset: ${result.offset}"
        println "Limit : ${result.limit}"

        writeResult(result.users)
    }

    /**
     * Get writer to output to file or standard output stream
     * @return the writer
     */
    def getWriter() {
        if (output) {
            def csvFile = new File(output)
            return csvFile.newWriter()
        }

        return new StringWriter()
    }

    /**
     * Write users list to CSV format with a writer
     * @param users list of users
     */
    def writeResult(users) {
        def writer = getWriter()
        def csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)
        csvPrinter.printRecord(['Id', 'Username', 'First Name', 'Last Name', 'Email', 'Enabled', 'Externally Managed'])

        users.each {
            csvPrinter.printRecord([it.id, it.username, it.firstName, it.lastName, it.email, it.enabled, it.externallyManaged])
        }

        if (output) {
            println "Saved to file ${output}."
        } else {
            println '----------'
            println writer.toString()
        }

        csvPrinter.close()
        writer.close()
    }
}
