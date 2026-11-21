package com.example.kotlintutorial.data

import kotlinx.serialization.Serializable
import retrofit2.http.GET

/**
 * 명언 API의 응답 모델.
 *
 * Kotlin 포인트: `@Serializable`을 붙이면 kotlinx.serialization이 컴파일 시점에
 * 파서를 만들어 준다. 리플렉션을 쓰는 Gson과 달리 null 안전성도 지켜진다.
 *
 * @property quote 명언 본문.
 * @property author 작성자.
 */
@Serializable
data class Quote(
    val quote: String,
    val author: String,
)

/**
 * 공개 명언 API(dummyjson.com) Retrofit 인터페이스.
 * 별도 키 없이 호출할 수 있다.
 */
interface QuoteApi {

    /** 무작위 명언 한 건을 가져온다. `suspend`라서 코루틴에서 바로 호출한다. */
    @GET("quotes/random")
    suspend fun random(): Quote
}
