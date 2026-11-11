pipeline {
  agent any
  tools { maven 'Maven' }
  triggers { pollSCM('H/2 * * * *') }
  environment { NEXUS_URL = 'http://nexus:8081' }
  parameters {
    booleanParam(name: 'PUBLIER', defaultValue: true,
                  description: 'Publier l\'artefact dans Nexus')
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build & Tests') {
      steps {
        // TODO A : construire et tester en mode batch
        sh 'mvn -B clean verify'
      }
    }
    stage('Publish') {
      when { expression { params.PUBLIER }  }
      steps {
          withCredentials([usernamePassword(credentialsId: 'nexus-deploy',
                 usernameVariable: 'NEXUS_USER',
                 passwordVariable: 'NEXUS_PASSWORD')]) {
         sh 'mvn -B -s ci-settings.xml -DskipTests -Dnexus.url=$NEXUS_URL deploy'
          }

      }
    }
    stage('SonarQube Analysis'){
      steps {
        withSonarQubeEnv('sonarqube'){
          sh 'mvn -B sonar:sonar'
        }
      }
    }
    stage('Quality Gate') {
      steps {
        timeout(time:10, unit:'MINUTES'){
          waitForQualityGate abortPipeline: true
        }
      }
    }

  }

  post {
    // TODO C : toujours publier les rapports de tests
    always {
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
    }
    success {
    // TODO D : en cas de succès, archiver le JAR
    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }

  }
}
