# Where in the World

An Android geography game built with **Kotlin** and **Jetpack Compose**. Players explore a Street View panorama, ask for hints, drop a pin on the map, and score points based on how close their guess is to the real location.

This repository is also a sandbox for modern Android tooling and patterns, including Compose, Navigation 3, Koin, Firebase, Paparazzi screenshot tests, and custom startup wiring.

## Gameplay

The current app flow is:

1. Start from the welcome screen
2. Choose a difficulty
3. Explore the location in Street View
4. Optionally request a hint
5. Drop a guess pin on the map
6. Review the final score on the game over screen

Current game modes exposed in the app:

- **Solo**
- **Quick Challenge**
- **Challenge Friends**

Current difficulties:

- **Easy**
- **Medium**
- **Hard**
- **Extreme**

## Tech stack

- **Kotlin**
- **Jetpack Compose**
- **Navigation 3**
- **Koin** for DI
- **Firebase** Analytics, Crashlytics, Performance, and Remote Config
- **Google Maps Compose**
- **Google Play Games Services**
- **Paparazzi** for screenshot regression tests

## Prerequisites

You will need:

- Android Studio with a recent Android SDK installed
- A Firebase project
- A Google Maps API key
- A Google Play Games Services configuration
- A `google-services.json` file for the app package

## Local configuration

The app reads local secrets from `local.properties` and also supports environment variables with the same names.

Create or update `local.properties` with:

```properties
mapId=
gameServicesProjectId=
googleMapsApiKey=

# Needed for signed release builds
storeFile=
storePassword=
keyAlias=
keyPassword=
```

Place **`google-services.json`** in the app module, and make sure the values in `local.properties` point at your real Firebase, Maps, and Play Games configuration.

## Remote Config

Gameplay content is loaded from Firebase Remote Config. Default values are bundled in:

- `app/src/main/res/xml/remote_config_defaults.xml`

Those defaults currently provide the fallback landmark/location data for each difficulty, so the app can still start even when remote fetch does not return newer values.

## Build commands

Common Gradle commands:

```bash
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew testDebugUnitTest
```

## Screenshot tests

UI changes should be validated with Paparazzi.

Use:

```bash
./gradlew verifyPaparazziDebug
```

If your work intentionally changes the UI, update the recorded screenshots with:

```bash
./gradlew recordPaparazziDebug
```

## Recommended validation workflow

For most code changes:

```bash
./gradlew testDebugUnitTest assembleDebug
```

For UI changes:

```bash
./gradlew testDebugUnitTest verifyPaparazziDebug assembleDebug
```

## Public release checklist

Before shipping publicly, make sure you have covered at least:

1. **Release signing** is configured
2. **Release build** succeeds locally and in CI
3. **Firebase / Play Games / Maps** production configuration is in place
4. **Privacy policy** and Play Console data safety disclosures are ready
5. **Crash reporting and analytics** behavior matches your public release policy
6. **Paparazzi screenshot tests** and **unit tests** are passing

## License

This project is licensed under the terms of the repository [`LICENSE`](LICENSE) file.
