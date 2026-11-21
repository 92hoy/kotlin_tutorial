package com.example.kotlintutorial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlintutorial.data.Memo
import com.example.kotlintutorial.data.MemoRepository
import com.example.kotlintutorial.data.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 메모 화면의 상태를 들고 있는 ViewModel.
 * 화면 회전 같은 구성 변경에도 상태가 유지된다.
 */
@HiltViewModel
class MemoViewModel @Inject constructor(
    private val repository: MemoRepository,
) : ViewModel() {

    /**
     * 화면에 보여줄 메모 목록.
     * DB의 Flow를 StateFlow로 바꿨고, 화면이 보일 때만 구독하다가
     * 5초 안에 다시 보이면 구독을 유지한다.
     */
    val memos: StateFlow<List<Memo>> = repository.memos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _quote = MutableStateFlow<UiState<Quote>>(UiState.Loading)

    /** 명언 카드의 상태(로딩, 성공, 실패). 외부에는 읽기 전용으로만 공개한다. */
    val quote: StateFlow<UiState<Quote>> = _quote.asStateFlow()

    init {
        loadQuote()
    }

    /**
     * 명언을 새로 불러온다.
     * Kotlin 포인트: `Result.fold`로 성공과 실패를 한 식에서 [UiState]로 변환한다.
     */
    fun loadQuote() {
        viewModelScope.launch {
            _quote.value = UiState.Loading
            _quote.value = repository.fetchQuote().fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "알 수 없는 오류") },
            )
        }
    }

    /** 새 메모를 저장한다. 내용은 없어도 된다. */
    fun addMemo(title: String, content: String?) {
        viewModelScope.launch { repository.add(title, content) }
    }

    /** 메모의 완료 상태를 토글한다. */
    fun toggleDone(memo: Memo) {
        viewModelScope.launch { repository.toggleDone(memo) }
    }

    /** 메모를 삭제한다. */
    fun deleteMemo(memo: Memo) {
        viewModelScope.launch { repository.delete(memo) }
    }
}
