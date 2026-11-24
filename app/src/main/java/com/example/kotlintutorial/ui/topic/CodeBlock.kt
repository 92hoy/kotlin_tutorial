package com.example.kotlintutorial.ui.topic

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 색을 입힐 키워드 목록. Kotlin과 Java 예시 코드에서 자주 나오는 것만 모았다. */
private val KEYWORDS = setOf(
    "val", "var", "fun", "class", "interface", "object", "data", "sealed", "enum", "annotation",
    "when", "if", "else", "for", "while", "return", "is", "in", "by", "override", "operator",
    "suspend", "inline", "reified", "out", "import", "private", "public", "static", "final",
    "void", "new", "extends", "implements", "true", "false", "null", "this", "try", "finally",
    "catch", "throw",
)

/** 코드에서 주석, 문자열, 단어(어노테이션 포함)를 찾아내는 정규식. */
private val TOKEN = Regex("//.*|\"(?:\\\\.|[^\"\\\\])*\"|@?[A-Za-z_][A-Za-z0-9_]*")

/**
 * 코드 문자열에서 주석, 문자열, 키워드에 색을 입힌 [AnnotatedString]을 만든다.
 * 정규식으로 토큰만 구분하는 간단한 방식이라 문법 검사는 하지 않는다.
 *
 * Kotlin 포인트: `buildAnnotatedString { }`도 수신 객체 람다 빌더이고,
 * 블록 안에서 `append`와 `withStyle`을 바로 호출한다.
 *
 * @param code 색을 입힐 코드.
 * @param keyword 키워드와 어노테이션 색.
 * @param string 문자열 리터럴 색.
 * @param comment 주석 색.
 */
fun highlightCode(code: String, keyword: Color, string: Color, comment: Color): AnnotatedString =
    buildAnnotatedString {
        var last = 0
        for (match in TOKEN.findAll(code)) {
            append(code.substring(last, match.range.first))
            val token = match.value
            val color = when {
                token.startsWith("//") -> comment
                token.startsWith("\"") -> string
                token.startsWith("@") || token in KEYWORDS -> keyword
                else -> null
            }
            if (color == null) {
                append(token)
            } else {
                withStyle(SpanStyle(color = color)) { append(token) }
            }
            last = match.range.last + 1
        }
        append(code.substring(last))
    }

/**
 * 예시 코드를 보여 주는 블록. 고정폭 글꼴이고 가로 스크롤과 길게 눌러 복사를 지원한다.
 *
 * @param code 보여 줄 코드.
 * @param modifier 바깥 모양 설정.
 * @param highlight false면 색 강조 없이 그대로 보여 준다(디렉토리 트리 같은 텍스트용).
 */
@Composable
fun CodeBlock(code: String, modifier: Modifier = Modifier, highlight: Boolean = true) {
    val keywordColor = MaterialTheme.colorScheme.primary
    val stringColor = MaterialTheme.colorScheme.tertiary
    val commentColor = MaterialTheme.colorScheme.outline
    val text = remember(code, highlight, keywordColor, stringColor, commentColor) {
        if (highlight) {
            highlightCode(code, keywordColor, stringColor, commentColor)
        } else {
            AnnotatedString(code)
        }
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.fillMaxWidth(),
    ) {
        SelectionContainer {
            Text(
                text = text,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                softWrap = false,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(12.dp),
            )
        }
    }
}
