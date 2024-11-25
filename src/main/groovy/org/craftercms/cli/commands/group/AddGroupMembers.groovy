package org.craftercms.cli.commands.group

import org.craftercms.cli.commands.AbstractCommand
import picocli.CommandLine

@CommandLine.Command(name = 'add-group-members', description = 'Adds one or more users to a group')
class AddGroupMembers extends AbstractCommand {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec commandSpec

    @CommandLine.Option(names = ['-gn', '--group-name'], required = true, description = 'Group name. Required if not bulk importing from a file.')
    String groupName

    @CommandLine.Option(names = ['-u', '--users'], split = ',', required = true, description = 'Comma separated list of users to add')
    String[] users

    def validateParameters() {
        if (!groupName) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required option group-name')
        }

        if (!users) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), 'Missing required option users')
        }
    }

    @Override
    def run(client) {
        validateParameters()
        addMembers(client, groupName, users)
    }

    /**
     * Add one or more users to a group
     * @param client HTTPClient object
     * @param groupName group name
     * @param users list of users to add
     */
    static def addMembers(client, groupName, users) {
        def response = getGroupIdByName(client, groupName)

        if (!response || !response.group) {
            println "Error retrieving group '${groupName}'"
            return
        }

        def groupId = response.group.id
        def path = "/studio/api/2/groups/${groupId}/members"
        def body = [usernames: users]

        def result = client.post(path, body)
        if (!result) {
            println "Failed to add members '${users}' to group '${groupName}'"
            return
        }

        println "Members '${users}' added to group '${groupName}' succesfully"
    }

    /**
     * Get a group by name
     * @param client HTTPClient object
     * @param groupName group name
     */
    static def getGroupIdByName(client, groupName) {
        def path = "/studio/api/2/groups/by_name/${groupName}"
        def result = client.get(path)
        if (!result) {
            println "Failed to retrieve group '${groupName}'"
            return
        }

        if (!result.group) {
            println "Failed to retrieve group '${groupName}', error '${result.message}'"
            return
        }

        return result
    }
}