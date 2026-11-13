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

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('SonarQube-Local') {
            sh '''
                if [ -n "$SONAR_AUTH_TOKEN" ]; then
                    echo "Token injecté correctement"
                else
                    echo "Token absent"
                    exit 1
                fi

                mvn -B \
                  org.sonarsource.scanner.maven:sonar-maven-plugin:5.8.0.7211:sonar \
                  -Dsonar.projectKey=demo-ci \
                  -Dsonar.token="$SONAR_AUTH_TOKEN"
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
    stage('Image Docker') {
      steps {
        sh 'docker build -t demo-ci:${BUILD_NUMBER} -t demo-ci:latest .'
        sh 'docker run --rm demo-ci:${BUILD_NUMBER} 100'
      }
    }
       stage('Push image Nexus') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'nexus-deploy',
                         usernameVariable: 'NEXUS_USER',
                         passwordVariable: 'NEXUS_PASSWORD')]) {
          sh '''
            echo "$NEXUS_PASSWORD" | docker login localhost:8082 -u "$NEXUS_USER" --password-stdin
            docker tag demo-ci:${BUILD_NUMBER} localhost:8082/demo-ci:${BUILD_NUMBER}
            docker push localhost:8082/demo-ci:${BUILD_NUMBER}
            docker logout localhost:8082
          '''
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
