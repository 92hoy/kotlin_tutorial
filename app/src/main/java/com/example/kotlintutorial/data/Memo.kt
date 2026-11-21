package com.example.kotlintutorial.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 메모 한 건을 나타내는 Room 엔티티.
 *
 * Kotlin 포인트: `data class`로 선언하면 equals / hashCode / toString / copy가
 * 자동으로 만들어져서 Java처럼 getter, setter를 직접 쓸 필요가 없다.
 *
 * @property id 기본 키. 0이면 저장할 때 Room이 자동으로 번호를 붙인다.
 * @property title 메모 제목.
 * @property content 메모 내용. 입력하지 않을 수 있어서 nullable(`String?`)로 뒀다.
 * @property done 완료 여부.
 * @property createdAt 생성 시각(epoch millis).
 */
@Entity(tableName = "memos")
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String? = null,
    val done: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
