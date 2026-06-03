pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        timestamps()
    }

    parameters {
        choice(
            name: 'BROWSER',
            choices: ['CHROME','EDGE','FIREFOX'],
            description: 'Select browser to run tests'
        )

        choice(
            name: 'HEADLESS',
            choices: ['true','false'],
            description: 'Enable or disable headless execution'
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
        stage('Install Dependencies / Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh "mvn clean install -DskipTests"
                    } else {
                        bat "mvn clean install -DskipTests"
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def mvnCmd = "mvn clean test -Dbrowser_name=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                    if (params.TEST_CLASS != 'All Tests') {
                        mvnCmd = "${mvnCmd} -Dtest=${params.TEST_CLASS}"
                    }

                    if (isUnix()) {
                        sh mvnCmd
                    } else {
                        bat mvnCmd
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                echo "POST BLOCK EXECUTING"
                def reportFile = "reports/extent-report.html"
                if (fileExists(reportFile)) {
                    emailext(
                        to: 'earlbertmercado@gmail.com',
                        subject: "Saucedemo Selenium Test Report - ${currentBuild.currentResult}",
                        body: "The test execution is complete. Please find the report attached.",
//                         attachmentsPattern: reportFile
                    )
                } else {
                    echo "Report file not found at: ${reportFile}"
                }
            }
        }
        success {
            echo 'Tests executed successfully'
        }
        failure {
            echo 'Build failed. Please review test reports.'
        }
    }
}