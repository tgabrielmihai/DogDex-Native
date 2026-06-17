package com.dogdex.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dogdex.ui.components.DogDexHeader
import com.dogdex.ui.components.ErrorState
import com.dogdex.ui.components.LoadingState
import com.dogdex.ui.theme.CorrectGreen
import com.dogdex.ui.theme.DogOrange
import com.dogdex.ui.theme.GlassCard

@Composable
fun QuizScreen(
    onNavigateHome: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    // Auto-advance ~1.2s after the user picks an answer.
    LaunchedEffect(state.selectedOption, state.currentIndex) {
        if (state.selectedOption != null) {
            kotlinx.coroutines.delay(1200)
            viewModel.next()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when (state.status) {
            QuizStatus.Loading -> LoadingState()
            QuizStatus.Error -> ErrorState(message = state.errorMessage, onRetry = viewModel::startQuiz)
            QuizStatus.Playing -> PlayingContent(state, onSelect = viewModel::selectAnswer)
            QuizStatus.Finished -> FinishedContent(
                score = state.score,
                total = state.total,
                onPlayAgain = viewModel::startQuiz,
                onNavigateHome = onNavigateHome,
            )
        }
    }
}

@Composable
private fun PlayingContent(state: QuizState, onSelect: (String) -> Unit) {
    val question = state.current ?: return
    DogDexHeader(title = "Guess the Breed!", subtitle = "Question ${state.currentIndex + 1} of ${state.total}")

    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AsyncImage(
            model = question.imageUrl,
            contentDescription = "Which breed is this?",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        question.options.forEach { option ->
            OptionRow(
                text = option,
                selected = state.selectedOption,
                correct = question.correctName,
                onClick = { onSelect(option) },
            )
        }
    }
}

@Composable
private fun OptionRow(text: String, selected: String?, correct: String, onClick: () -> Unit) {
    val answered = selected != null
    val isCorrect = text == correct
    val isPicked = text == selected

    // Once answered: correct -> green, picked-wrong -> red, others neutral.
    val (bg, fg) = when {
        !answered -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
        isCorrect -> CorrectGreen to Color.White
        isPicked -> MaterialTheme.colorScheme.error to Color.White
        else -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.dp, DogOrange.copy(alpha = if (answered) 0f else 0.5f), RoundedCornerShape(14.dp))
            .clickable(enabled = !answered, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Text(text, color = fg, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun FinishedContent(score: Int, total: Int, onPlayAgain: () -> Unit, onNavigateHome: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        GlassCard(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier.size(72.dp).clip(CircleShape).background(DogOrange),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Text("Quiz Complete!", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Text("$score/$total", style = MaterialTheme.typography.headlineMedium, color = DogOrange, fontWeight = FontWeight.Bold)
                Text(
                    resultMessage(score, total),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DogOrange),
                ) { Text("Play Again", color = Color.White, fontWeight = FontWeight.SemiBold) }
                OutlinedButton(
                    onClick = onNavigateHome,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                ) { Text("Back to Home") }
            }
        }
    }
}

private fun resultMessage(score: Int, total: Int): String = when {
    total == 0 -> ""
    score == total -> "Perfect! You're a dog expert!"
    score >= total / 2 -> "Nice work! Keep sniffing around."
    else -> "Keep practicing — every dog has its day!"
}
