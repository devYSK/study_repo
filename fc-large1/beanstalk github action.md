

```yaml
name: Java CI with Gradle

on:
  workflow_dispatch:

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'

      - name: Build with Gradle
        uses: gradle/gradle-build-action@67421db6bd0bf253fb4bd25b31ebb98943c375e1
        with:
          arguments: clean build bootJar

      - name: Get current time
        uses: 1466587594/get-current-time@v2
        id: current-time
        with:
          format: YYYYMMDDTHHmm
          utcOffset: "+09:00"

      - name: Generate deployment package
        run: |
          mkdir -p deployment
          cp build/libs/hello-github-action-0.0.1-SNAPSHOT.jar deployment/hello-github-action-0.0.1-SNAPSHOT.jar
          # cp deployment/Procfile deployment/Procfile
          cd deployment && zip -r hello-github-action-${{ steps.current-time.outputs.formattedTime }} .
          ls

      - name: Deploy Hello Github Action to EB
        uses: einaregilsson/beanstalk-deploy@v14
        with:
          aws_access_key: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws_secret_key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          application_name: hello-simple-api-beanstalk
          environment_name: Hellosimpleapibeanstalk-env
          version_label: hello-github-action-${{ steps.current-time.outputs.formattedTime }}
          region: ap-northeast-2
          deployment_package: deployment/hello-github-action-${{ steps.current-time.outputs.formattedTime }}.zip

```

