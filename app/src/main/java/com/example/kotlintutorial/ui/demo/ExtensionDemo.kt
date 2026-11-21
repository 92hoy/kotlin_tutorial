package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 입력한 문자열에 확장 함수들을 적용한 결과 목록을 만든다.
 *
 * @param input 사용자가 입력한 문자열.
 * @return 호출 식과 결과 쌍의 목록.
 */
fun extensionResults(input: String): List<Pair<String, String>> = listOf(
    "input.isEmail()" to input.isEmail().toString(),
    "input.maskMiddle()" to input.maskMiddle(),
    "input.wordCount" to input.wordCount.toString(),
    "input.length.toWon()" to input.length.toWon(),
    "input.split(\" \").secondOrNull()" to input.split(" ").secondOrNull().orDash(),
    "(null as String?).orDash()" to (null as String?).orDash(),
)

/**
 * 확장 함수 데모 화면. 입력값이 바뀔 때마다 모든 확장 함수의 결과가 다시 계산된다.
 *
 * Kotlin 포인트: 클래스를 수정하거나 상속하지 않고 기존 타입에 기능을 덧붙임.
 * 실제로는 정적 함수로 컴파일되지만 호출 코드는 멤버 메서드처럼 읽힌다.
 */
@Composable
fun ExtensionDemo() {
    var input by remember { mutableStateOf("hello@kotlin.dev 반가워요") }

    DemoScaffold("확장 함수") {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("입력") },
            modifier = Modifier.fillMaxWidth(),
        )
        extensionResults(input).forEach { (title, result) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(title, style = MaterialTheme.typography.labelMedium)
                    Text(result, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
