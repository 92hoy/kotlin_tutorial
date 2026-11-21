# Kotlin Tutorial

Java로만 안드로이드 앱을 만들어 오다가, Kotlin도 제대로 써 보려고 시작한 연습 프로젝트입니다.
Kotlin을 대표하는 기능을 하나씩 직접 앱에 넣어 보면서 정리하는 게 목표입니다.

## 지금 들어 있는 것
메모 앱이 기본 뼈대입니다. 여기에 기능 데모를 하나씩 붙여 가는 중입니다.

- 메모 추가, 완료 체크, 삭제 (Room에 저장)
- 상단에 오늘의 명언 표시, 새로고침 가능 (공개 API 호출)
- 명언은 로딩 / 성공 / 실패 상태를 나눠서 처리

## 사용한 기술
- Kotlin, Jetpack Compose (Material 3)
- Coroutines, Flow, StateFlow
- Room, Hilt (둘 다 KSP)
- Retrofit, kotlinx.serialization
- ViewModel + Repository 구조 (MVVM)

## Kotlin 기능을 쓴 위치
| 기능 | 파일 |
|---|---|
| data class, null 안전성 | `data/Memo.kt` |
| suspend, Flow | `data/MemoDao.kt`, `ui/MemoViewModel.kt` |
| sealed interface + when | `ui/UiState.kt`, `ui/MemoScreen.kt` |
| 확장 함수 | `ui/Extensions.kt` |
| runCatching, Result.fold | `data/MemoRepository.kt`, `ui/MemoViewModel.kt` |
| 람다 DSL (`Json { }`) | `di/AppModule.kt` |

모든 클래스와 함수에는 KDoc 주석을 달아서, 코드만 읽어도 무슨 역할인지 알 수 있게 했습니다.

## 실행 방법
1. Android Studio 최신 안정 버전으로 이 저장소를 엽니다.
2. Gradle Sync가 끝나면 에뮬레이터나 실기기(Android 8.0 이상)에서 `app`을 실행합니다.
3. 명언은 `dummyjson.com`에서 가져오므로 인터넷 연결이 필요합니다.

```
app/src/main/java/com/example/kotlintutorial
├─ data/   Room, Retrofit, Repository
├─ di/     Hilt 모듈
└─ ui/     Compose 화면, ViewModel, 확장 함수
```

## Java와 비교해서 느낀 점
- 모델 클래스가 `data class` 한 줄로 끝납니다. getter, setter, equals를 직접 쓰지 않아도 됩니다.
- 콜백 대신 `suspend` 함수를 쓰면 비동기 코드가 위에서 아래로 읽힙니다.
- XML 레이아웃과 `findViewById` 대신, 상태가 바뀌면 화면이 다시 그려지는 선언형 UI(Compose)로 바뀌었습니다.
- null 체크는 `?.`, `?:`, `let`으로 짧게 쓸 수 있습니다.

## 앞으로 할 일
진행 상황은 [ROADMAP.md](ROADMAP.md)에 체크리스트로 관리합니다.
