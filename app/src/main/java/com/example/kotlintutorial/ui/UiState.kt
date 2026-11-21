package com.example.kotlintutorial.ui

/**
 * 화면이 가질 수 있는 상태를 타입으로 표현한 것.
 *
 * Kotlin 포인트: `sealed interface`라서 가능한 하위 타입을 컴파일러가 전부 안다.
 * `when`으로 분기할 때 하나라도 빠뜨리면 컴파일 에러가 나므로 else가 필요 없다.
 *
 * @param T 성공했을 때 담기는 데이터 타입.
 */
sealed interface UiState<out T> {

    /** 불러오는 중. */
    data object Loading : UiState<Nothing>

    /** 성공. [data]에 결과가 담긴다. */
    data class Success<T>(val data: T) : UiState<T>

    /** 실패. [message]에 화면에 보여줄 이유가 담긴다. */
    data class Error(val message: String) : UiState<Nothing>
}
