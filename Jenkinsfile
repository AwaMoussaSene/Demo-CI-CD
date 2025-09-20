pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "awamousene/demo-ci-cd"
        DOCKER_TAG = "origin-master-6"
    }

    stages {
        stage('Checkout SCM') {
            steps {
                git url: 'https://github.com/AwaMoussaSene/Demo-CI-CD.git', branch: 'master'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat """
                docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% .
                docker tag %DOCKER_IMAGE%:%DOCKER_TAG% %DOCKER_IMAGE%:latest
                """
            }
        }

        stage('Push Docker Image') {
            steps {
                bat """
                docker push %DOCKER_IMAGE%:%DOCKER_TAG%
                docker push %DOCKER_IMAGE%:latest
                """
            }
        }

        stage('Deploy to Render') {
            steps {
                withCredentials([
                    string(credentialsId: 'java-render-webhook', variable: 'RENDER_WEBHOOK'),
                    string(credentialsId: 'java-render-app-url', variable: 'RENDER_APP_URL')
                ]) {
                    echo "Déploiement sur Render..."
                    bat "curl -X POST \"%RENDER_WEBHOOK%\""
                    echo "App URL: %RENDER_APP_URL%"
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline terminé avec succès ✅"
        }
        failure {
            echo "Le déploiement a échoué... ❌"
        }
    }
}
