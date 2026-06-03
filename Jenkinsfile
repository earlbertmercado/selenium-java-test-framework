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
        emailext(
            to: 'earlbertmercado@gmail.com',
            subject: "PIPELINE EMAIL TEST - ${currentBuild.currentResult}",
            body: "If you receive this, pipeline email works."
        )
    }
}
}