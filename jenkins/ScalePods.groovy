import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

def project = { String envName ->
    def prefix = '77c02f-'

    if (envName == 'TEST')
        return "${prefix}dev"
    else if (envName == 'UAT')
        return "${prefix}test"
    else if (envName == 'PROD')
        return "${prefix}prod"
    else
        return "${prefix}tools"
}

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(daysToKeepStr: '', numToKeepStr: '5'))
    }
    environment {
        APP_NAME = 'student-grad-tools'
    }
    parameters {
        choice(
                name: 'Desired_number_of_Pods',
                choices: ['0', '1', '2', '3'],
                default: '2'
        )
        choice(
                name: 'Environment',
                choices: ['DEV', 'TEST'],
                default: 'DEV'
        )
    }
    stages {
        stage ('Init') {
            steps {
                script {
                    println "Scale to => ${env.Desired_number_of_Pods} in ${env.Environment}"
                    sh "oc --project ${project}; oc get dc"
                }
            }
            post {
                success {
                    echo 'Init Complete'
                }
                failure {
                    echo 'Init Failed'
                }
            }
        }
        /*stage ('Scale-Pods') {
            steps {
                script {
                    oc scale dc --replicas=2 educ-grad-assessment-api-dc educ-grad-course-api-dc
                }
            }
            post {
                success {
                    echo 'Folder added Successfully'
                }
                failure {
                    echo 'Failed adding folder'
                }
            }
        }
        stage ('print-apps') {
            steps {
                script {
                    def slurper = new JsonSlurper()
                    def inventory = slurper.parse(
                            "https://raw.githubusercontent.com/bcgov/EDUC-GRAD-TOOLS/main/tools/jenkins/inventory.json".toURL()
                    )
                    def apps = inventory.applications
                    println ''
                    apps.each {
                        app -> println app.name
                    }
                    println ''
                }
            }
        }*/
    }
    post {
        success {
            println 'Scaling Complete'
        }
        failure {
            println 'Scaling Failed'
        }
    }
}
