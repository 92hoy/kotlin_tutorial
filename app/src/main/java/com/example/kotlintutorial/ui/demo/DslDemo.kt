package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** DSL 범위를 제한하는 마커. 안쪽 블록에서 바깥 빌더의 함수를 실수로 호출하지 못하게 한다. */
@DslMarker
annotation class HtmlDsl

/** 만들어진 HTML 한 덩어리. 태그 이름, 자식 목록, 글자를 가진다. */
@HtmlDsl
open class Tag(private val name: String) {
    private val children = mutableListOf<Tag>()
    var text: String = ""

    /** 자식 태그를 만들고 [init] 블록으로 채운 뒤 등록한다. */
    protected fun <T : Tag> add(child: T, init: T.() -> Unit): T {
        child.init()
        children += child
        return child
    }

    /** 태그를 문자열로 만든다. */
    fun render(indent: String = ""): String = buildString {
        append(indent).append("<").append(name).append(">").append(text)
        if (children.isNotEmpty()) {
            append("\n")
            children.forEach { append(it.render("$indent  ")).append("\n") }
            append(indent)
        }
        append("</").append(name).append(">")
    }
}

/** 목록 항목 태그. */
class Li : Tag("li") {
    /** 항목에 글자를 넣는다. */
    operator fun String.unaryPlus() { text = this }
}

/** 순서 없는 목록 태그. */
class Ul : Tag("ul") {
    /** 항목 하나를 추가한다. */
    fun li(init: Li.() -> Unit) = add(Li(), init)
}

/** 본문 전체를 감싸는 태그. */
class Body : Tag("body") {
    /** 제목을 추가한다. */
    fun h1(title: String) = add(Tag("h1")) { text = title }

    /** 목록을 추가한다. */
    fun ul(init: Ul.() -> Unit) = add(Ul(), init)
}

/**
 * HTML을 코드로 짓는 DSL의 시작점.
 *
 * Kotlin 포인트: 수신 객체 람다(`Body.() -> Unit`)와 `@DslMarker` 덕분에
 * 중첩된 블록이 선언형 문법처럼 읽힌다. Java는 빌더 체이닝(`.add().add()`)이나 XML이 필요함.
 *
 * @param init 본문을 채우는 블록.
 * @return 완성된 Body 태그.
 */
fun body(init: Body.() -> Unit): Body = Body().apply(init)

/** 빌더 DSL 데모 화면. DSL 코드와 만들어진 결과를 같이 보여 준다. */
@Composable
fun DslDemo() {
    val html = body {
        h1("Kotlin 기능")
        ul {
            li { +"확장 함수" }
            li { +"코루틴" }
            li { +"DSL" }
        }
    }.render()

    DemoScaffold("타입 안전 빌더 DSL") {
        listOf(
            "코드" to "body {\n  h1(\"Kotlin 기능\")\n  ul {\n    li { +\"확장 함수\" }\n    ...\n  }\n}",
            "결과" to html,
        ).forEach { (title, content) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(title, style = MaterialTheme.typography.labelMedium)
                    Text(content, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
