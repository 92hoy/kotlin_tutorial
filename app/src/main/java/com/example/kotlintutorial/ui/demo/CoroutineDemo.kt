package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * 일정 시간 기다린 뒤 값을 돌려주는 가짜 네트워크 호출.
 *
 * Kotlin 포인트: `suspend` 함수는 스레드를 막지 않고 중단되었다가 이어진다.
 * 콜백 없이 위에서 아래로 읽히는 동기 코드처럼 쓴다.
 *
 * @param millis 기다릴 시간(밀리초).
 * @return 기다린 시간을 그대로 돌려준 값.
 */
suspend fun fakeFetch(millis: Long): Long {
    delay(millis)
    return millis
}

/**
 * 같은 작업 두 개를 순차 실행할 때와 `async`로 병렬 실행할 때 걸린 시간을 잰다.
 *
 * Kotlin 포인트: `async`는 `Deferred`를 돌려주고 `await()`로 결과를 받는다.
 * Java의 `CompletableFuture` 체이닝보다 일반 변수 대입에 가깝게 읽힘.
 *
 * @return 순차 시간과 병렬 시간(밀리초) 쌍.
 */
suspend fun measureSequentialAndParallel(): Pair<Long, Long> {
    val sequential = measureTimeMillis {
        fakeFetch(500)
        fakeFetch(500)
    }
    val parallel = measureTimeMillis {
        coroutineScope {
            val a = async { fakeFetch(500) }
            val b = async { fakeFetch(500) }
            a.await() + b.await()
        }
    }
    return sequential to parallel
}

/**
 * 구조적 동시성 예시. 자식 코루틴 하나가 실패하면 같은 스코프의 형제도 취소되고
 * 예외가 `coroutineScope` 바깥으로 전파된다.
 *
 * Kotlin 포인트: 코루틴은 부모-자식 관계로 묶여서, 스코프가 끝나기 전에 자식이 모두 끝나거나 취소됨.
 * 직접 스레드를 관리하는 Java 방식에서 생기는 "고아 작업"이 남지 않는다.
 *
 * @return 결과 설명 문자열.
 */
suspend fun structuredConcurrencyResult(): String {
    var siblingCancelled = false
    return try {
        coroutineScope {
            launch {
                try {
                    delay(1000)
                } finally {
                    siblingCancelled = true
                }
            }
            launch {
                delay(100)
                throw IllegalStateException("자식 실패")
            }
        }
        "예외 없음"
    } catch (e: IllegalStateException) {
        "예외 전파됨: ${e.message} / 형제 취소=$siblingCancelled"
    }
}

/**
 * 코루틴 데모 화면. 병렬 실행, 구조적 동시성, 취소를 버튼으로 하나씩 실행해 로그를 확인한다.
 *
 * Kotlin 포인트: `rememberCoroutineScope`로 얻은 스코프는 화면이 사라질 때 같이 취소된다.
 */
@Composable
fun CoroutineDemo() {
    val scope = rememberCoroutineScope()
    val logs = remember { mutableStateListOf<String>() }
    var ticker by remember { mutableStateOf<Job?>(null) }

    DemoScaffold("코루틴") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                scope.launch {
                    logs += "순차/병렬 측정 시작"
                    val (seq, par) = measureSequentialAndParallel()
                    logs += "순차 ${seq}ms, 병렬 ${par}ms"
                }
            }) { Text("async/await") }
            Button(onClick = {
                scope.launch { logs += structuredConcurrencyResult() }
            }) { Text("구조적 동시성") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                enabled = ticker?.isActive != true,
                onClick = {
                    ticker = scope.launch {
                        try {
                            repeat(10) { i ->
                                logs += "tick $i"
                                delay(300)
                            }
                        } finally {
                            // 취소되어도 finally는 실행된다.
                            logs += "정리 완료"
                        }
                    }
                },
            ) { Text("반복 작업 시작") }
            Button(onClick = { ticker?.cancel() }) { Text("취소") }
        }
        logs.forEach { Text(it, style = MaterialTheme.typography.bodyMedium) }
    }
}
