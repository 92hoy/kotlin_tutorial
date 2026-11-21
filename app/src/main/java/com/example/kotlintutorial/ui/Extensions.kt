package com.example.kotlintutorial.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * epoch millis 값을 읽기 좋은 날짜 문자열로 바꾼다.
 *
 * Kotlin 포인트: 확장 함수라서 `memo.createdAt.toDateString()`처럼
 * Long 타입에 원래 있던 메서드인 것처럼 호출한다.
 *
 * @param pattern 날짜 형식. 기본값은 `yyyy-MM-dd HH:mm`.
 */
fun Long.toDateString(pattern: String = "yyyy-MM-dd HH:mm"): String =
    SimpleDateFormat(pattern, Locale.KOREA).format(Date(this))

/**
 * 공백뿐인 문자열이면 null, 아니면 자기 자신을 돌려준다.
 * `?:` 엘비스 연산자와 함께 쓰면 입력값 정리가 한 줄로 끝난다.
 */
fun String.nullIfBlank(): String? = takeIf { it.isNotBlank() }
