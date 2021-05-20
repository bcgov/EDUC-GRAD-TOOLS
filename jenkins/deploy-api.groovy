pipeline{
    agent {
        kubernetes {
            label 'maven'
            cloud 'openshift'
            defaultContainer 'jnlp'
            serviceAccount 'jenkins'
            yaml """
        kind: Pod
        metadata:
          name: jenkins-slave
        spec:
          containers:
          - name: jnlp
            image: registry.access.redhat.com/openshift3/jenkins-agent-maven-35-rhel7
            privileged: false
            alwaysPullImage: false
            workingDir: /tmp
            ttyEnabled: false
            volumeMounts:
            - mountPath: '/home/jenkins/.m2'
              name: pvc
          volumes:
          - name: pvc
            persistentVolumeClaim:
              claimName: 'maven-slave-pvc'
      """
        }
    }

    environment{
        APP_NAME = 'educ-grad-code-api'
        APP_DOMAIN = 'apps.silver.devops.gov.bc.ca'
    }

    stages{
        stage('Promote to TEST') {
            steps{
                script {
                    openshift.withCluster() {
                        openshift.withProject('77c02f-dev') {
                            def dcTemplate = openshift.process('-f', 'openshift/api.dc.yaml',
                                    "REPO_NAME=educ-grad-code-api",
                                    "JOB_NAME=main",
                                    "NAMESPACE=${NAMESPACE}",
                                    "APP_NAME=educ-grad-code-api",
                                    "HOST_ROUTE=educ-grad-code-api-77c02f-test.apps.silver.devops.gov.bc.ca",
                                    "TAG=${TAG}"
                            )

                            echo "Applying Deployment educ-grad-code-api"
                            def dc = openshift.apply(dcTemplate).narrow('dc')

                            echo "Waiting for deployment to roll out"
                            // Wait for deployments to roll out
                            timeout(10) {
                                dc.rollout().status('--watch=true')
                            }
                        }
                    }
                }
            }
            post{
                success{
                    echo 'Deployment to Dev was successful'
                }
                failure{
                    echo 'Deployment to Dev failed'
                }
            }
        }
    }
}
