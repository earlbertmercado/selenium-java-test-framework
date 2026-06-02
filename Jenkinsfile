pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    parameters {
        choice(
            name: 'BROWSER_NAME', 
            choices: ['chrome', 'firefox', 'edge'], 
            description: 'Browser to run the tests on'
            )

        booleanParam(
            name: 'HEADLESS', 
            defaultValue: true, 
            description: 'Run browser in headless mode'
            )

        choice(
            name: 'TEST_CLASS',
            choices: [
                'All Tests',
                'InventoryTest',
                'ItemDetailTest',
                'LoginTest'
            ],
            description: 'Select which test class to execute'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn -B -DskipTests clean compile'
                    } else {
                        bat 'mvn -B -DskipTests clean compile'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    def cmd = "mvn -B clean test -Dexecution_mode=remote -Dbrowser_name=${params.BROWSER_NAME} -Dheadless=${params.HEADLESS}"
                    if (params.TEST_CLASS != 'All Tests') {
                        cmd += " -Dtest=${params.TEST_CLASS}"
                    }

                    if (isUnix()) {
                        sh cmd
                    } else {
                        bat cmd
                    }
                }
            }
        }

        stage('Publish Results') {
            steps {
                junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                archiveArtifacts allowEmptyArchive: true, artifacts: 'reports/extent-report.html, target/surefire-reports/**/*.html'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
