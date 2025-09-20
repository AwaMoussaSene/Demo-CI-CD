pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        // Credentials Docker Hub
        DOCKERHUB = credentials('java-dockerhub-creds')
        IMAGE_NAME = "${DOCKERHUB_USR}/demo-ci-cd-java"
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
                    def src = (env.BRANCH_NAME ?: env.GIT_BRANCH ?: 'master')
                    def safeTag = src.replaceAll('[^A-Za-z0-9._-]', '-')
                    def imageTag = "${IMAGE_NAME}:${safeTag}-${env.BUILD_NUMBER}"
                    def latestImageTag = "${IMAGE_NAME}:latest"

                    echo "🐳 Construction de l'image Docker: ${imageTag}"

                    // Build l'image
                    bat "docker build -t ${imageTag} ."

                    // Login Docker Hub
                    bat """
                        echo ${DOCKERHUB_PSW} | docker login -u ${DOCKERHUB_USR} --password-stdin
                    """

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
                    bat "curl -i -X POST \"${HOOK_URL}\""
                }
                echo "✅ Déploiement déclenché."
            }
        }

        stage("Vérifier Application (optionnel)") {
            steps {
                withCredentials([string(credentialsId: 'java-render-app-url', variable: 'APP_URL')]) {
                    bat "curl -I \"${APP_URL}\" || echo '⚠️ Impossible de contacter l’application'"
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
