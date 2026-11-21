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
 * 목록에서 [T] 타입인 원소만 골라낸다.
 *
 * Kotlin 포인트: `inline`이라 호출 지점에 코드가 펼쳐지고, `reified` 덕분에 실행 중에도 T를 알 수 있다.
 * Java는 타입 소거 때문에 `Class<T>`를 인자로 따로 넘겨야 함.
 *
 * @return [T]로 캐스팅된 원소 목록.
 */
inline fun <reified T> List<Any?>.filterByType(): List<T> = filterIsInstance<T>()

/**
 * 값이 [T] 타입이면 설명을 돌려주고 아니면 null.
 *
 * Kotlin 포인트: `reified` 타입은 `is T` 검사에 바로 쓸 수 있다.
 */
inline fun <reified T> Any?.describeIf(): String? =
    if (this is T) "${T::class.simpleName} 타입: $this" else null

/**
 * 비교 가능한 값 중 큰 쪽을 돌려준다.
 *
 * Kotlin 포인트: `T : Comparable<T>`는 Java의 `<T extends Comparable<T>>`에 해당하는 상한 제약.
 */
fun <T : Comparable<T>> maxOf2(a: T, b: T): T = if (a >= b) a else b

/**
 * 읽기 전용 상자. `out`으로 선언해서 `Box<String>`을 `Box<Any>`로 쓸 수 있다.
 *
 * Kotlin 포인트: 선언 지점 변성(`out`). Java는 사용할 때마다 `? extends T`를 써야 함.
 *
 * @param T 담긴 값의 타입.
 * @property value 담긴 값.
 */
class Box<out T>(val value: T)

/** 제네릭, inline, reified 결과 목록을 만든다. */
fun genericResults(): List<Pair<String, String>> {
    val mixed: List<Any?> = listOf(1, "둘", 3.0, "넷", null, 5)
    val strBox: Box<String> = Box("문자열")
    val anyBox: Box<Any> = strBox
    return listOf(
        "mixed.filterByType<String>()" to mixed.filterByType<String>().toString(),
        "mixed.filterByType<Int>()" to mixed.filterByType<Int>().toString(),
        "3.0.describeIf<Double>()" to 3.0.describeIf<Double>().toString(),
        "\"a\".describeIf<Int>()" to "a".describeIf<Int>().toString(),
        "maxOf2(\"가\", \"나\")" to maxOf2("가", "나"),
        "maxOf2(3, 7)" to maxOf2(3, 7).toString(),
        "val anyBox: Box<Any> = Box<String>(...)" to anyBox.value.toString(),
    )
}

/** 제네릭 데모 화면. */
@Composable
fun GenericDemo() {
    DemoScaffold("제네릭, inline, reified") {
        genericResults().forEach { (code, result) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(code, style = MaterialTheme.typography.labelMedium)
                    Text(result, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
