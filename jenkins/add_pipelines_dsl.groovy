import groovy.yaml.YamlSlurper

println 'Starting SCRIPT'

folder('TESTING') {
    displayName('TESTING')
    description('Folder for Testing scripts')
}

pipelineJob('TESTING/example') {
    definition {
        cps {
            script(readFileFromWorkspace('jenkins/Jenkinsfile'))
            sandbox()
        }
    }
}