# Roadmap

Kotlin 대표 기능을 하나씩 데모 화면으로 만들어 가는 계획. 완료하면 `[x]`로 바꿈.

## 브랜치 규칙
- `main`: 항상 실행 가능한 안정 버전
- `develop`: 개발 통합
- `feature/kotlin-catalog`: 기능 작업 (기능 하나는 커밋 하나 이상으로 구분)

## 커밋 규칙
`feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`, `ci:` 접두어를 쓰고 메시지는 한국어로 짧게 씀.

## 체크리스트

### 마일스톤 1: 기본 구조
- [x] 메모 앱 기본 뼈대 (Room, Retrofit, Hilt, Compose)
- [x] 기능 카탈로그 홈 화면과 Navigation Compose
- [x] null 안전성 데모 (`?.`, `?:`, `!!`, `let`)
- [x] data class 데모 (`copy`, 구조 분해)
- [x] sealed 상태 데모 (`sealed` + `when`)

### 마일스톤 2: 함수형과 비동기
- [x] 확장 함수 모음과 단위 테스트
- [ ] 스코프 함수 비교 (`let`, `run`, `with`, `apply`, `also`)
- [ ] 컬렉션 연산 (`map`, `filter`, `groupBy`, 시퀀스)
- [ ] 코루틴 (`async`/`await`, 구조적 동시성, 취소)
- [ ] Flow 검색 (`StateFlow`, `debounce`, `combine`)

### 마일스톤 3: 고급 문법
- [ ] 위임 (`by lazy`, `observable`, 클래스 위임)
- [ ] 타입 안전 빌더 DSL
- [ ] 제네릭, `inline`, `reified`
- [ ] 연산자 오버로딩
- [ ] README 최종 정리
