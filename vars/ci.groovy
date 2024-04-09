pipeline {
  environment {
      DOCKER_REGISTRY_USER = credentials("dockerRegistryUserName")
      DOCKER_REGISTRY_PASS = credentials("dockerRegistryPassword")
  }  
    agent {
        kubernetes {
            label 'my-pod'
            defaultContainer 'docker'
            yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: my-app
  name: my-pod
spec:
  containers:
  - name: docker
    image: docker:latest
    command:
    - cat
    tty: true
  - name: kubectl
    image: bitnami/kubectl:latest
    command:
    - cat
    tty: true
"""
        }
    }
    stages{
        stage('git checkout'){
            steps{
                git 'https://github.com/suruthisk/docker-frontend-backend-db.git'
            }
        }
    }
    stages {
        stage('Docker Build') {
            steps {
                container('docker') {
                    sh 'docker build -f  ./frontend/Dockerfile -t froo:3 ./frontend/
                }
            }
        }
        stage('Docker push') {
            steps {
                Scripts{
                    sh docker push suruthi125/froo:3

                }
            }
        }
        
        
    }
}

