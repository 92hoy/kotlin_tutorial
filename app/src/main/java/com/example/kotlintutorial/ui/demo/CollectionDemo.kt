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

/**
 * 컬렉션 연산 예시에 쓰는 상품 모델.
 *
 * @property name 상품 이름.
 * @property category 분류.
 * @property price 가격(원).
 */
data class Product(val name: String, val category: String, val price: Int)

/** 컬렉션 데모에서 공통으로 쓰는 샘플 데이터. */
val sampleProducts: List<Product> = listOf(
    Product("사과", "과일", 1500),
    Product("바나나", "과일", 1000),
    Product("우유", "유제품", 2500),
    Product("치즈", "유제품", 4000),
    Product("빵", "베이커리", 3500),
    Product("식빵", "베이커리", 3000),
)

/**
 * 컬렉션 연산별 코드와 결과를 만든다.
 *
 * Kotlin 포인트: 람다를 받는 고차 함수로 `for` 루프와 임시 리스트 없이 변환, 필터, 묶기를 한 줄에 쓴다.
 * 원본은 읽기 전용 `List`라서 연산마다 새 컬렉션이 만들어지고 원본은 바뀌지 않음.
 * Java의 Stream과 달리 `.stream()`, `.collect()` 호출이 필요 없다.
 *
 * @param products 대상 상품 목록.
 * @return 코드와 결과 쌍의 목록.
 */
fun collectionResults(products: List<Product>): List<Pair<String, String>> {
    val (cheap, expensive) = products.partition { it.price < 3000 }

    // 시퀀스는 원소 하나씩 모든 단계를 거치므로, 필요한 개수를 채우면 나머지는 계산하지 않는다.
    var listCalls = 0
    products.map { listCalls++; it.price }.filter { listCalls++; it > 2000 }.first()
    var seqCalls = 0
    products.asSequence().map { seqCalls++; it.price }.filter { seqCalls++; it > 2000 }.first()

    return listOf(
        "map { it.name }" to products.map { it.name }.toString(),
        "filter { it.price >= 3000 }.map { it.name }" to
            products.filter { it.price >= 3000 }.map { it.name }.toString(),
        "groupBy { it.category }.mapValues { 개수 }" to
            products.groupBy { it.category }.mapValues { (_, list) -> list.size }.toString(),
        "groupBy { category }.mapValues { sumOf(price) }" to
            products.groupBy { it.category }.mapValues { (_, list) -> list.sumOf { it.price } }.toString(),
        "partition { it.price < 3000 }" to "싼 것=${cheap.map { it.name }} / 비싼 것=${expensive.map { it.name }}",
        "associate { name to price }" to products.associate { it.name to it.price }.toString(),
        "maxByOrNull { it.price }?.name" to products.maxByOrNull { it.price }?.name.toString(),
        "List 체인의 람다 호출 횟수" to "$listCalls 번 (전체를 map한 뒤 filter)",
        "asSequence() 체인의 람다 호출 횟수" to "$seqCalls 번 (첫 결과가 나오면 중단)",
    )
}

/**
 * 컬렉션 연산 데모 화면. 샘플 상품 목록에 적용한 연산 결과를 카드로 나열한다.
 */
@Composable
fun CollectionDemo() {
    DemoScaffold("컬렉션 연산") {
        Text(
            "목록: " + sampleProducts.joinToString { "${it.name}(${it.price})" },
            style = MaterialTheme.typography.bodyMedium,
        )
        collectionResults(sampleProducts).forEach { (code, result) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(code, style = MaterialTheme.typography.labelMedium)
                    Text(result, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
