@Grab(group='org.codehaus.groovy', module='groovy-yaml', version='3.0.8')
import groovy.yaml.YamlSlurper

def newFolder = { folderName ->
    folder(folderName) {
        displayName(folderName)
    }
}
def newPipeline = { String folderName, String appName, String scmUrl, String scmBranch ->
    pipelineJob("${folderName}/${appName}") {
        definition {
            cpsScm {
                scm {
                    git {
                        remote { url(scmUrl) }
                        branches(scmBranch)
                        extensions { }  // required as otherwise it may try to tag the repo, which you may not want
                    }
                }
                scriptPath('tools/jenkins/DeployToTest.groovy')
            }
        }
        logRotator { numToKeep(5) }
    }
}
def thisBuild = Thread.currentThread().executable
def resolver = thisBuild.buildVariableResolver
def envName = resolver.resolve('ENV_NAME')

newFolder(envName)

def yamlSlurper = new YamlSlurper()
inventory = yamlSlurper.parse(new File("${WORKSPACE}/jenkins/inventory.yaml"))
apps = inventory.applications

apps.each { app ->
    app.environments.each {
        if (envName.equalsIgnoreCase(it.acronym)) {
            newPipeline(it.acronym, app.name, app.git, 'main')
        }
    }
}