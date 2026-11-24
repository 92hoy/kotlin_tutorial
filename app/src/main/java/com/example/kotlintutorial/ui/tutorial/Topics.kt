package com.example.kotlintutorial.ui.tutorial

import com.example.kotlintutorial.ui.MemoScreen
import com.example.kotlintutorial.ui.demo.CollectionDemo
import com.example.kotlintutorial.ui.demo.CoroutineDemo
import com.example.kotlintutorial.ui.demo.DataClassDemo
import com.example.kotlintutorial.ui.demo.DelegationDemo
import com.example.kotlintutorial.ui.demo.DslDemo
import com.example.kotlintutorial.ui.demo.ExtensionDemo
import com.example.kotlintutorial.ui.demo.FlowSearchDemo
import com.example.kotlintutorial.ui.demo.GenericDemo
import com.example.kotlintutorial.ui.demo.NullSafetyDemo
import com.example.kotlintutorial.ui.demo.OperatorDemo
import com.example.kotlintutorial.ui.demo.ScopeFunctionDemo
import com.example.kotlintutorial.ui.demo.SealedStateDemo

/**
 * 튜토리얼에 나오는 모든 기능 목록. 홈 화면은 [Topic.level]로 묶어서 보여 준다.
 * 새 기능을 추가하려면 `ui/demo/`에 실습 화면을 만들고 여기에 [Topic] 하나를 등록하면 된다.
 *
 * 예시 코드는 읽기 쉽게 문자열 템플릿 대신 `+`로 이어 붙여서 적었다.
 */
val topics: List<Topic> = listOf(
    // ---------------------------------------------------------------- 초급
    Topic(
        id = "null-safety",
        title = "null 안전성",
        level = Level.BEGINNER,
        summary = "null 가능 타입과 `?.`, `?:`, `let`, `!!`",
        explanation = "Kotlin은 null이 들어갈 수 있는 타입(String?)과 들어갈 수 없는 타입(String)을 구분한다. " +
            "컴파일러가 null 가능성을 검사해서 NullPointerException을 실행 전에 걸러 줌. " +
            "null일 수 있는 값은 안전 호출, 엘비스 연산자, let으로 다룬다.",
        points = listOf(
            "`String`은 null 불가, `String?`은 null 가능",
            "`?.` 안전 호출: null이면 뒤쪽 호출을 건너뛰고 null을 돌려줌",
            "`?:` 엘비스: null일 때 쓸 기본값을 지정",
            "`!!`는 null이면 예외를 던지므로 꼭 필요할 때만 사용",
        ),
        kotlinCode = """
            fun main() {
                val name: String? = null

                println(name?.length)                  // null
                println(name?.length ?: 0)             // 0
                name?.let { println(it.uppercase()) }  // null이면 실행 안 됨

                val text: String = "kotlin"            // null을 넣으면 컴파일 에러
                println(text.length)                   // 6
            }
        """.trimIndent(),
        javaCode = """
            String name = null;
            if (name != null) {
                System.out.println(name.length());
            }
            // 검사를 빼먹으면 실행 중에 NullPointerException
        """.trimIndent(),
        demo = { NullSafetyDemo() },
    ),
    Topic(
        id = "data-class",
        title = "data class",
        level = Level.BEGINNER,
        summary = "`copy`, 구조 분해, `==`와 `===` 비교",
        explanation = "data class는 값을 담는 클래스다. equals, hashCode, toString, copy, 구조 분해용 함수를 " +
            "컴파일러가 만들어 줘서 Java에서 getter와 setter를 포함해 수십 줄 쓰던 코드가 한 줄로 끝남.",
        points = listOf(
            "주 생성자의 프로퍼티만 equals와 toString에 쓰임",
            "`copy`로 일부만 바꾼 새 객체를 만든다 (원본은 그대로)",
            "`val (a, b) = obj`처럼 구조 분해 선언 가능",
            "`==`는 값 비교, `===`는 같은 객체인지 참조 비교",
        ),
        kotlinCode = """
            data class User(val id: Long, val name: String, val age: Int = 20)

            fun main() {
                val a = User(1, "홍길동")
                val b = a.copy(name = "김철수")    // 일부만 바꿔서 복사

                println(a)                         // User(id=1, name=홍길동, age=20)
                println(a == a.copy())             // true  (값 비교)
                println(a === a.copy())            // false (참조 비교)

                val (id, name) = b                 // 구조 분해
                println(id)                        // 1
                println(name)                      // 김철수
            }
        """.trimIndent(),
        javaCode = """
            public class User {
                private final long id;
                private final String name;
                private final int age;
                // 생성자, getter, equals, hashCode, toString ...
                // 필드가 늘 때마다 같이 고쳐야 함
            }
        """.trimIndent(),
        demo = { DataClassDemo() },
    ),
    Topic(
        id = "collection",
        title = "컬렉션 연산",
        level = Level.BEGINNER,
        summary = "`map`, `filter`, `groupBy`, `partition`과 시퀀스",
        explanation = "컬렉션 확장 함수로 반복문 없이 변환, 걸러내기, 묶기를 한 줄에 표현한다. " +
            "읽기 전용(List)과 변경 가능(MutableList)이 타입으로 나뉘어 있음. " +
            "데이터가 많으면 asSequence()로 지연 평가해서 중간 리스트를 만들지 않는다.",
        points = listOf(
            "`map`은 변환, `filter`는 걸러내기, `groupBy`는 키로 묶기",
            "`partition`은 조건으로 두 리스트로 나눔",
            "`List`는 읽기 전용, 수정하려면 `MutableList`",
            "시퀀스는 최종 연산을 호출할 때 요소를 하나씩 처리",
        ),
        kotlinCode = """
            val nums = listOf(1, 2, 3, 4, 5, 6)

            val squares = nums.map { it * it }              // [1, 4, 9, 16, 25, 36]
            val evens = nums.filter { it % 2 == 0 }         // [2, 4, 6]
            val byParity = nums.groupBy { if (it % 2 == 0) "짝수" else "홀수" }
            val (small, big) = nums.partition { it < 4 }    // [1, 2, 3] / [4, 5, 6]

            // 시퀀스: 필요한 만큼만 계산한다
            val firstTwo = nums.asSequence()
                .map { it * 10 }
                .filter { it > 20 }
                .take(2)
                .toList()                                   // [30, 40]
        """.trimIndent(),
        javaCode = """
            List<Integer> squares = new ArrayList<>();
            for (int n : nums) {
                squares.add(n * n);
            }
            // 또는 nums.stream().map(n -> n * n).collect(Collectors.toList())
        """.trimIndent(),
        demo = { CollectionDemo() },
    ),
    Topic(
        id = "extension",
        title = "확장 함수",
        level = Level.BEGINNER,
        summary = "String, Int, List에 덧붙인 확장 함수와 확장 프로퍼티",
        explanation = "기존 클래스를 고치지 않고 함수나 프로퍼티를 덧붙인 것처럼 쓰는 기능이다. " +
            "실제로는 정적 함수이고 호출 모양만 멤버처럼 보임. " +
            "Java에서 유틸 클래스에 모아 두던 함수를 자연스럽게 호출할 수 있다.",
        points = listOf(
            "`fun 타입.이름()` 형태로 선언, 함수 안의 `this`가 수신 객체",
            "확장 프로퍼티는 `val 타입.이름: 타입 get() = ...`",
            "클래스의 private 멤버에는 접근할 수 없음",
            "같은 이름의 멤버 함수가 있으면 멤버가 우선",
        ),
        kotlinCode = """
            fun String.isEmail(): Boolean = contains("@") && contains(".")

            val Int.isEven: Boolean get() = this % 2 == 0

            fun List<Int>.secondOrNull(): Int? = getOrNull(1)

            fun main() {
                println("a@b.com".isEmail())            // true
                println(4.isEven)                       // true
                println(listOf(5, 6, 7).secondOrNull()) // 6
            }
        """.trimIndent(),
        javaCode = """
            public final class StringUtils {
                public static boolean isEmail(String s) {
                    return s.contains("@") && s.contains(".");
                }
            }
            // 호출: StringUtils.isEmail(text)  (유틸 클래스를 따로 알아야 함)
        """.trimIndent(),
        demo = { ExtensionDemo() },
    ),
    Topic(
        id = "scope-function",
        title = "스코프 함수",
        level = Level.BEGINNER,
        summary = "`let`, `run`, `with`, `apply`, `also` 비교",
        explanation = "객체의 문맥 안에서 코드 블록을 실행하는 함수 다섯 가지다. " +
            "객체를 it로 받는지 this로 받는지, 결과로 무엇을 돌려주는지가 서로 다름. " +
            "초기화에는 apply, null 처리와 변환에는 let을 주로 쓴다.",
        points = listOf(
            "`let`: 객체가 `it`, 람다 결과를 반환 (null 처리, 변환)",
            "`run`, `with`: 객체가 `this`, 람다 결과를 반환",
            "`apply`: 객체가 `this`, 객체 자신을 반환 (초기화)",
            "`also`: 객체가 `it`, 객체 자신을 반환 (로그 같은 부가 작업)",
        ),
        kotlinCode = """
            data class Person(var name: String = "", var age: Int = 0)

            val p = Person().apply {            // this, 객체 반환 → 초기화
                name = "홍길동"
                age = 30
            }.also { println("생성: " + it) }   // it, 객체 반환 → 부가 작업

            val label = p.let { it.name + " (" + it.age + ")" }   // 결과 반환 → 변환
            val info = with(p) { name + " / " + age }              // this, 결과 반환
            val upper = p.run { name.uppercase() }                 // this, 결과 반환
        """.trimIndent(),
        javaCode = """
            Person p = new Person();
            p.setName("홍길동");
            p.setAge(30);
            // 변수 이름을 매번 반복해서 써야 함
        """.trimIndent(),
        demo = { ScopeFunctionDemo() },
    ),
    // ---------------------------------------------------------------- 중급
    Topic(
        id = "sealed-state",
        title = "sealed 상태",
        level = Level.INTERMEDIATE,
        summary = "`sealed interface`와 else 없는 `when`",
        explanation = "sealed 타입은 하위 타입의 종류가 컴파일 시점에 모두 정해진다. " +
            "when으로 분기할 때 하나라도 빠지면 컴파일 에러가 나서, 화면 상태처럼 닫힌 집합을 안전하게 다룰 수 있음. " +
            "이 앱의 UiState가 이 방식이다.",
        points = listOf(
            "`sealed interface` 또는 `sealed class`로 선언",
            "값이 없는 상태는 `data object`로 표현",
            "`when`에 else가 필요 없고, 새 상태를 추가하면 분기 누락이 컴파일 에러로 드러남",
            "`is Success` 확인 뒤에는 스마트 캐스트로 바로 `data`에 접근",
        ),
        kotlinCode = """
            sealed interface UiState<out T> {
                data object Loading : UiState<Nothing>
                data class Success<T>(val data: T) : UiState<T>
                data class Error(val message: String) : UiState<Nothing>
            }

            fun render(state: UiState<String>): String = when (state) {
                UiState.Loading -> "로딩 중"
                is UiState.Success -> "결과: " + state.data    // 스마트 캐스트
                is UiState.Error -> "오류: " + state.message
                // else 없음. 상태를 추가하면 여기서 컴파일 에러
            }
        """.trimIndent(),
        javaCode = """
            enum Status { LOADING, SUCCESS, ERROR }
            // 값(data, message)은 별도 필드로 두고 null 체크로 관리
            // 분기 누락을 컴파일러가 잡아 주지 못함
        """.trimIndent(),
        demo = { SealedStateDemo() },
    ),
    Topic(
        id = "delegation",
        title = "위임",
        level = Level.INTERMEDIATE,
        summary = "`by lazy`, `observable`, `vetoable`, 클래스 위임",
        explanation = "by 키워드로 프로퍼티의 getter와 setter, 또는 인터페이스 구현을 다른 객체에 맡긴다. " +
            "상속 대신 합성을 문법으로 지원하는 기능이다. " +
            "지연 초기화나 값 변경 감지 같은 반복 코드를 한 줄로 줄여 줌.",
        points = listOf(
            "`by lazy`: 처음 접근할 때 한 번만 초기화",
            "`Delegates.observable`: 값이 바뀔 때마다 콜백 실행",
            "`Delegates.vetoable`: 변경을 거부할 수 있음",
            "`class A(b: I) : I by b`: 인터페이스 구현을 b에 위임",
        ),
        kotlinCode = """
            import kotlin.properties.Delegates

            class Config {
                val heavy: String by lazy {
                    println("초기화")            // 처음 접근할 때 한 번만 실행
                    "값"
                }
                var level: Int by Delegates.observable(0) { _, old, new ->
                    println("level: " + old + " -> " + new)
                }
            }

            interface Sound { fun play(): String }
            class Bell : Sound { override fun play() = "딸랑" }

            class Door(sound: Sound) : Sound by sound   // play() 구현을 sound에 위임
        """.trimIndent(),
        javaCode = """
            private String heavy;
            String getHeavy() {
                if (heavy == null) heavy = compute();   // 동기화까지 직접 처리해야 함
                return heavy;
            }
            // 인터페이스 위임도 메서드를 하나씩 전달하는 코드를 써야 함
        """.trimIndent(),
        demo = { DelegationDemo() },
    ),
    Topic(
        id = "coroutine",
        title = "코루틴",
        level = Level.INTERMEDIATE,
        summary = "`async`/`await` 병렬 실행, 구조적 동시성, 취소",
        explanation = "코루틴은 스레드를 막지 않고 일시 중단(suspend)할 수 있는 가벼운 비동기 단위다. " +
            "콜백 없이 순차 코드처럼 비동기 작업을 쓴다. " +
            "구조적 동시성 덕분에 부모가 취소되면 자식도 함께 취소되어 누수를 막아 줌.",
        points = listOf(
            "`suspend` 함수는 코루틴 안에서만 호출",
            "`async`/`await`로 여러 작업을 동시에 시작하고 결과를 합침",
            "`viewModelScope` 같은 스코프가 코루틴의 수명을 관리",
            "`cancel()`은 협력적 취소라서 `delay` 같은 중단 지점에서 반영됨",
        ),
        kotlinCode = """
            suspend fun fetchA(): Int { delay(1000); return 1 }
            suspend fun fetchB(): Int { delay(1000); return 2 }

            suspend fun total(): Int = coroutineScope {
                val a = async { fetchA() }      // 동시에 시작
                val b = async { fetchB() }
                a.await() + b.await()           // 약 1초 (순서대로 부르면 2초)
            }

            fun main() = runBlocking {
                val job = launch {
                    try { delay(5000) } finally { println("정리") }
                }
                delay(100)
                job.cancel()                    // 취소하면 finally 실행
            }
        """.trimIndent(),
        javaCode = """
            fetchA(a -> fetchB(b -> {
                callback.onResult(a + b);       // 콜백이 중첩됨
            }));
            // 병렬 실행과 취소까지 직접 관리하려면 코드가 훨씬 길어짐
        """.trimIndent(),
        demo = { CoroutineDemo() },
    ),
    Topic(
        id = "operator",
        title = "연산자 오버로딩",
        level = Level.INTERMEDIATE,
        summary = "`operator fun`으로 `+`, `*`, `[]`, `in`, `<` 정의",
        explanation = "operator 키워드를 붙인 함수로 연산자의 동작을 직접 정의한다. " +
            "정해진 이름(plus, times, get, contains, compareTo)의 함수가 각 연산자에 대응됨. " +
            "좌표나 금액처럼 값을 다루는 클래스를 수식처럼 읽히게 만들 때 쓴다.",
        points = listOf(
            "`operator fun plus` → `a + b`",
            "`operator fun get` → `a[i]`",
            "`operator fun contains` → `x in a`",
            "`operator fun compareTo` → `<`, `>`, `<=`, `>=`",
        ),
        kotlinCode = """
            data class Point(val x: Int, val y: Int) {
                operator fun plus(o: Point) = Point(x + o.x, y + o.y)
                operator fun times(n: Int) = Point(x * n, y * n)
                operator fun get(i: Int) = if (i == 0) x else y
            }

            fun main() {
                val a = Point(1, 2)
                val b = Point(3, 4)
                println(a + b)    // Point(x=4, y=6)
                println(a * 3)    // Point(x=3, y=6)
                println(b[1])     // 4
            }
        """.trimIndent(),
        javaCode = """
            Point c = a.plus(b).times(3);
            // 연산자 오버로딩이 없어서 메서드 호출 체인으로만 표현 가능
        """.trimIndent(),
        demo = { OperatorDemo() },
    ),
    // ---------------------------------------------------------------- 고급
    Topic(
        id = "flow-search",
        title = "Flow 검색",
        level = Level.ADVANCED,
        summary = "`StateFlow`, `debounce`, `combine`으로 만든 검색",
        explanation = "Flow는 시간에 따라 여러 값을 내보내는 비동기 스트림이다. " +
            "StateFlow는 항상 최신 값을 가진 상태 홀더. " +
            "debounce, combine 같은 연산자로 검색어 입력처럼 연속된 이벤트를 선언적으로 처리한다.",
        points = listOf(
            "StateFlow는 초기값이 필요하고 최신 값만 유지",
            "`debounce`: 입력이 잠시 멈췄을 때만 값을 통과시킴",
            "`combine`: 여러 Flow의 최신 값을 합쳐서 새 값을 만듦",
            "`stateIn`으로 Flow를 StateFlow로 바꿔 화면에서 구독",
        ),
        kotlinCode = """
            @OptIn(FlowPreview::class)
            class SearchViewModel(items: List<String>) : ViewModel() {
                private val query = MutableStateFlow("")
                private val source = MutableStateFlow(items)

                val results: StateFlow<List<String>> =
                    combine(query.debounce(300), source) { q, list ->
                        list.filter { it.contains(q, ignoreCase = true) }
                    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), items)

                fun onQueryChange(q: String) { query.value = q }
            }
        """.trimIndent(),
        javaCode = """
            handler.removeCallbacks(task);
            handler.postDelayed(task, 300);     // debounce를 직접 구현
            // 여러 입력을 합치는 로직도 리스너로 따로 연결해야 함
        """.trimIndent(),
        demo = { FlowSearchDemo() },
    ),
    Topic(
        id = "generic",
        title = "제네릭, inline, reified",
        level = Level.ADVANCED,
        summary = "`reified`로 런타임 타입 확인, 상한 제약, `out` 변성",
        explanation = "제네릭은 타입을 매개변수로 받는 기능이다. " +
            "Java에서는 타입 소거로 런타임에 T가 지워지지만, Kotlin은 inline과 reified로 타입 정보를 남길 수 있음. " +
            "변성(out, in)도 사용하는 쪽이 아닌 선언 지점에 적는다.",
        points = listOf(
            "상한 제약: `T : Comparable<T>`",
            "`inline` 함수는 호출 지점에 본문이 복사됨",
            "`reified`는 inline 함수 안에서 `is T`, `T::class`를 가능하게 함",
            "`out T`는 꺼내기만(공변), `in T`는 넣기만(반공변)",
        ),
        kotlinCode = """
            fun <T : Comparable<T>> maxOf2(a: T, b: T): T = if (a > b) a else b

            inline fun <reified T> List<*>.firstOfType(): T? =
                firstOrNull { it is T } as T?     // T가 런타임에도 남아 있음

            interface Producer<out T> { fun get(): T }   // 공변

            fun main() {
                println(maxOf2(3, 7))                                 // 7
                println(listOf(1, "a", 2.0).firstOfType<String>())    // a
            }
        """.trimIndent(),
        javaCode = """
            static <T> T firstOfType(List<?> list, Class<T> type) {
                for (Object o : list) {
                    if (type.isInstance(o)) return type.cast(o);
                }
                return null;    // Class 객체를 인자로 직접 넘겨야 함
            }
        """.trimIndent(),
        demo = { GenericDemo() },
    ),
    Topic(
        id = "dsl",
        title = "타입 안전 빌더 DSL",
        level = Level.ADVANCED,
        summary = "수신 객체 람다와 `@DslMarker`로 만든 HTML 빌더",
        explanation = "수신 객체 람다(T.() -> Unit)와 확장 함수로 선언적인 빌더 문법을 만든다. " +
            "중첩 구조를 코드 모양 그대로 표현할 수 있고, @DslMarker로 바깥 블록의 멤버를 잘못 호출하는 실수를 막음. " +
            "Compose와 Gradle Kotlin 스크립트가 이 방식으로 만들어져 있다.",
        points = listOf(
            "람다 안에서 `this`가 빌더 객체",
            "마지막 인자가 람다면 괄호 밖으로 뺄 수 있음",
            "`@DslMarker`로 바깥 스코프 멤버 호출을 막음",
            "`apply(block)`으로 빌더를 만들고 설정을 적용",
        ),
        kotlinCode = """
            @DslMarker annotation class HtmlDsl

            @HtmlDsl
            class Ul {
                val items = mutableListOf<String>()
                fun li(text: String) { items += text }
            }

            fun ul(block: Ul.() -> Unit): String {
                val ul = Ul().apply(block)
                return "<ul>" + ul.items.joinToString("") { "<li>" + it + "</li>" } + "</ul>"
            }

            fun main() {
                println(ul {
                    li("사과")
                    li("배")
                })    // <ul><li>사과</li><li>배</li></ul>
            }
        """.trimIndent(),
        javaCode = """
            String html = new HtmlBuilder()
                .ul()
                .li("사과")
                .li("배")
                .end()
                .build();
            // 메서드 체이닝뿐이라 중첩 구조가 코드에 드러나지 않음
        """.trimIndent(),
        demo = { DslDemo() },
    ),
    // ---------------------------------------------------------------- 종합 예제
    Topic(
        id = "memo",
        title = "메모 앱",
        level = Level.PROJECT,
        summary = "Room, Retrofit, Flow, sealed 상태를 한 번에 쓴 기본 뼈대",
        explanation = "이 앱의 기본 뼈대인 메모 앱이다. Room에 메모를 저장하고, Retrofit으로 명언을 불러오고, " +
            "ViewModel의 StateFlow로 상태를 관리하며, Hilt로 객체를 주입한다. " +
            "구조와 패턴은 홈의 '앱 구조 한눈에 보기'에서 정리해 둠.",
        points = listOf(
            "Room의 `Flow` → ViewModel의 `StateFlow` → Compose 화면 순서로 데이터가 흐름",
            "명언은 `Result.fold`로 `UiState`(로딩, 성공, 실패)에 매핑",
            "Hilt가 Repository, DAO, API를 주입",
            "화면을 회전해도 ViewModel의 상태가 유지됨",
        ),
        kotlinCode = """
            @HiltViewModel
            class MemoViewModel @Inject constructor(
                private val repository: MemoRepository,
            ) : ViewModel() {

                val memos: StateFlow<List<Memo>> = repository.memos
                    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

                fun addMemo(title: String, content: String?) {
                    viewModelScope.launch { repository.add(title, content) }
                }
            }
        """.trimIndent(),
        javaCode = """
            public class MemoViewModel extends ViewModel {
                private final LiveData<List<Memo>> memos;

                public void addMemo(String title, String content) {
                    executor.execute(() -> repository.add(title, content));
                }
            }
            // Executor, LiveData, 콜백을 직접 조합해야 함
        """.trimIndent(),
        demo = { MemoScreen() },
    ),
)
