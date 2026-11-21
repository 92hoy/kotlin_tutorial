package com.example.kotlintutorial.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * 앱 전체에서 쓰는 Room 데이터베이스.
 * 테이블을 추가하면 [entities]에 등록하고 [version]을 올려야 한다.
 */
@Database(entities = [Memo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /** 메모 DAO를 돌려준다. 구현체는 Room이 자동 생성한다. */
    abstract fun memoDao(): MemoDao
}
