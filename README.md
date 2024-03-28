
# Movie App

영화 정보를 보여주는 샘플 App

## Development

### Language

- Kotlin

### Libraries

- AndroidX
  - Activity & Activity Compose
  - AppCompat
  - Core
  - Lifecycle & ViewModel Compose
  - Navigation
  - DataStore

- Kotlin Libraries (Coroutine, Serialization)
- Compose
  - Material3
  - Navigation

- Landscapist & Landscapist Coil
- Hilt
- Square (Retrofit, OkHttp)

### Test

- JUnit4
- Mockk
- Turbine
- Kotest

## Architecture

### Layer

<img width="248" alt="image" src="https://github.com/junghoon-luckyhuni/movie/assets/35446333/2681056c-d7e4-4b6a-880f-854f6ed80425">


### Module

![project dot](https://github.com/junghoon-luckyhuni/movie/assets/35446333/61637206-e63f-410a-9373-9bc2f6a4d569)

**Module Graph 생성 방법**

```
1. 그래프를 시각화하는 오픈소스 설치
- brew install graphviz

2. 그래프 생성 Gradle Task 실행
./gradlew projectDependencyGraph
```

### Mendable

<img width="1844" alt="image" src="https://github.com/junghoon-luckyhuni/movie/assets/35446333/1851ee4a-b72c-452d-a64d-6685c9ffc9da">

### ScreenShot

<img alt="image" src="https://github.com/junghoon-luckyhuni/movie/assets/35446333/533ad1a8-9841-4c0d-9173-151c2a73d7d7">
