package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
 * 로그인 진행 상태. 가능한 상태를 하위 타입으로 전부 나열한다.
 *
 * Kotlin 포인트: `sealed`라서 하위 타입이 같은 모듈 안으로 제한되고,
 * `when`이 모든 경우를 다뤘는지 컴파일러가 검사한다. Java의 enum은 상태마다 다른 데이터를 못 담음.
 */
sealed interface LoginState {
    /** 아직 시도하지 않은 상태. 데이터 없음이라 object로 선언. */
    data object Idle : LoginState

    /** 요청 중인 상태. */
    data object Loading : LoginState

    /**
     * 성공 상태.
     *
     * @property userName 로그인한 사용자 이름.
     */
    data class Success(val userName: String) : LoginState

    /**
     * 실패 상태.
     *
     * @property reason 실패 이유.
     * @property retryCount 지금까지 실패한 횟수.
     */
    data class Failure(val reason: String, val retryCount: Int) : LoginState
}

/**
 * 상태를 화면에 보여 줄 문장으로 바꾼다.
 *
 * Kotlin 포인트: `when`을 식으로 쓰고 else가 없다. 하위 타입을 하나 추가하면 여기서 컴파일 오류가 나서 누락을 막아 줌.
 * 스마트 캐스트 덕분에 `is Success` 분기에서 `state.userName`을 바로 쓴다.
 */
fun describe(state: LoginState): String = when (state) {
    LoginState.Idle -> "대기 중"
    LoginState.Loading -> "로그인 중..."
    is LoginState.Success -> "${state.userName}님 환영합니다"
    is LoginState.Failure -> "실패(${state.retryCount}회): ${state.reason}"
}

/**
 * sealed 상태 데모 화면. 버튼으로 상태를 바꾸고 `when` 분기 결과를 확인한다.
 */
@Composable
fun SealedStateDemo() {
    var state: LoginState by remember { mutableStateOf(LoginState.Idle) }

    DemoScaffold("sealed 상태") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { state = LoginState.Loading }) { Text("로딩") }
            Button(onClick = { state = LoginState.Success("호영") }) { Text("성공") }
            Button(onClick = {
                val count = (state as? LoginState.Failure)?.retryCount ?: 0
                state = LoginState.Failure("비밀번호 불일치", count + 1)
            }) { Text("실패") }
            Button(onClick = { state = LoginState.Idle }) { Text("초기화") }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (state == LoginState.Loading) CircularProgressIndicator()
                Text(describe(state), style = MaterialTheme.typography.titleMedium)
                Text(state.toString(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
