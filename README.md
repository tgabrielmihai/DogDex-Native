# DogDex Native

## Project Status

⚠️ Completed Academic Project

This project fulfills the academic requirements for the course and is considered functionally complete. However, some improvements related to API integration, data synchronization and overall stability could still be implemented in future versions.

## Description

DogDex Native is an Android application developed using Kotlin and Jetpack Compose, designed to help users explore dog breeds, access breed-specific information and build a personalized collection of favorite breeds.

The application provides an intuitive mobile interface for browsing dog breed data, viewing detailed information, managing favorites and exploring educational content related to different breeds. The project follows modern Android development principles and utilizes a structured architecture based on MVVM to ensure maintainability and scalability.

Data is retrieved from The Dog API and combined with local storage mechanisms to improve usability and provide a smoother user experience. Favorites and user preferences are stored locally, while breed information is fetched through REST API requests.

DogDex Native was developed as part of a mobile application development project and demonstrates the practical use of native Android technologies, modern UI design, local persistence and API integration.

---

## Features

- Dog breed browsing
- Breed details and information
- Favorites management
- Local data persistence
- Modern Jetpack Compose interface
- Navigation between screens
- MVVM architecture
- API integration for breed data
- Responsive mobile design

---

## Technologies Used

### Mobile Development

- Kotlin
- Jetpack Compose
- Android Studio

### Architecture

- MVVM
- Repository Pattern

### Data Management

- Room Database
- DataStore

### API

- The Dog API
- Retrofit

---

## Project Structure

```text
app/
  data/
    local/
    remote/
    repo/
  di/
  ui/
    breedlist/
    breeddetail/
    favorites/
    quiz/
    settings/
    splash/
  navigation/
```

---

## How to Run

1. Obtain an API key from:

```text
https://thedogapi.com
```

2. Open the project in Android Studio.

3. Configure the API key according to the project requirements.

4. Sync Gradle dependencies.

5. Run the application on an Android emulator or a physical Android device.

---

## Notes

This native version mirrors the functionality of the DogDex Hybrid application while using Android-specific technologies and architecture patterns.

Breed information is retrieved through The Dog API, while user preferences and favorite breeds are stored locally on the device.

The API integration fulfills the project requirements, although certain improvements related to data consistency and API handling could still be implemented in future versions.
