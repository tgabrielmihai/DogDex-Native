package com.dogdex.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogdex.data.model.Breed
import com.dogdex.data.repo.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizQuestion(
    val imageUrl: String,
    val correctName: String,
    val options: List<String>,
)

enum class QuizStatus { Loading, Error, Playing, Finished }

data class QuizState(
    val status: QuizStatus = QuizStatus.Loading,
    val errorMessage: String = "",
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedOption: String? = null,
) {
    val total: Int get() = questions.size
    val current: QuizQuestion? get() = questions.getOrNull(currentIndex)
    val isLastQuestion: Boolean get() = currentIndex == questions.lastIndex
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state.asStateFlow()

    init {
        startQuiz()
    }

    fun startQuiz() {
        _state.value = QuizState(status = QuizStatus.Loading)
        viewModelScope.launch {
            runCatching { breedRepository.getBreeds() }
                .onSuccess { breeds ->
                    val questions = buildQuestions(breeds)
                    _state.value = if (questions.isEmpty()) {
                        QuizState(status = QuizStatus.Error, errorMessage = "Not enough breeds with images to start a quiz.")
                    } else {
                        QuizState(status = QuizStatus.Playing, questions = questions)
                    }
                }
                .onFailure {
                    _state.value = QuizState(status = QuizStatus.Error, errorMessage = it.message ?: "Failed to load quiz")
                }
        }
    }

    /** Pick [TOTAL] breeds that have images; each question gets 3 random distractor names. */
    private fun buildQuestions(breeds: List<Breed>): List<QuizQuestion> {
        val withImages = breeds.filter { it.hasImage }
        if (withImages.size < OPTIONS) return emptyList()

        return withImages.shuffled().take(TOTAL).map { answer ->
            val distractors = withImages
                .filter { it.id != answer.id }
                .shuffled()
                .map { it.name }
                .distinct()
                .take(OPTIONS - 1)
            val options = (distractors + answer.name).shuffled()
            QuizQuestion(imageUrl = answer.imageUrl, correctName = answer.name, options = options)
        }
    }

    fun selectAnswer(option: String) {
        val s = _state.value
        if (s.selectedOption != null || s.status != QuizStatus.Playing) return // lock after first tap
        val isCorrect = option == s.current?.correctName
        _state.update {
            it.copy(
                selectedOption = option,
                score = if (isCorrect) it.score + 1 else it.score,
            )
        }
    }

    fun next() {
        val s = _state.value
        if (s.selectedOption == null) return
        if (s.isLastQuestion) {
            _state.update { it.copy(status = QuizStatus.Finished) }
        } else {
            _state.update { it.copy(currentIndex = it.currentIndex + 1, selectedOption = null) }
        }
    }

    private companion object {
        const val TOTAL = 5
        const val OPTIONS = 4
    }
}
