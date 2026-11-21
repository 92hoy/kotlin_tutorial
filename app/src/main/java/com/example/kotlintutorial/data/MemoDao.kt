package com.example.kotlintutorial.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 메모 테이블 접근용 DAO.
 *
 * Kotlin 포인트: 조회는 `Flow`, 쓰기는 `suspend`로 만들어서
 * 콜백 없이 코루틴 안에서 순서대로 읽히게 했다.
 */
@Dao
interface MemoDao {

    /**
     * 모든 메모를 최신순으로 관찰한다.
     * 테이블이 바뀔 때마다 Flow가 새 목록을 자동으로 내보낸다.
     */
    @Query("SELECT * FROM memos ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Memo>>

    /** 메모를 새로 저장한다. */
    @Insert
    suspend fun insert(memo: Memo)

    /** 기존 메모를 id 기준으로 덮어쓴다. */
    @Update
    suspend fun update(memo: Memo)

    /** 메모를 삭제한다. */
    @Delete
    suspend fun delete(memo: Memo)
}
