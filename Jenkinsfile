pipeline {
    agent any

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat 'docker build -t awamousene/demo-ci-cd:origin-master-6 .'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    bat 'docker tag awamousene/demo-ci-cd:origin-master-6 awamousene/demo-ci-cd:latest'
                    bat 'docker push awamousene/demo-ci-cd:origin-master-6'
                    bat 'docker push awamousene/demo-ci-cd:latest'
                }
            }
        }

        stage('Deploy to Render') {
            steps {
                script {
                    def deployHook = 'https://api.render.com/deploy/srv-d34nve0dl3ps73822cbg?key=Js3I0NRGXSU'
                    bat "curl -X POST %deployHook%"

                    def appUrl = 'https://nom-de-ton-app.onrender.com'

                    timeout(time: 2, unit: 'MINUTES') {
                        waitUntil {
                            def response = bat(script: "curl -s -o NUL -w %%{http_code} %appUrl%", returnStdout: true).trim()
                            echo "HTTP status: $response"
                            return (response == '200')
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Déploiement terminé avec succès ! 🎉'
        }
        failure {
            echo 'Le déploiement a échoué... ❌'
        }
    }
}
