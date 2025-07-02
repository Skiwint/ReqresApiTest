pipeline {
    agent any            // Агент: где выполнять pipeline (any – на любом доступном узле, в нашем случае на самом Jenkins)
    tools {
        maven "Maven 3.8.5"   // Указываем установленный Maven (имя должно совпадать с настроенным в Global Tools)
    }
    stages {
        stage('Build') {
            steps {
                echo 'Starting build...'
                // Сборка проекта (компиляция и упаковка без запуска тестов):
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                // Запускаем тесты Maven (TestNG + RestAssured):
                sh 'mvn clean test'
            }
            post {
                // Генерируем Allure-отчет независимо от статуса тестов:
                always {
                    allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                }
            }
        }
        // stage('Deploy') будет добавлен после настройки тестов и отчетов
    }
}