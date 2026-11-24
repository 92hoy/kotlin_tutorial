package com.example.kotlintutorial.ui.architecture

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlintutorial.ui.topic.CodeBlock

/** 앱 구조 화면의 탭 이름. 순서대로 [StructureTab], [PatternTab], [DirectoryTab]에 대응한다. */
private val TAB_TITLES = listOf("구조", "패턴", "디렉토리")

/**
 * 앱 구조 한눈에 보기 화면. 레이어 구조, 적용한 디자인 패턴, 디렉토리 위치를 탭으로 나눠 보여 준다.
 *
 * @param onBack 뒤로 가기 버튼을 눌렀을 때 호출된다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchitectureScreen(onBack: () -> Unit) {
    var tab by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("앱 구조 한눈에 보기") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = tab) {
                TAB_TITLES.forEachIndexed { index, title ->
                    Tab(selected = tab == index, onClick = { tab = index }, text = { Text(title) })
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                when (tab) {
                    0 -> StructureTab()
                    1 -> PatternTab()
                    else -> DirectoryTab()
                }
            }
        }
    }
}

/**
 * 레이어 구조 탭. UI에서 데이터 소스까지의 의존 방향과 읽기, 쓰기 흐름을 보여 준다.
 */
@Composable
private fun StructureTab() {
    Text("위에서 아래로만 의존한다. 아래 레이어는 위 레이어를 모름.", style = MaterialTheme.typography.bodyMedium)

    LayerCard(
        title = "UI · Jetpack Compose",
        body = "화면을 그리고 사용자 입력을 ViewModel에 전달한다.\nMemoScreen, TopicScreen, ArchitectureScreen",
        color = MaterialTheme.colorScheme.primaryContainer,
    )
    FlowArrow("↓ 이벤트 호출          ↑ 상태 전달(StateFlow)")
    LayerCard(
        title = "ViewModel · StateFlow",
        body = "화면 상태를 들고 있고 코루틴으로 데이터를 요청한다. 화면 회전에도 유지됨.\nMemoViewModel",
        color = MaterialTheme.colorScheme.secondaryContainer,
    )
    FlowArrow("↓ suspend 호출          ↑ Flow, Result")
    LayerCard(
        title = "Repository",
        body = "Room과 Retrofit 중 어디서 가져오는지 감춘다.\nMemoRepository",
        color = MaterialTheme.colorScheme.tertiaryContainer,
    )
    FlowArrow("↓ 조회·저장          ↑ 데이터")
    LayerCard(
        title = "데이터 소스 · Room / Retrofit",
        body = "내부 DB와 네트워크 API.\nMemoDao, AppDatabase, QuoteApi",
        color = MaterialTheme.colorScheme.surfaceVariant,
    )

    LayerCard(
        title = "DI · Hilt (모든 레이어에 걸침)",
        body = "객체를 직접 만들지 않고 AppModule이 만들어서 생성자에 넣어 준다.\nAppModule, @HiltViewModel, @Inject",
        color = MaterialTheme.colorScheme.errorContainer,
    )

    SectionLabel("읽기 흐름")
    CodeBlock(
        "Room 테이블 변경\n → Flow (MemoDao)\n → Repository.memos\n → StateFlow (ViewModel)\n → collectAsStateWithLifecycle\n → 화면 갱신",
        highlight = false,
    )
    SectionLabel("쓰기 흐름")
    CodeBlock(
        "버튼 클릭\n → ViewModel 함수 호출\n → viewModelScope.launch\n → Repository (suspend)\n → DAO insert, update, delete\n → 테이블 변경이 읽기 흐름으로 이어짐",
        highlight = false,
    )
}

/**
 * 레이어 하나를 나타내는 카드.
 *
 * @param title 레이어 이름.
 * @param body 역할과 해당 클래스 설명.
 * @param color 카드 배경색.
 */
@Composable
private fun LayerCard(title: String, body: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/**
 * 레이어 사이의 데이터 방향 표시.
 *
 * @param text 화살표와 설명.
 */
@Composable
private fun FlowArrow(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
    )
}

/**
 * 탭 안의 소제목.
 *
 * @param text 제목 글자.
 */
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp),
    )
}

/**
 * 디자인 패턴 한 개의 정보.
 *
 * @property name 패턴 이름.
 * @property where 적용된 파일이나 코드 위치.
 * @property description 이 앱에서 어떻게 쓰였는지.
 */
private data class PatternInfo(val name: String, val where: String, val description: String)

/** 이 앱에 적용된 패턴 목록. */
private val PATTERNS = listOf(
    PatternInfo(
        "MVVM",
        "ui/MemoViewModel.kt, ui/MemoScreen.kt",
        "화면(View)은 그리기만 하고 상태와 로직은 ViewModel이 가진다.",
    ),
    PatternInfo(
        "Repository",
        "data/MemoRepository.kt",
        "Room과 Retrofit을 한곳으로 감싸서 ViewModel이 데이터 출처를 모르게 한다.",
    ),
    PatternInfo(
        "의존성 주입 (DI)",
        "di/AppModule.kt, @HiltViewModel, @Inject",
        "객체를 직접 만들지 않고 Hilt가 만들어서 넣어 준다. 테스트 때 교체하기 쉬움.",
    ),
    PatternInfo(
        "싱글톤",
        "@Singleton, object AppModule",
        "DB, Retrofit, Repository를 앱 전체에서 하나만 만들어 공유한다. Kotlin은 object 한 줄로 가능.",
    ),
    PatternInfo(
        "옵저버",
        "Flow, StateFlow, collectAsStateWithLifecycle",
        "데이터나 상태가 바뀌면 구독 중인 화면이 자동으로 갱신된다.",
    ),
    PatternInfo(
        "단방향 데이터 흐름 (UDF)",
        "ViewModel → 화면(상태), 화면 → ViewModel(이벤트)",
        "상태는 아래로, 이벤트는 위로만 흐르게 해서 변경 지점을 한 곳으로 모은다.",
    ),
    PatternInfo(
        "상태 패턴 (유사)",
        "ui/UiState.kt",
        "로딩, 성공, 실패를 sealed 타입으로 나누고 when으로 빠짐없이 처리한다.",
    ),
    PatternInfo(
        "빌더",
        "ui/demo/DslDemo.kt, Retrofit.Builder, Json { }",
        "수신 객체 람다로 설정을 나열하는 DSL 형태의 빌더.",
    ),
    PatternInfo(
        "위임",
        "ui/demo/DelegationDemo.kt",
        "by lazy, observable, 클래스 위임으로 상속 대신 합성을 쓴다.",
    ),
)

/**
 * 디자인 패턴 탭. [PATTERNS]를 카드 목록으로 보여 준다.
 */
@Composable
private fun PatternTab() {
    Text("이 앱에서 실제로 쓰인 패턴과 위치.", style = MaterialTheme.typography.bodyMedium)
    PATTERNS.forEach { pattern ->
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(pattern.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(pattern.description, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = pattern.where,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

/** 디렉토리 트리 문자열. 파일을 추가하거나 옮기면 같이 고쳐야 한다. */
private const val DIRECTORY_TREE = """app/src/main/java/com/example/kotlintutorial
├─ MainActivity.kt         앱 진입점, NavHost 실행
├─ MemoApp.kt              Hilt 시작점 (Application)
├─ data/                   데이터 레이어
│  ├─ Memo.kt              Room 엔티티 (data class)
│  ├─ MemoDao.kt           DB 접근 (Flow, suspend)
│  ├─ AppDatabase.kt       Room 데이터베이스
│  ├─ QuoteApi.kt          Retrofit 인터페이스, 응답 모델
│  └─ MemoRepository.kt    데이터 출처 통합
├─ di/
│  └─ AppModule.kt         Hilt 모듈 (DB, Retrofit 제공)
└─ ui/                     UI 레이어
   ├─ MemoScreen.kt        메모 화면
   ├─ MemoViewModel.kt     메모 화면 상태
   ├─ UiState.kt           sealed 상태 타입
   ├─ Extensions.kt        확장 함수 모음
   ├─ home/                튜토리얼 홈 (난이도별 목록)
   ├─ topic/               기능 상세 화면, 코드 블록
   ├─ tutorial/            난이도와 기능 목록, 설명 데이터
   ├─ architecture/        앱 구조 설명 화면
   ├─ navigation/          화면 이동 (NavHost)
   └─ demo/                기능별 실습 화면

app/src/test/java/.../ui/demo
└─ ExtensionFunctionsTest.kt   확장 함수 단위 테스트"""

/**
 * 디렉토리 탭. 폴더 구조와 각 파일의 역할을 트리로 보여 준다.
 */
@Composable
private fun DirectoryTab() {
    Text("어느 폴더에 무엇이 있는지.", style = MaterialTheme.typography.bodyMedium)
    CodeBlock(DIRECTORY_TREE, highlight = false)
    SectionLabel("새 기능을 추가하려면")
    CodeBlock(
        "1. ui/demo/ 에 실습 화면 작성\n2. ui/tutorial/Topics.kt 에 Topic 한 개 등록\n3. 홈 목록과 상세 화면은 자동으로 반영됨",
        highlight = false,
    )
}
