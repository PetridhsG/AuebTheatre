# 🎭 AUEB Theatre – Mobile Ticket Booking App

AUEB Theatre is a mobile application that allows users to interact with a theater's ticket office using natural language conversations in English or Greek. The app offers both a chatbot interface and traditional button-based menus to accommodate all types of users.

## 📱 Application Overview

The app is designed to:

- Provide information about current plays, showtimes, and availability
- Allow users to book or cancel tickets
- Enable communication with a theater representative
- Support both natural language chat and menu-based navigation

## 🎥 Walkthrough Video

[![Watch the video](https://img.youtube.com/vi/mE2d8fXswAs/0.jpg)](https://www.youtube.com/watch?v=mE2d8fXswAs)

## 🌟 Features

- 🔍 **Information Retrieval** – Learn about shows, theaters, and available seats.
- 🎫 **Ticket Booking** – Reserve seats for afternoon or evening performances.
- ❌ **Ticket Cancellation** – Cancel existing reservations with confirmation.
- 💬 **Multilingual Chatbot** – Interact in Greek or English using a natural language assistant.
- 🧭 **Dual Interaction Modes** – Use either chatbot or guided menus.
- ☎️ **Representative Contact** – Contact theater staff when chatbot support is insufficient.
- 🌓 **Dark/Light Theme** – Automatic theme switching based on system preferences.
- 🌐 **Language Auto-Detection** – Adapts to device language settings.

## 🛠 Technologies Used

- **Platform:** Android  
- **Language:** Kotlin  
- **UI Toolkit:** Jetpack Compose + Material 3  
- **Chatbot:** Llama-4-maverick:free via OpenRouter LLM API  
- **Prototyping:** Figma  
- **IDE:** Android Studio  

## ⚠️ Important: Local Configuration Required

> To successfully build and run the application, you **must** create a `local.properties` file in the root directory of the project and add your API credentials for the chatbot service.

```properties
API_KEY=your_openrouter_api_key
BASE_URL=https://openrouter.ai/api/v1/
MODEL=meta-llama/llama-4-maverick:free
TEMPERATURE=0.0
