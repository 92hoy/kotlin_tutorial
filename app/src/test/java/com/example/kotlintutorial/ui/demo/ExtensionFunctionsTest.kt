package com.example.kotlintutorial.ui.demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 확장 함수 모음 단위 테스트.
 *
 * Kotlin 포인트: 확장 함수는 순수 함수라서 안드로이드 기기 없이 JVM 테스트로 바로 검증된다.
 */
class ExtensionFunctionsTest {

    /** 정상 이메일과 잘못된 이메일을 구분하는지 확인. */
    @Test
    fun isEmail_형식을_구분한다() {
        assertTrue("a@b.com".isEmail())
        assertFalse("a@b".isEmail())
        assertFalse("hello".isEmail())
    }

    /** 가운데만 가리고, 짧으면 전부 가리는지 확인. */
    @Test
    fun maskMiddle_가운데를_가린다() {
        assertEquals("ab****gh", "abcdefgh".maskMiddle())
        assertEquals("***", "abc".maskMiddle())
    }

    /** 천 단위 쉼표와 원 표시 확인. */
    @Test
    fun toWon_쉼표를_붙인다() {
        assertEquals("12,000원", 12000.toWon())
    }

    /** 연속 공백과 빈 문자열 처리 확인. */
    @Test
    fun wordCount_공백을_정리해서_센다() {
        assertEquals(3, "a  b   c".wordCount)
        assertEquals(0, "   ".wordCount)
    }

    /** 원소가 부족하면 null인지 확인. */
    @Test
    fun secondOrNull_없으면_null() {
        assertEquals(2, listOf(1, 2, 3).secondOrNull())
        assertNull(listOf(1).secondOrNull())
    }

    /** null 수신 객체에도 호출되는지 확인. */
    @Test
    fun orDash_null과_공백은_대시() {
        assertEquals("-", (null as String?).orDash())
        assertEquals("-", " ".orDash())
        assertEquals("x", "x".orDash())
    }
}
