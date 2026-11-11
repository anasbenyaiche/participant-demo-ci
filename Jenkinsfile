pipeline {
  agent any
  tools { maven 'Maven' }
  triggers { pollSCM('H/2 * * * *') }
  environment { NEXUS_URL = 'http://nexus:8081' }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build & Tests') {
      steps {
        sh 'echo "building"'
      }
    }
    stage('Publish') {
      steps {
        sh 'echo "publish"'
      }
    }
  }

  // post {
  //   // TODO C : toujours publier les rapports de tests
  //   // TODO D : en cas de succès, archiver le JAR

  // }
}
