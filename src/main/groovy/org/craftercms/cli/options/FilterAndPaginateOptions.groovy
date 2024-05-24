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

class FilterAndPaginateOptions {

    @CommandLine.Option(names = ['-k', '--keyword'], description = 'A keyword to filter by')
    String keyword

    @CommandLine.Option(names = ['-os', '--offset'], description = 'Offset of first record in the response. Default is 0.')
    String offset

    @CommandLine.Option(names = ['-l', '--limit'], description = 'Number of records to return. Default is 10.')
    String limit

    @CommandLine.Option(names = ['-s', '--sort'],
            description = 'The fields to use for sorting, plus the asc or desc keyword case-insensitive. Multiple fields are separated by commas. Example: column1 ASC, column2 DESC.')
    String sort

    @CommandLine.Option(names = ['-o', '--output'], description = 'Output file')
    String output
}