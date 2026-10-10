# NEXCUT AI — Android starter project

This is an initial Android UI prototype for the NEXCUT AI video-editing assistant.

## What currently works
- Modern dark interface with separate Auto Edit and Prompt Editor entry points.
- Android system video picker.
- Prompt input and local display of the requested edit.
- No music is added.

## Not implemented yet
This is **not yet a full AI editor**. It does not analyze video content, apply actual effects, trim/export videos, or call an AI model. Those require integration with an AI API or a suitable on-device model plus a video-processing engine. Never put a private API key directly in an Android app; a secure backend is recommended.

## Build an APK
The included GitHub Actions workflow builds a debug APK. Upload this project to a GitHub repository, then run **Actions → Build Android APK → Run workflow**. Download the `NEXCUT-AI-debug-apk` artifact after the workflow succeeds.

For a local build, install JDK 17, Android SDK platform 35 and Gradle 8.9, then run:
`gradle assembleDebug`

Application ID: `com.nexcut.ai`
Min Android: 8.0 (API 26)


## Custom app icon
This project now includes a custom NEXCUT AI launcher icon in purple/cyan on a dark background, including density-specific PNG assets and Android adaptive-icon resources. The icon is wired into `AndroidManifest.xml`. The app still needs to be built and tested; this icon update does not add AI video-processing functionality.
