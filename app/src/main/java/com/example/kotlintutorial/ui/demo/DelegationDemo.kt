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
import kotlin.properties.Delegates

/** 소리를 내는 동작을 정의한 인터페이스. 클래스 위임 예시에 쓴다. */
interface Speaker {
    /** 소리를 문자열로 돌려준다. */
    fun speak(): String
}

/** 기본 구현체. 위임 대상이 된다. */
class BasicSpeaker(private val sound: String) : Speaker {
    override fun speak(): String = sound
}

/**
 * [Speaker]를 직접 구현하지 않고 [speaker]에 위임하는 클래스.
 *
 * Kotlin 포인트: `by speaker` 한 줄이면 인터페이스의 모든 메서드를 컴파일러가 전달 코드로 만들어 준다.
 * Java는 상속하거나 모든 메서드를 손으로 전달해야 함.
 *
 * @param speaker 실제 동작을 맡는 객체.
 */
class Robot(speaker: Speaker) : Speaker by speaker

/**
 * 위임 프로퍼티 세 가지를 실행해 결과를 만든다.
 *
 * Kotlin 포인트: `by lazy`는 처음 읽을 때 한 번만 계산하고, `observable`은 값이 바뀔 때마다 콜백을 부른다.
 * Java는 지연 초기화에 null 체크와 동기화를 직접 써야 하고, 변경 감지는 setter에 넣어야 함.
 *
 * @return 설명과 결과 쌍의 목록.
 */
fun delegationResults(): List<Pair<String, String>> {
    val log = mutableListOf<String>()

    val heavy: String by lazy { log += "lazy 계산 실행"; "계산된 값" }
    log += "lazy 선언 직후 (아직 계산 안 됨)"
    val first = heavy
    val second = heavy

    var level: Int by Delegates.observable(0) { _, old, new -> log += "level $old -> $new" }
    level = 1
    level = 5

    var evenOnly: Int by Delegates.vetoable(0) { _, _, new -> new % 2 == 0 }
    evenOnly = 4
    evenOnly = 7

    return listOf(
        "by lazy: 두 번 읽기" to "$first, $second",
        "observable 변경 로그" to log.toString(),
        "vetoable (짝수만 허용) 4 후 7 대입" to "결과 $evenOnly",
        "class Robot(s: Speaker) : Speaker by s" to Robot(BasicSpeaker("삐빅")).speak(),
    )
}

/** 위임 데모 화면. 결과를 카드로 나열한다. */
@Composable
fun DelegationDemo() {
    DemoScaffold("위임") {
        delegationResults().forEach { (code, result) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(code, style = MaterialTheme.typography.labelMedium)
                    Text(result, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
