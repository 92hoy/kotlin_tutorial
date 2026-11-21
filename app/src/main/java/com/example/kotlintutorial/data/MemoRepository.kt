package com.example.kotlintutorial.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 메모(Room)와 명언(Retrofit) 데이터 출처를 한곳에서 감싸는 저장소.
 * ViewModel은 데이터가 어디서 오는지 몰라도 되게 한다.
 */
@Singleton
class MemoRepository @Inject constructor(
    private val dao: MemoDao,
    private val api: QuoteApi,
) {
    /** 저장된 메모 목록. DB가 바뀌면 자동으로 새 값이 흐른다. */
    val memos: Flow<List<Memo>> = dao.observeAll()

    /**
     * 메모를 추가한다.
     *
     * @param title 제목.
     * @param content 내용. 없으면 null.
     */
    suspend fun add(title: String, content: String?) =
        dao.insert(Memo(title = title, content = content))

    /**
     * 완료 상태를 뒤집는다.
     * Kotlin 포인트: data class의 `copy`로 불변 객체를 새 값으로 바꿔 만든다.
     */
    suspend fun toggleDone(memo: Memo) = dao.update(memo.copy(done = !memo.done))

    /** 메모를 삭제한다. */
    suspend fun delete(memo: Memo) = dao.delete(memo)

    /**
     * 무작위 명언을 가져온다.
     * Kotlin 포인트: `runCatching`으로 예외를 `Result`에 담아, 호출하는 쪽에서 try/catch 없이 처리한다.
     */
    suspend fun fetchQuote(): Result<Quote> = runCatching { api.random() }
}
