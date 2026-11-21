package com.example.kotlintutorial.ui.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn

/**
 * 검색어와 분류를 Flow로 묶어 결과 목록을 만드는 클래스.
 *
 * Kotlin 포인트: 입력은 `MutableStateFlow`, 결과는 읽기 전용 `StateFlow`로 노출한다.
 * `debounce`로 타이핑이 멈춘 뒤에만 검색하고, `combine`으로 두 입력의 최신 값을 합친다.
 * Java의 RxJava나 LiveData 변환보다 짧게 쓰고, 코루틴 안에서 바로 이어 쓸 수 있음.
 *
 * @param scope 결과 Flow를 유지할 코루틴 스코프.
 * @param source 검색 대상 목록.
 */
@OptIn(FlowPreview::class)
class ProductSearch(scope: CoroutineScope, private val source: List<Product>) {

    /** 현재 입력 중인 검색어. 화면의 텍스트 필드와 연결된다. */
    val query = MutableStateFlow("")

    /** 선택한 분류. null이면 전체. */
    val category = MutableStateFlow<String?>(null)

    /** 검색어(300ms 디바운스)와 분류를 합쳐 필터링한 결과. */
    val results: StateFlow<List<Product>> =
        combine(query.debounce(300), category) { q, c ->
            source.filter { p ->
                p.name.contains(q.trim()) && (c == null || p.category == c)
            }
        }.stateIn(scope, SharingStarted.WhileSubscribed(5_000), source)
}

/**
 * Flow 검색 데모 화면. 검색어를 입력하면 입력이 멈춘 뒤 결과가 갱신되고,
 * 분류 칩을 누르면 `combine`이 즉시 다시 계산한다.
 */
@Composable
fun FlowSearchDemo() {
    val scope = rememberCoroutineScope()
    val search = remember { ProductSearch(scope, sampleProducts) }
    val query by search.query.collectAsState()
    val category by search.category.collectAsState()
    val results by search.results.collectAsState()

    DemoScaffold("Flow 검색") {
        OutlinedTextField(
            value = query,
            onValueChange = { search.query.value = it },
            label = { Text("상품 이름") },
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val categories = sampleProducts.map { it.category }.distinct()
            categories.forEach { c ->
                FilterChip(
                    selected = category == c,
                    onClick = { search.category.value = if (category == c) null else c },
                    label = { Text(c) },
                )
            }
        }
        Text("결과 ${results.size}개 (입력 후 300ms 뒤 반영)", style = MaterialTheme.typography.labelMedium)
        results.forEach { Text("${it.name} / ${it.category} / ${it.price.toWon()}") }
    }
}
