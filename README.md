# ArtScope 🎨

A modern Android application that provides an elegant interface to explore the Art Institute of Chicago's vast collection of artworks. Built with modern Android development practices and MVVM architecture.

## Features ✨

- **Smart Search**: Instantly search through thousands of artworks with real-time results
- **Rich Artwork Details**: View comprehensive information about each artwork including:
  - High-resolution images
  - Artist information
  - Historical context
  - Physical dimensions
  - Creation date
- **Interactive Image Viewing**: Zoom and pan through high-quality artwork images
- **Offline Support**: View previously loaded artworks even without internet connection
- **Modern UI**: Material Design implementation with smooth animations and transitions

## Technical Stack 🛠️

- **Architecture**: MVVM (Model-View-ViewModel)
- **Language**: 100% Kotlin
- **Jetpack Components**:
  - ViewModel
  - LiveData
  - Coroutines for async operations
  - Material Design components
- **Network**: 
  - Retrofit for API communication
  - OkHttp for network interceptors
  - Gson for JSON parsing
- **Image Loading**: 
  - Picasso with custom configuration
  - Efficient caching strategy
  - Error handling with placeholders
- **UI Components**:
  - RecyclerView with DiffUtil
  - PhotoView for image zooming
  - Custom views for artwork details
- **Testing**:
  - JUnit for unit tests
  - Mockito for mocking
  - Espresso for UI tests

## Architecture 🏗️

The app follows Clean Architecture principles and is organized into the following packages:

```
com.example.artinstituteapp/
├── model/          # Data models and entities
├── network/        # API interfaces and network utilities
├── repository/     # Data operations and business logic
├── view/          # Activities, Fragments and Adapters
├── viewmodel/     # ViewModels for managing UI state
└── util/          # Extension functions and utilities
```

## Screenshots 📱

[Screenshots will be added here]

## Getting Started 🚀

1. Clone the repository
2. Open the project in Android Studio
3. Run the app on an emulator or physical device

## API Integration 🔌

The app integrates with the Art Institute of Chicago's public API:
- Base URL: https://api.artic.edu/api/v1/
- Documentation: https://api.artic.edu/docs/

## Testing Strategy 🧪

- **Unit Tests**: Cover repository and ViewModel logic
- **Integration Tests**: API response handling and data mapping
- **UI Tests**: User interaction flows and screen state verification

## Future Enhancements 🔮

- Custom font integration
- Enhanced animations and transitions
- Deep linking support
- Responsive tablet layout
- Favorite artworks feature
- Virtual gallery tours


