pipeline {
    agent any

    options {
        timestamps()
    }

    triggers {
        githubPush() // Déclenchement automatique sur push GitHub
    }

    stages {
        stage("Checkout") {
            steps {
                echo "📥 Récupération du code source..."
                checkout scm
            }
        }

        stage("Verify Docker") {
            steps {
                echo "🔍 Vérification du daemon Docker..."
                bat """
                    docker info || (
                        echo Docker daemon non disponible
                        exit 1
                    )
                """
            }
        }

        stage("Build & Push Docker Image") {
            steps {
                script {
                    // Tag sécurisé basé sur la branche ou master
                    def src = (env.BRANCH_NAME ?: env.GIT_BRANCH ?: 'master')
                    def safeTag = src.replaceAll('[^A-Za-z0-9._-]', '-')
                    def imageName = "awamoussasene/demo-ci-cd-java"
                    def imageTag = "${imageName}:${safeTag}-${env.BUILD_NUMBER}"
                    def latestImageTag = "${imageName}:latest"

                    echo "🐳 Construction de l'image Docker: ${imageTag}"

                    // Build l'image
                    bat "docker build -t ${imageTag} ."

                    // Login Docker Hub avec les credentials Jenkins
                    withCredentials([usernamePassword(credentialsId: 'java-dockerhub-creds',
                                                      usernameVariable: 'DOCKERHUB_USR',
                                                      passwordVariable: 'DOCKERHUB_PSW')]) {
                        bat 'echo %DOCKERHUB_PSW% | docker login -u %DOCKERHUB_USR% --password-stdin'
                    }

                    // Push des tags
                    bat "docker push ${imageTag}"
                    bat "docker tag ${imageTag} ${latestImageTag}"
                    bat "docker push ${latestImageTag}"

                    echo "✅ Image Docker construite et publiée avec succès."
                }
            }
        }

        stage("Deploy to Render") {
            steps {
                echo "🚀 Déclenchement du déploiement sur Render..."
                withCredentials([string(credentialsId: 'java-render-webhook', variable: 'HOOK_URL')]) {
                    bat 'curl -i -X POST "%HOOK_URL%"'
                }
                echo "✅ Déploiement déclenché."
            }
        }

        stage("Verify Application (optionnel)") {
            steps {
                withCredentials([string(credentialsId: 'java-render-app-url', variable: 'APP_URL')]) {
                    bat 'curl -I "%APP_URL%" || echo "⚠️ Impossible de contacter l’application"'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
            echo "✨ Pipeline terminé."
        }
        success {
            echo "🎉 Succès: Le pipeline s'est terminé avec succès!"
        }
        failure {
            echo "❌ Échec: Le pipeline a échoué."
        }
    }
}
