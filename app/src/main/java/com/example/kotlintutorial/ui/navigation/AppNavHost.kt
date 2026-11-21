package com.example.kotlintutorial.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlintutorial.ui.home.CatalogHomeScreen

/** 홈 화면의 Navigation 목적지 이름. */
private const val ROUTE_HOME = "home"

/**
 * 앱 전체의 화면 이동을 담당한다.
 * 시작 화면은 카탈로그 홈이고, [catalog]에 등록된 데모는 반복문으로 한 번에 등록한다.
 *
 * Kotlin 포인트: `NavHost { }`의 마지막 인자가 람다라서 DSL처럼 읽히고,
 * `forEach`로 목적지를 등록하기 때문에 데모가 늘어도 이 코드는 그대로임.
 */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ROUTE_HOME) {
        composable(ROUTE_HOME) {
            CatalogHomeScreen(
                entries = catalog,
                onOpen = { entry -> navController.navigate(entry.route) },
            )
        }
        catalog.forEach { entry ->
            composable(entry.route) { entry.screen() }
        }
    }
}
