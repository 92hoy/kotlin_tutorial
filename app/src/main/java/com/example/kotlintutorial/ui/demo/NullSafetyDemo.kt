package com.example.kotlintutorial.ui.demo

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * null 안전성 연산자 네 가지의 결과를 같은 입력으로 비교해 보여 주는 순수 함수 모음.
 *
 * Kotlin 포인트: `String`과 `String?`는 서로 다른 타입이라 null 가능성을 컴파일러가 검사한다.
 * Java는 실행해 보기 전까지 NullPointerException 여부를 알 수 없음.
 *
 * @param input 사용자가 입력한 값. null이면 "값 없음"을 뜻한다.
 */
fun nullSafetyResults(input: String?): List<Pair<String, String>> = listOf(
    "input" to input.toString(),
    // 안전 호출: null이면 이후 호출을 건너뛰고 null을 돌려준다.
    "input?.length" to input?.length.toString(),
    // 엘비스: null일 때 쓸 기본값을 지정한다.
    "input?.length ?: 0" to (input?.length ?: 0).toString(),
    // let: null이 아닐 때만 블록을 실행한다.
    "input?.let { \"[\$it]\" }" to (input?.let { "[$it]" } ?: "블록 실행 안 됨"),
    // !!: null이면 NullPointerException을 던진다. 예외를 잡아서 결과로 보여 줌.
    "input!!.length" to runCatching { input!!.length.toString() }
        .getOrElse { "예외: ${it::class.simpleName}" },
)

/**
 * null 안전성 데모 화면. 스위치로 입력을 null로 바꿔 가며 각 연산자의 결과를 비교한다.
 *
 * Kotlin 포인트: `?.`, `?:`, `let`, `!!`를 한 화면에서 같은 값으로 확인한다.
 */
@Composable
fun NullSafetyDemo() {
    var text by rememberSaveable { mutableStateOf("kotlin") }
    var isNull by rememberSaveable { mutableStateOf(false) }
    val input: String? = if (isNull) null else text

    DemoScaffold("null 안전성") {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { androidx.compose.material3.Text("입력값") },
            enabled = !isNull,
            modifier = Modifier.fillMaxWidth(),
        )
        androidx.compose.foundation.layout.Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            Switch(checked = isNull, onCheckedChange = { isNull = it })
            Text("  null로 만들기", style = MaterialTheme.typography.bodyLarge)
        }
        nullSafetyResults(input).forEach { (expr, result) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(expr, style = MaterialTheme.typography.labelMedium)
                    Text(result, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
