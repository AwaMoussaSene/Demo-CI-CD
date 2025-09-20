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
                    // Construire l'image Docker
                    sh 'docker build -t awamousene/demo-ci-cd:origin-master-6 .'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Push image avec tag
                    sh 'docker tag awamousene/demo-ci-cd:origin-master-6 awamousene/demo-ci-cd:latest'
                    sh 'docker push awamousene/demo-ci-cd:origin-master-6'
                    sh 'docker push awamousene/demo-ci-cd:latest'
                }
            }
        }

        stage('Deploy to Render') {
            steps {
                script {
                    // Déclencher le Deploy Hook Render
                    def deployHook = 'https://api.render.com/deploy/srv-d34nve0dl3ps73822cbg?key=Js3I0NRGXSU'
                    sh "curl -X POST $deployHook"

                    // URL de ton application Render
                    def appUrl = 'https://nom-de-ton-app.onrender.com'

                    // Boucle pour attendre que l'application soit disponible
                    timeout(time: 2, unit: 'MINUTES') {
                        waitUntil {
                            def response = sh(script: "curl -s -o /dev/null -w '%{http_code}' $appUrl", returnStdout: true).trim()
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
