package com.example.kotlintutorial.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlintutorial.ui.architecture.ArchitectureScreen
import com.example.kotlintutorial.ui.home.TutorialHomeScreen
import com.example.kotlintutorial.ui.topic.TopicScreen
import com.example.kotlintutorial.ui.tutorial.Topic
import com.example.kotlintutorial.ui.tutorial.topics

/** 홈 화면의 Navigation 목적지 이름. */
private const val ROUTE_HOME = "home"

/** 앱 구조 화면의 Navigation 목적지 이름. */
private const val ROUTE_ARCHITECTURE = "architecture"

/** 기능 상세 화면의 경로 형식. `{id}` 자리에 [Topic.id]가 들어간다. */
private const val ROUTE_TOPIC = "topic/{id}"

/** 실습 화면의 경로 형식. `{id}` 자리에 [Topic.id]가 들어간다. */
private const val ROUTE_DEMO = "demo/{id}"

/**
 * 앱 전체의 화면 이동을 담당한다.
 * 홈 → 기능 상세 → 실습 화면 순서로 들어가고, 홈에서 앱 구조 화면으로도 갈 수 있다.
 *
 * Kotlin 포인트: `NavHost { }`의 마지막 인자가 람다라서 DSL처럼 읽힌다.
 * 기능마다 목적지를 등록하지 않고 `{id}` 인자 하나로 [topics] 전체를 처리하므로
 * 기능이 늘어도 이 코드는 그대로임.
 */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    /** 경로 인자로 받은 id에 해당하는 기능을 찾는다. 없으면 null. */
    fun findTopic(id: String?): Topic? = topics.firstOrNull { it.id == id }

    NavHost(navController = navController, startDestination = ROUTE_HOME) {
        composable(ROUTE_HOME) {
            TutorialHomeScreen(
                topics = topics,
                onOpenTopic = { topic -> navController.navigate("topic/" + topic.id) },
                onOpenArchitecture = { navController.navigate(ROUTE_ARCHITECTURE) },
            )
        }
        composable(ROUTE_ARCHITECTURE) {
            ArchitectureScreen(onBack = { navController.popBackStack() })
        }
        composable(ROUTE_TOPIC) { entry ->
            findTopic(entry.arguments?.getString("id"))?.let { topic ->
                TopicScreen(
                    topic = topic,
                    onBack = { navController.popBackStack() },
                    onRunDemo = { navController.navigate("demo/" + topic.id) },
                )
            }
        }
        composable(ROUTE_DEMO) { entry ->
            findTopic(entry.arguments?.getString("id"))?.let { topic -> topic.demo() }
        }
    }
}
