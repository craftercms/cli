# Crafter CLI
Commandline interface to CrafterCMS. Learn more at http://docs.craftercms.org.

# Examples

The first command needed to work with this tool is `add-environment`, you need to provide a name, the url for a
CrafterCMS authoring server and the authentication information:

`crafter-cli add-environment -e local -u http://localhost:8080 --username john --password`

Once the environment has been added, you can create projects and sync a remote repository:

`crafter-cli create-site -e local -s editorial --blueprint org.craftercms.blueprint.editorial`

`crafter-cli add-remote -e local -s editorial -n origin -u http://github.com/john.doe/editorial.git`

`crafter-cli sync-to -e local -s editorial -n origin -b site-updates`

It is also possible to create a project from a remote repository and later sync it:

```
crafter-cli create-site -e local -s my-site -n My Site \
                        -r --clone -u http://github.com/john/doe/my-site.git -b development \
                        --username john --key /home/john/private_key
```

`crafter-cli sync-from -e local -s editorial -n origin`

Also, you are able to publish an item to live or staging (staging must be enabled first before using that target):

`crafter-cli publish-content -e local -s editorial --publishingTarget live --items "/site/website/index.xml"`

`crafter-cli publish-content -e local -s editorial --publishingTarget staging --items "/site/website/index.xml"`

To publish various items to live or staging, you can use the following command (be wary of the quotes and spaces):

`crafter-cli publish-content -e local -s editorial --publishingTarget live --items "/site/website/index.xml,/site/website/health/index.xml"`

You can add optional (soft) dependencies to the publish command:

`crafter-cli publish-content -e local -s editorial --publishingTarget live --items "/site/website/index.xml" --optionalDependencies "/templates/web/pages/category-landing.ftl"`

`crafter-cli publish-content -e local -s editorial --publishingTarget live --items "/site/website/entertainment/index.xml,/site/website/articles/2021/1/men-styles-for-winter/index.xml" --optionalDependencies "/templates/web/pages/category-landing.ftl,/templates/web/pages/article.ftl"`

You can set item(s) to be published in a specific date and time (ISO Date Time Format):

`crafter-cli publish-content -e local -s editorial --publishingTarget live --items "/site/website/index.xml" --schedule "2025-10-31T01:30:00.000-05:00" --comment "My comment"`

For a detailed list of commands & arguments run `crafter-cli help`

# Community
## Contributors
https://github.com/craftercms/craftercms/blob/develop/CONTRIBUTORS.md

## Code of Conduct
https://github.com/craftercms/craftercms/blob/develop/CODE_OF_CONDUCT.md

## Contributing
https://github.com/craftercms/craftercms/blob/develop/CONTRIBUTING.md

## Git Workflow
https://github.com/craftercms/craftercms/blob/develop/GIT_WORKFLOW.md