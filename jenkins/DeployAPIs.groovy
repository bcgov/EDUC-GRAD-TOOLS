import groovy.json.JsonSlurper

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(daysToKeepStr: '', numToKeepStr: '5'))
    }
    environment {
        def projectPrefix = '77c02f-'
        def ocpProject = "${projectPrefix}tools"
        def repos = ""
    }
    parameters {
        choice( name: 'Environment', choices: ['DEV', 'TEST'] )
    }
    stages {
        stage ('Init') {
            // TODO: Get a list of all the GRAD repos from the inventory and populate the repos env variable
        }
        stage ('Deploy-APIs') {
            steps {
                // TODO: Use the defined list in the environment and loop through it instead
                build "${params.Environment}/educ-grad-algorithm-api"
                build "${params.Environment}/educ-grad-assessment-api"
                build "${params.Environment}/educ-grad-batch-graduation-api"
                build "${params.Environment}/educ-grad-course-api"
                build "${params.Environment}/educ-grad-data-conversion-api"
                build "${params.Environment}/educ-grad-graduation-api"
                build "${params.Environment}/educ-grad-graduation-report-api"
                build "${params.Environment}/educ-grad-program-api"
                build "${params.Environment}/educ-grad-report-api"
                build "${params.Environment}/educ-grad-student-api"
                build "${params.Environment}/educ-grad-student-graduation-api"
                build "${params.Environment}/educ-grad-trax-api"
                build "${params.Environment}/educ-rule-engine-api"
            }
            post {
                success { echo 'Deployments complete!' }
                failure { echo 'Deployments failed!' }
            }
        }
    }
    post {
        success { println 'Complete' }
        failure { println 'Failed' }
        always { println '[Send out a notification here]' }
    }
}
