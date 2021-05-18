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
            resourceRequestCpu: 3
            resourceRequestMemory: 2Gi
            resourceLimitCpu: 3
            resourceLimitMemory: 2Gi
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
        TOOLS = '77c02f-tools'
        DEV = '77c02f-dev'
        TEST = '77c02f-test'
        PROD = '77c02f-prod'

        REPO_NAME = 'educ-grad-code-api'
        OWNER = 'bcgov'
        JOB_NAME = 'main'
        TAG = 'dev'

        APP_NAME = 'educ-grad-code-api'
        APP_DOMAIN = 'apps.silver.devops.gov.bc.ca'

        TEST_HOST_ROUTE = "${APP_NAME}-${TEST}.${APP_DOMAIN}"
    }
    stages{
        stage('Promote to TEST') {
            steps{
                deployStage('test', DEV, TEST_HOST_ROUTE, 'dev')
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

def deployStage(String stageEnv, String projectEnv, String hostRouteEnv, String tag) {

    if (!stageEnv.equalsIgnoreCase('dev')) {
        input("Deploy to ${projectEnv}?")
    }

    openshift.withCluster() {
        openshift.withProject(projectEnv) {
            def dcTemplate = openshift.process('-f',
                    'openshift/api.dc.yaml',
                    "REPO_NAME=${REPO_NAME}",
                    "JOB_NAME=${JOB_NAME}",
                    "NAMESPACE=${projectEnv}",
                    "APP_NAME=${APP_NAME}",
                    "HOST_ROUTE=${hostRouteEnv}",
                    "TAG=${tag}"
            )

            echo "Applying Deployment ${REPO_NAME}"
            def dc = openshift.apply(dcTemplate).narrow('dc')

            echo "Waiting for deployment to roll out"
            // Wait for deployments to roll out
            timeout(10) {
                dc.rollout().status('--watch=true')
            }
        }
    }
}
