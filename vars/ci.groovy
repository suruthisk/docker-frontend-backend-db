pipeline {
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
                    sh 'docker pull suruthi125/froo:3'
                }
            }
        }
        stage('Deploy') {
            steps {
                Scripts{
                    sh 'kubectl apply -f frontdeployment.yaml -n suruthi'
                
                    sh 'kubectl apply -f service.yaml -n suruthi'

                    sh  'kubectl get all -n suruthi' 
                    

                }
            }
        }
        
        
    }
}

