package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * data class 데모에서 쓰는 사용자 모델.
 *
 * Kotlin 포인트: 선언 한 줄로 `equals`, `hashCode`, `toString`, `copy`, `componentN`이 자동 생성된다.
 * Java는 getter, setter, equals 등을 직접 쓰거나 Lombok이 필요함.
 *
 * @property name 이름.
 * @property age 나이.
 * @property city 사는 곳.
 */
data class Person(val name: String, val age: Int, val city: String)

/**
 * data class 기능별 결과 문자열을 만든다.
 *
 * @param person 기준이 되는 객체.
 * @return 기능 이름과 결과 쌍의 목록.
 */
fun dataClassResults(person: Person): List<Pair<String, String>> {
    // copy: 일부 값만 바꾼 새 객체를 만든다. 원본은 그대로.
    val older = person.copy(age = person.age + 1)
    // 구조 분해: componentN 함수로 프로퍼티를 한 번에 꺼낸다.
    val (name, age, city) = person
    return listOf(
        "toString()" to person.toString(),
        "copy(age = age + 1)" to older.toString(),
        "val (name, age, city) = person" to "$name / $age / $city",
        "person == person.copy()" to (person == person.copy()).toString(),
        "person == older" to (person == older).toString(),
        "person === person.copy()" to (person === person.copy()).toString(),
    )
}

/**
 * data class 데모 화면. 버튼으로 `copy`를 눌러 값을 바꿔 가며 결과를 확인한다.
 *
 * Kotlin 포인트: `==`는 equals(값 비교), `===`는 참조 비교.
 * Java의 `==`(참조)와 `equals`가 Kotlin에서는 반대 직관으로 정리됨.
 */
@Composable
fun DataClassDemo() {
    var person by remember { mutableStateOf(Person("호영", 30, "서울")) }

    DemoScaffold("data class") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { person = person.copy(age = person.age + 1) }) { Text("나이 +1") }
            Button(onClick = { person = person.copy(city = if (person.city == "서울") "부산" else "서울") }) {
                Text("도시 변경")
            }
        }
        dataClassResults(person).forEach { (title, result) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(title, style = MaterialTheme.typography.labelMedium)
                    Text(result, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
