package com.example.kotlintutorial.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kotlintutorial.data.Memo
import com.example.kotlintutorial.data.Quote

/**
 * 메모 앱의 메인 화면.
 * 상단 명언 카드, 메모 목록, 메모 추가 버튼으로 구성된다.
 *
 * @param viewModel Hilt가 주입해 주는 화면 상태 관리자.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoScreen(viewModel: MemoViewModel = hiltViewModel()) {
    val memos by viewModel.memos.collectAsStateWithLifecycle()
    val quote by viewModel.quote.collectAsStateWithLifecycle()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Kotlin 메모") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "메모 추가")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                QuoteCard(state = quote, onRefresh = viewModel::loadQuote)
            }
            items(memos, key = { it.id }) { memo ->
                MemoItem(
                    memo = memo,
                    onToggle = { viewModel.toggleDone(memo) },
                    onDelete = { viewModel.deleteMemo(memo) },
                )
            }
        }
    }

    if (showDialog) {
        AddMemoDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, content ->
                viewModel.addMemo(title, content)
                showDialog = false
            },
        )
    }
}

/**
 * 오늘의 명언 카드.
 * Kotlin 포인트: [UiState]가 sealed라서 `when`에 else 없이 모든 상태를 처리한다.
 *
 * @param state 명언 로딩 상태.
 * @param onRefresh 새로고침 버튼을 눌렀을 때 호출된다.
 */
@Composable
private fun QuoteCard(state: UiState<Quote>, onRefresh: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                when (state) {
                    UiState.Loading -> CircularProgressIndicator()
                    is UiState.Success -> {
                        Text(state.data.quote, style = MaterialTheme.typography.bodyLarge)
                        Text("- ${state.data.author}", style = MaterialTheme.typography.labelMedium)
                    }
                    is UiState.Error -> Text(
                        "불러오기 실패: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            IconButton(onClick = onRefresh) {
                Icon(Icons.Default.Refresh, contentDescription = "새로고침")
            }
        }
    }
}

/**
 * 메모 한 줄. 체크박스, 제목, 내용, 작성 시각, 삭제 버튼으로 구성된다.
 *
 * @param memo 보여줄 메모.
 * @param onToggle 체크박스를 눌렀을 때 호출된다.
 * @param onDelete 삭제 버튼을 눌렀을 때 호출된다.
 */
@Composable
private fun MemoItem(memo: Memo, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = memo.done, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = memo.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (memo.done) TextDecoration.LineThrough else null,
                )
                // content가 null이 아닐 때만 그린다.
                memo.content?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                Text(
                    memo.createdAt.toDateString(),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "삭제")
            }
        }
    }
}

/**
 * 새 메모를 입력받는 다이얼로그. 제목이 비어 있으면 저장 버튼이 비활성화된다.
 *
 * @param onDismiss 취소하거나 바깥을 눌렀을 때 호출된다.
 * @param onConfirm 저장을 눌렀을 때 제목과 내용(없으면 null)을 넘겨 호출된다.
 */
@Composable
private fun AddMemoDialog(onDismiss: () -> Unit, onConfirm: (String, String?) -> Unit) {
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 메모") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("제목") }, singleLine = true)
                OutlinedTextField(content, { content = it }, label = { Text("내용 (선택)") })
            }
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank(),
                onClick = { onConfirm(title.trim(), content.nullIfBlank()) },
            ) { Text("저장") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("취소") } },
    )
}
