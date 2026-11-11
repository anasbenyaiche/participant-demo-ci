pipeline {
  agent any

  tools {
    maven 'Maven'
  }

  triggers {
    pollSCM('H/2 * * * *')
  }

  environment {
    NEXUS_URL = 'http://nexus:8081'
  }

  parameters {
    booleanParam(
      name: 'PUBLIER',
      defaultValue: true,
      description: 'Publier l\'artefact dans Nexus'
    )
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Tests') {
      steps {
        sh 'mvn -B clean verify'
      }
    }

    stage('Publish') {
      when {
        expression { return params.PUBLIER }
      }
      steps {
        withCredentials([
          usernamePassword(
            credentialsId: 'nexus-deploy',
            usernameVariable: 'NEXUS_USER',
            passwordVariable: 'NEXUS_PASSWORD'
          )
        ]) {
          sh 'mvn -B -s ci-settings.xml -DskipTests -Dnexus.url=$NEXUS_URL deploy'
        }
      }
    }

stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube-Local') {
            sh '''
                mvn -B \
                  org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                  -Dsonar.projectKey=demo-ci \
                  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
            '''
        }
    }
}

    stage('Quality Gate') {
      steps {
        timeout(time: 10, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
  }

  post {
    always {
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
    }
    success {
      archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
  }
}
