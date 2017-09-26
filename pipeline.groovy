pipeline {
    agent {
        docker { image '853142832404.dkr.ecr.eu-west-1.amazonaws.com/jenkins-slave-ubuntu-16.04-armcc' }
    }
    stages {
        stage('Test') {
            steps {
                sh 'armcc'
            }
        }
    }
}
