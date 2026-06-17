# DogDex — Native (Android / Kotlin + Jetpack Compose)

The native variant of DogDex. MVVM + repository, Hilt DI, offline favorites via Room,
settings via SharedPreferences.

## Run
1. Get a free key at https://thedogapi.com.
2. Open `local.properties` and set `DOG_API_KEY=yourkey`.
3. Open the `DogDex-Native` folder in Android Studio (it will use its bundled JDK), or
   build from the CLI:
   ```
   # JAVA_HOME must point to a JDK 17+ (Android Studio's jbr works):
   set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
   gradlew.bat assembleDebug          # build the APK
   gradlew.bat installDebug           # install on a running emulator/device
   ```
4. Pick an emulator (API 26+) in Android Studio and press ▶.

## Architecture
```
data/
  remote/   Retrofit DogApiService + DTOs (x-api-key interceptor in di/NetworkModule)
  local/    Room (FavoriteBreedEntity/Dao/AppDatabase) + SettingsPreferences
  repo/     BreedRepository (in-memory cache), FavoritesRepository, SettingsRepository
  model/    Breed domain model + mappers (incl. synthesized "About" text)
ui/
  splash/ breedlist/ breeddetail/ favorites/ quiz/ settings/   (each: Screen + ViewModel)
  components/  BreedCard, TagChip, headers, loading/error/empty states
  theme/       glassmorphism colors, DogDexTheme, GlassCard, LocalMetricUnits
  navigation/  routes, bottom bar, NavHost
di/            Hilt modules (NetworkModule, DatabaseModule)
```

## Notes
- The Dog API has no description field; the "About" paragraph is synthesized in
  `Breed.aboutText()` from breed group, origin, bred-for, temperament and life span.
- Offline: favorites live in Room; Coil disk-caches the images by URL so saved breeds
  render without a connection once viewed.
- Settings (Dark mode, Metric units) persist in SharedPreferences and re-theme / re-unit
  the whole app reactively via a Flow.
