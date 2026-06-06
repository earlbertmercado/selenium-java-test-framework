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
            choices: ['Chrome','Edge','Firefox'],
            description: 'Select browser to run tests'
        )

        booleanParam(
            name: 'HEADLESS',
            defaultValue: false,
            description: 'Enable or disable headless mode'
        )

        choice(
            name: 'PAGE',
            choices: [
                'All',
                'Inventory',
                'ItemDetail',
                'Login'
            ],
            description: 'Select which page to test'
        )

        choice(
            name: 'TEST_EXECUTION',
            choices: [
                'Sequential',
                'Parallel-Methods',
                'Parallel-Classes',
                'Parallel-Tests'
            ],
            description: 'Select test execution mode'
        )

        choice(
            name: 'THREAD_COUNT',
            choices: ['2','3','4','5','6','7','8','9','10','11','12'],
            description: 'Number of threads for parallel execution (ignored if SEQUENTIAL)'
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
                    def parallelMode = [
                        'Sequential'        : 'none',
                        'Parallel-Methods'  : 'methods',
                        'Parallel-Classes'  : 'classes',
                        'Parallel-Tests'    : 'tests'
                    ]

                    def mvnCmd = "mvn clean test" +
                                 " -Dbrowser_name=${params.BROWSER}" +
                                 " -Dheadless=${params.HEADLESS}" +
                                 " -Dparallel=${parallelMode[params.TEST_EXECUTION]}" +
                                 " -DthreadCount=${params.THREAD_COUNT}"

                    if (params.PAGE != 'All') {
                        mvnCmd = "${mvnCmd} -Dtest=${params.PAGE}Test"
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
                def reportFile = "reports/extent-report.html"
                if (fileExists(reportFile)) {
                    emailext(
                        to: 'earlbertmercado@gmail.com',
                        subject: "Saucedemo Selenium Test Report - ${currentBuild.currentResult}",
                        body: "The test execution is complete. Please find the report attached.",
                        attachmentsPattern: reportFile
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