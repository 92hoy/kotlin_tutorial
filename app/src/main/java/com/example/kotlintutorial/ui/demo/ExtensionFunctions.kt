package com.example.kotlintutorial.ui.demo

import java.util.Locale

/** 이메일 형식 검사에 쓰는 정규식. 매번 만들지 않도록 최상위 상수로 둔다. */
private val EMAIL_REGEX = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")

/**
 * 이메일 형식인지 검사한다.
 *
 * Kotlin 포인트: 확장 함수라서 `"a@b.com".isEmail()`처럼 String의 메서드인 것처럼 쓴다.
 * Java는 `EmailUtils.isEmail(str)` 같은 정적 유틸 클래스를 따로 만들어야 함.
 */
fun String.isEmail(): Boolean = matches(EMAIL_REGEX)

/**
 * 앞뒤 [visible]글자만 남기고 가운데를 `*`로 가린다.
 * 길이가 너무 짧으면 전부 가린다.
 *
 * @param visible 앞과 뒤에서 각각 남길 글자 수.
 */
fun String.maskMiddle(visible: Int = 2): String =
    if (length <= visible * 2) "*".repeat(length)
    else take(visible) + "*".repeat(length - visible * 2) + takeLast(visible)

/**
 * 천 단위 쉼표를 붙인 원화 문자열로 바꾼다. 예: `12000.toWon()` -> `12,000원`.
 *
 * Kotlin 포인트: Int 같은 기본 타입에도 함수를 덧붙일 수 있다. Java는 기본 타입에 메서드를 못 붙임.
 */
fun Int.toWon(): String = String.format(Locale.KOREA, "%,d원", this)

/**
 * 단어 수. 연속된 공백은 하나로 보고 센다.
 *
 * Kotlin 포인트: 함수뿐 아니라 확장 프로퍼티(`val String.wordCount`)도 만들 수 있다.
 * 값을 저장하지 않고 getter만 가진다.
 */
val String.wordCount: Int
    get() = trim().split(Regex("\\s+")).count { it.isNotEmpty() }

/**
 * 두 번째 원소를 돌려준다. 없으면 null.
 *
 * Kotlin 포인트: 제네릭 확장 함수. 모든 `List<T>`에 붙고, 반환 타입 `T?`로 null 가능성을 드러낸다.
 */
fun <T> List<T>.secondOrNull(): T? = getOrNull(1)

/**
 * null이거나 공백뿐이면 `-`, 아니면 자기 자신을 돌려준다.
 *
 * Kotlin 포인트: 수신 타입을 `String?`로 두면 null인 값에도 `.orDash()`를 바로 호출할 수 있다.
 * Java라면 호출 전에 null 체크가 필요하거나 정적 메서드에 넘겨야 함.
 */
fun String?.orDash(): String = this?.takeIf { it.isNotBlank() } ?: "-"
