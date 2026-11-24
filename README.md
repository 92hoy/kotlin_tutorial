# Kotlin Tutorial

Java로만 안드로이드 앱을 만들어 오다가, Kotlin도 제대로 써 보려고 시작한 연습 프로젝트임.
Kotlin을 대표하는 기능을 하나씩 직접 앱에 넣어 보고, 설명과 예시 코드까지 앱 안에서 바로 볼 수 있게 정리하는 게 목표.

## 앱 화면 구성
홈 화면 제목은 **Kotlin Tutorial**. 기능을 난이도별로 묶어 두었고, 누르면 아래 순서로 들어간다.

1. 홈: 초급 / 중급 / 고급 / 종합 예제 목록 + 앱 구조 카드
2. 기능 상세: 설명, 핵심 포인트, Kotlin 예시 코드, Java로 쓰면
3. 직접 해보기: 상세 화면 하단 버튼으로 실습 화면 실행

| 난이도 | 기능 |
|---|---|
| 초급 | null 안전성, data class, 컬렉션 연산, 확장 함수, 스코프 함수 |
| 중급 | sealed 상태, 위임, 코루틴, 연산자 오버로딩 |
| 고급 | Flow 검색, 제네릭 / inline / reified, 타입 안전 빌더 DSL |
| 종합 예제 | 메모 앱 (Room, Retrofit, Flow, Hilt) |

홈 맨 위 "앱 구조 한눈에 보기"에서는 레이어 구조, 적용한 패턴, 디렉토리 위치를 탭으로 볼 수 있음.

## 사용한 기술
- Kotlin, Jetpack Compose (Material 3), Navigation Compose
- Coroutines, Flow, StateFlow
- Room, Hilt (둘 다 KSP)
- Retrofit, kotlinx.serialization
- ViewModel + Repository 구조 (MVVM)

## 아키텍처
위에서 아래로만 의존하고, 아래 레이어는 위 레이어를 모름.

```
UI (Compose)  ──이벤트──▶  ViewModel (StateFlow)  ──suspend──▶  Repository  ──▶  Room / Retrofit
      ◀──────상태─────────         ◀──────Flow, Result──────         ◀──────────
                       DI: Hilt가 모든 레이어에 객체를 주입
```

- 읽기: Room 변경 → Flow → Repository → StateFlow → 화면 갱신
- 쓰기: 버튼 → ViewModel → `viewModelScope.launch` → Repository → DAO

### 적용한 패턴
| 패턴 | 위치 |
|---|---|
| MVVM | `ui/MemoViewModel.kt`, `ui/MemoScreen.kt` |
| Repository | `data/MemoRepository.kt` |
| 의존성 주입 | `di/AppModule.kt`, `@HiltViewModel`, `@Inject` |
| 싱글톤 | `@Singleton`, `object AppModule` |
| 옵저버 | `Flow`, `StateFlow`, `collectAsStateWithLifecycle` |
| 단방향 데이터 흐름 | ViewModel 상태 → 화면, 화면 이벤트 → ViewModel |
| 상태 패턴 (유사) | `ui/UiState.kt` |
| 빌더 | `ui/demo/DslDemo.kt`, `Retrofit.Builder`, `Json { }` |
| 위임 | `ui/demo/DelegationDemo.kt` |

## 디렉토리 구조
```
app/src/main/java/com/example/kotlintutorial
├─ MainActivity.kt         앱 진입점, NavHost 실행
├─ MemoApp.kt              Hilt 시작점
├─ data/                   Room, Retrofit, Repository
├─ di/                     Hilt 모듈
└─ ui/
   ├─ MemoScreen.kt, MemoViewModel.kt, UiState.kt, Extensions.kt
   ├─ home/                튜토리얼 홈 (난이도별 목록)
   ├─ topic/               기능 상세 화면, 코드 블록
   ├─ tutorial/            난이도와 기능 목록, 설명 데이터
   ├─ architecture/        앱 구조 설명 화면
   ├─ navigation/          화면 이동 (NavHost)
   └─ demo/                기능별 실습 화면
```

## Kotlin 기능을 쓴 위치
| 기능 | 파일 |
|---|---|
| data class, null 안전성 | `data/Memo.kt` |
| suspend, Flow | `data/MemoDao.kt`, `ui/MemoViewModel.kt` |
| sealed interface + when | `ui/UiState.kt`, `ui/MemoScreen.kt` |
| 확장 함수 | `ui/Extensions.kt` |
| runCatching, Result.fold | `data/MemoRepository.kt`, `ui/MemoViewModel.kt` |
| enum 프로퍼티, `Level.entries`, 함수 타입 프로퍼티 | `ui/tutorial/Topic.kt`, `ui/tutorial/Topics.kt` |
| `groupBy`, `orEmpty`, 확장 함수 `Level.containerColor()` | `ui/home/TutorialHomeScreen.kt`, `ui/topic/TopicScreen.kt` |
| 정규식 + `buildAnnotatedString` 빌더 | `ui/topic/CodeBlock.kt` |
| null 안전 연산자 (`?.`, `?:`, `!!`, `let`) | `ui/demo/NullSafetyDemo.kt` |
| data class `copy`, 구조 분해 | `ui/demo/DataClassDemo.kt` |
| sealed interface, data object, 스마트 캐스트 | `ui/demo/SealedStateDemo.kt` |
| 확장 함수, 확장 프로퍼티, null 수신 타입 | `ui/demo/ExtensionFunctions.kt`, `ui/demo/ExtensionDemo.kt` |
| 스코프 함수 (`let`, `run`, `with`, `apply`, `also`) | `ui/demo/ScopeFunctionDemo.kt` |
| 컬렉션 연산 (`map`, `filter`, `groupBy`, 시퀀스) | `ui/demo/CollectionDemo.kt` |
| 코루틴 (`async`/`await`, 구조적 동시성, 취소) | `ui/demo/CoroutineDemo.kt` |
| Flow 검색 (`StateFlow`, `debounce`, `combine`, `stateIn`) | `ui/demo/FlowSearchDemo.kt` |
| 위임 (`by lazy`, `observable`, 클래스 위임) | `ui/demo/DelegationDemo.kt` |
| 타입 안전 빌더 DSL (`@DslMarker`, 수신 객체 람다) | `ui/demo/DslDemo.kt` |
| 제네릭, `inline`, `reified` | `ui/demo/GenericDemo.kt` |
| 연산자 오버로딩 (`operator fun`, `compareTo`) | `ui/demo/OperatorDemo.kt` |
| 람다 DSL (`Json { }`) | `di/AppModule.kt` |

모든 클래스와 함수에 KDoc 주석을 달아서, 코드만 읽어도 역할을 알 수 있게 함.

## 실행 방법
1. Android Studio 최신 안정 버전으로 이 저장소를 연다.
2. Gradle Sync가 끝나면 에뮬레이터나 실기기(Android 8.0 이상)에서 `app`을 실행한다.
3. 메모 앱의 명언은 `dummyjson.com`에서 가져오므로 인터넷 연결 필요.

## 새 기능 추가하는 법
1. `ui/demo/`에 실습 화면을 만든다.
2. `ui/tutorial/Topics.kt`의 `topics`에 `Topic` 하나를 등록한다. (난이도, 설명, 예시 코드 포함)
3. 홈 목록과 상세 화면, 경로는 자동으로 반영됨.

## Java와 비교해서 느낀 점
- 모델 클래스가 `data class` 한 줄로 끝남. getter, setter, equals를 직접 쓸 필요 없음.
- 콜백 대신 `suspend` 함수를 쓰면 비동기 코드가 위에서 아래로 읽힘.
- XML 레이아웃과 `findViewById` 대신, 상태가 바뀌면 화면이 다시 그려지는 선언형 UI(Compose)로 바뀜.
- null 체크는 `?.`, `?:`, `let`으로 짧게 쓸 수 있음.

## 진행 상황
ROADMAP.md의 마일스톤 1~4 항목을 모두 구현함. 체크리스트는 [ROADMAP.md](ROADMAP.md) 참고.
