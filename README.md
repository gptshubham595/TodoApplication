# Todo Application

## Retrofit + RoomDB+RealmDB+EncryptedSharedPref + Hilt + Flow + MulitModular + Clean Arch + MVVM

<img width="1495" alt="Screenshot 2024-04-07 at 3 36 03 PM" src="https://github.com/gptshubham595/TodoApplication/assets/24877361/8458a7d6-12a0-49d2-b9d1-840423a83f33">


## Benchmarking 
- https://medium.com/@gptshubham595/boosting-your-android-apps-performance-a-guide-to-macrobenchmarking-baseline-profiles-and-61969eff910c

## Ktlint
- https://gptshubham595.medium.com/how-do-you-add-ktlint-to-a-multimodular-android-project-4a77083750e3

## Github Actions
```YML
# Workflow name
name: Release App

# When it will be triggered
on:
  push:
    branches:
      - main

# Where it will run
jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v3

      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          distribution: 'zulu'
          java-version: '17'
          cache: 'gradle'

      - name: Setup Android SDK
        uses: android-actions/setup-android@v2

      - name: Cache Gradle dependencies
        uses: actions/cache@v2
        with:
          path: ~/.gradle/caches
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties', '**/buildSrc/**/*.kt')}}) }}
          restore-keys: |
            $C{{ runner.os }}-gradle-

      - name: Make Gradle Executable
        run: chmod +x ./gradlew

      - name: Decode Keystore
        env:
          ENCODED_STRING: ${{ secrets.KEYSTORE_BASE64 }}
          RELEASE_KEYSTORE_PASSWORD: ${{ secrets.RELEASE_KEYSTORE_PASSWORD }}
          RELEASE_KEYSTORE_ALIAS: ${{ secrets.RELEASE_KEYSTORE_ALIAS }}
          RELEASE_KEY_PASSWORD: ${{ secrets.RELEASE_KEY_PASSWORD }}

        run: |
          echo $ENCODED_STRING > keystore-b64.txt
          base64 -d keystore-b64.txt > keystore.jks

      - name: Generate App Version Name
        id: version
        run: echo "VERSION_NAME=$(git describe --tags | sed 's/\(.*\)-/\1./' | sed 's/\(.*\)-/\1+/')" >> $GITHUB_ENV

      - name: Bump Version
        uses: chkfung/android-version-actions@v1.1
        with:
          gradlePath: app/build.gradle.kts
          versionCode: ${{ github.run_number }}
          versionName: ${{ steps.version.outputs.VERSION_NAME }}

      - name: Build with Gradle
        run: ./gradlew build

      - name: Build Release apk
        run: ./gradlew assembleRelease --stacktrace

      - name: Build Release bundle
        run: ./gradlew bundleRelease --stacktrace

      - name: Get release file aab path
        id: releaseAab
        run: echo "aabfile=$(find app/build/outputs/bundle/release/*.aab)" >> $GITHUB_OUTPUT

      - name: Get release file apk path
        id: releaseApk
        run: echo "apkfile=$(find app/build/outputs/apk/release/*.apk)" >> $GITHUB_OUTPUT

      - name: Zip Files
        uses: papeloto/action-zip@v1
        with:
          files: ${{ steps.releaseAab.outputs.aabfile }} ${{ steps.releaseApk.outputs.apkfile }}
          dest: ${{ steps.releaseApk.outputs.apkfile }}.zip

      - name: Get Zip File
        id: zipFile
        run: echo "zipfile=$(find app/build/outputs/apk/release/*.zip)" >> $GITHUB_OUTPUT

      # Upload Artifact Build
      # Noted For Output [main_project_module]/build/outputs/apk/debug/
      - name: Upload APK Release
        uses: actions/upload-artifact@v3
        with:
          name: APK(s) generated
          path: ${{ steps.releaseApk.outputs.apkfile }}

      # Noted For Output [main_project_module]/build/outputs/apk/debug

      - name: Upload Reports
        uses: actions/upload-artifact@v3
        with:
          name: Test-Reports
          path: app/build/reports
          if: always()


```

## Testing - Junit + mockitto + roboelectric + google truth
