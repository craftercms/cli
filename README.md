![build status](https://app.travis-ci.com/craftercms/cli.svg?branch=develop)

# Crafter CLI
Commandline interface to CrafterCMS

# Examples

The first command needed to work with this tool is `add-environment`, you need to provide a name, the url for a
CrafterCMS authoring server and the authentication information:

`crafter-cli add-environment -e local -u http://localhost:8080 --username john --password`

Once the environment has been added, you can create sites and sync a remote repository:

`crafter-cli create-site -e local -s editorial --blueprint org.craftercms.blueprint.editorial`

`crafter-cli add-remote -e local -s editorial -n origin -u http://github.com/john.doe/editorial.git`

`crafter-cli sync-to -e local -s editorial -n origin -b site-updates`

It is also possible to create a site from a remote repository and later sync it:

```
crafter-cli create-site -e local -s my-site -n My Site \
                        -r --clone -u http://github.com/john/doe/my-site.git -b development \
                        --username john --key /home/john/private_key
```

`crafter-cli sync-from -e local -s editorial -n origin`

For a detailed list of commands & arguments run `crafter-cli help`
