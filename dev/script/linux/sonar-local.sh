export SONAR_TOKEN=$(cat ~/.sonar/sonar-global)

mvn clean verify sonar:sonar \
  -Dsonar.projectKey=fibonacci-fib-worker \
  -Dsonar.projectName='fibonacci-fib-worker' \
  -Dsonar.host.url=http://localhost:9000
