# WhatsApp Reply Generator (Morocco Edition) 🇲🇦

A modern Android application designed for Moroccan freelancers, sellers, and small businesses to generate professional and natural WhatsApp replies using Google's **Gemini 2.5 Flash** AI.

Featuring the **"Nano Banana"** aesthetic theme.

## ✨ Features
- **Moroccan-Centric AI**: Specifically tuned to understand and respond in **Darija**, as well as French and English.
- **Smart Generation**: Generates 3 distinct reply options for every input:
  - **Short**: Quick and direct.
  - **Professional**: Formal and business-ready.
  - **Friendly**: Warm and approachable.
- **Context Awareness**: Input the received message, your goal, the desired tone, and additional context for perfect results.
- **One-Tap Copy**: Easily copy replies to your clipboard to paste directly into WhatsApp.
- **Nano Banana Theme**: A unique, modern UI with a custom splash screen and a vibrant yellow/green color palette.

## 🛠️ Tech Stack
- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **AI Engine**: [Google Gemini 2.5 Flash](https://ai.google.dev/gemini-api/docs/models#gemini-2.5-flash) (via Firebase Vertex AI)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Design System**: Material Design 3 (M3)

## 🚀 Getting Started
1. **Clone the repo**:
   ```bash
   git clone https://github.com/YOUR_USERNAME/Morocco-WhatsApp-Reply-Generator.git
   ```
2. **Setup Firebase**:
   - Create a project on the [Firebase Console](https://console.firebase.google.com/).
   - Add an Android App with package name `com.example.firstgeminiapp` and download the `google-services.json`.
   - Place `google-services.json` in the `app/` directory (**this file is gitignored – never commit it**).
   - Enable **Firebase AI (Gemini)** in the Firebase Console.
3. **Build & Run**: Open the project in Android Studio and run it on your device.

> **No API key is hardcoded in this project.** Firebase project credentials are managed through `google-services.json` which is excluded from git. The Firebase AI SDK authenticates using those project credentials — there is no separate Gemini API key to manage. See `local.properties.example` for a full setup guide.

## 🔒 Security

| File | Gitignored? | Notes |
|---|---|---|
| `google-services.json` | ✅ Yes | Contains Firebase API key — never commit |
| `local.properties` | ✅ Yes | Contains local SDK path — never commit |
| `*.jks` / `*.keystore` | ✅ Yes | Release signing keys — never commit |
| `.env` / `key.properties` | ✅ Yes | Any extra secrets — never commit |

- **No secrets are hardcoded** in Kotlin source files.
- **Gemini API calls** go through the Firebase AI SDK — Firebase project credentials are configured via `google-services.json`, not hardcoded in source code.
- To set up locally, copy `local.properties.example` → `local.properties` and follow the instructions inside.

## 📸 Screenshots
*(Screenshots will be added here later!)*

---
*Built with ❤️ for the Moroccan community.*
