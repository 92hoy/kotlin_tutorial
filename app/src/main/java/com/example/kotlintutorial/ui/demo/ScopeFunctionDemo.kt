package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 스코프 함수 비교에 쓰는 설정 객체.
 *
 * @property name 서버 이름.
 * @property port 포트 번호.
 * @property debug 디버그 모드 여부.
 */
class ServerConfig {
    var name: String = ""
    var port: Int = 0
    var debug: Boolean = false

    /** 값을 한 줄로 보여 준다. */
    override fun toString(): String = "ServerConfig(name=$name, port=$port, debug=$debug)"
}

/**
 * 스코프 함수 한 개의 비교 정보.
 *
 * @property name 함수 이름.
 * @property receiver 블록 안에서 객체를 가리키는 방법(`it` 또는 `this`).
 * @property returns 함수가 돌려주는 값.
 * @property code 예시 코드.
 * @property result 실행 결과.
 */
data class ScopeResult(
    val name: String,
    val receiver: String,
    val returns: String,
    val code: String,
    val result: String,
)

/**
 * 같은 객체에 다섯 가지 스코프 함수를 적용한 결과를 만든다.
 *
 * Kotlin 포인트: 다섯 함수는 "객체를 `it`으로 받나 `this`로 받나", "무엇을 반환하나" 두 축으로만 다르다.
 * Java에는 대응하는 문법이 없어서 임시 변수와 setter 호출을 줄줄이 써야 함.
 *
 * @param nullableName null일 수 있는 입력. `let`의 null 처리 예시에 쓴다.
 * @return 함수별 비교 결과 목록.
 */
fun scopeResults(nullableName: String?): List<ScopeResult> {
    val config = ServerConfig().apply { name = "dev"; port = 8080 }
    val logs = mutableListOf<String>()
    val created = ServerConfig().also { logs += "생성 직후 port=${it.port}" }
        .apply { port = 9090 }

    return listOf(
        ScopeResult(
            "let", "it", "람다 결과",
            "name?.let { it.length }",
            nullableName?.let { it.length }.toString(),
        ),
        ScopeResult(
            "run", "this", "람다 결과",
            "config.run { \"\$name:\$port\" }",
            config.run { "$name:$port" },
        ),
        ScopeResult(
            "with", "this", "람다 결과",
            "with(config) { port + 1 }",
            with(config) { port + 1 }.toString(),
        ),
        ScopeResult(
            "apply", "this", "객체 자신",
            "ServerConfig().apply { name = \"dev\"; port = 8080 }",
            config.toString(),
        ),
        ScopeResult(
            "also", "it", "객체 자신",
            "ServerConfig().also { log(it) }.apply { port = 9090 }",
            "$created / 로그=$logs",
        ),
    )
}

/**
 * 스코프 함수 비교 화면. 함수마다 수신 방식, 반환값, 예시 코드와 결과를 카드로 보여 준다.
 *
 * Kotlin 포인트: 객체 초기화는 `apply`, 부수 효과(로그 등)는 `also`,
 * null 처리와 변환은 `let`, 결과 계산은 `run`/`with`로 나눠 쓰는 게 관례.
 */
@Composable
fun ScopeFunctionDemo() {
    DemoScaffold("스코프 함수") {
        scopeResults("kotlin").forEach { r ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "${r.name}  (${r.receiver}, 반환: ${r.returns})",
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(r.code, style = MaterialTheme.typography.labelMedium)
                    Text("-> ${r.result}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
