package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionEntity
import com.example.data.local.TestAttemptEntity
import com.example.data.local.TestEntity
import com.example.model.Language
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestsScreen(
  tests: List<TestEntity>,
  attempts: List<TestAttemptEntity>,
  currentLanguage: Language,
  activeTest: TestEntity?,
  testQuestions: List<QuestionEntity>,
  currentQuestionIdx: Int,
  selectedAnswers: Map<Int, Int>,
  timerSeconds: Int,
  testResult: TestAttemptEntity?,
  onStartTest: (TestEntity) -> Unit,
  onSelectAnswer: (questionIdx: Int, optionIdx: Int) -> Unit,
  onGoToQuestion: (Int) -> Unit,
  onSubmitTest: () -> Unit,
  onCloseTest: () -> Unit
) {
  var selectedTabIdx by remember { mutableStateOf(0) } // 0: Available Tests, 1: Test History & Reports

  // If a test is actively being taken
  if (activeTest != null) {
    if (testResult != null) {
      // Test Result / Scorecard View
      TestResultScorecardView(
        result = testResult,
        test = activeTest,
        questions = testQuestions,
        userAnswers = selectedAnswers,
        currentLanguage = currentLanguage,
        onClose = onCloseTest
      )
    } else {
      // Active Test Runner Screen
      TestRunnerView(
        test = activeTest,
        questions = testQuestions,
        currentQuestionIdx = currentQuestionIdx,
        selectedAnswers = selectedAnswers,
        timerSeconds = timerSeconds,
        currentLanguage = currentLanguage,
        onSelectAnswer = onSelectAnswer,
        onGoToQuestion = onGoToQuestion,
        onSubmit = onSubmitTest,
        onCancel = onCloseTest
      )
    }
    return
  }

  // Normal Tests Screen: List of Tests & History
  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("tests_screen_root")
  ) {
    TabRow(
      selectedTabIndex = selectedTabIdx,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = RoyalBlue800
    ) {
      Tab(
        selected = selectedTabIdx == 0,
        onClick = { selectedTabIdx = 0 },
        text = { Text("Available Tests (${tests.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
      )
      Tab(
        selected = selectedTabIdx == 1,
        onClick = { selectedTabIdx = 1 },
        text = { Text("My Test History (${attempts.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
      )
    }

    if (selectedTabIdx == 0) {
      // Available Tests List
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(tests) { test ->
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("test_card_${test.id}")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  color = RoyalBlue800.copy(alpha = 0.1f),
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = test.subject,
                    color = RoyalBlue800,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }

                Surface(
                  color = OrangeAccent.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "${test.durationMinutes} Mins",
                    color = OrangeAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = if (currentLanguage == Language.URDU) test.titleUrdu else test.titleHindi,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )

              Spacer(modifier = Modifier.height(10.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                  Text(text = "Total: ${test.totalQuestions} MCQs", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  Text(text = "Marks: ${test.totalMarks}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Button(
                  onClick = { onStartTest(test) },
                  colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                  modifier = Modifier.testTag("start_test_btn_${test.id}")
                ) {
                  Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Start Test / शुरू करें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    } else {
      // Test History List
      if (attempts.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.AssignmentLate, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("No test attempts yet", fontWeight = FontWeight.Bold)
            Text("Attempt any test from the Available Tests tab to see your score & solutions here.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(attempts) { attempt ->
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surface,
              tonalElevation = 2.dp,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = attempt.testTitleHindi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                  )

                  Surface(
                    color = if (attempt.score >= (attempt.totalMarks * 0.4)) EmeraldLive.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                  ) {
                    Text(
                      text = "${attempt.score} / ${attempt.totalMarks} Marks",
                      color = if (attempt.score >= (attempt.totalMarks * 0.4)) EmeraldLive else Color.Red,
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(text = "✓ Correct: ${attempt.correctAnswers}", fontSize = 11.sp, color = EmeraldLive, fontWeight = FontWeight.SemiBold)
                  Text(text = "✗ Incorrect: ${attempt.wrongAnswers}", fontSize = 11.sp, color = Color.Red, fontWeight = FontWeight.SemiBold)
                  Text(text = "Accuracy: ${attempt.accuracyPercent}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoyalBlue800)
                }
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestRunnerView(
  test: TestEntity,
  questions: List<QuestionEntity>,
  currentQuestionIdx: Int,
  selectedAnswers: Map<Int, Int>,
  timerSeconds: Int,
  currentLanguage: Language,
  onSelectAnswer: (questionIdx: Int, optionIdx: Int) -> Unit,
  onGoToQuestion: (Int) -> Unit,
  onSubmit: () -> Unit,
  onCancel: () -> Unit
) {
  val currentQuestion = questions.getOrNull(currentQuestionIdx)
  val isUrdu = currentLanguage == Language.URDU

  fun formatTimer(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
  }

  CompositionLocalProvider(
    LocalLayoutDirection provides (if (isUrdu) LayoutDirection.Rtl else LayoutDirection.Ltr)
  ) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Column {
              Text(
                text = if (isUrdu) test.titleUrdu else test.titleHindi,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
              )
              Text(text = "Question ${currentQuestionIdx + 1} of ${questions.size}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
            }
          },
          navigationIcon = {
            IconButton(onClick = onCancel) {
              Icon(Icons.Default.Close, contentDescription = "Exit", tint = Color.White)
            }
          },
          actions = {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (timerSeconds < 120) Color.Red else Color.White.copy(alpha = 0.2f),
              modifier = Modifier.padding(end = 8.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Text(
                  text = formatTimer(timerSeconds),
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Button(
              onClick = onSubmit,
              colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
              shape = RoundedCornerShape(6.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.testTag("submit_test_now_btn")
            ) {
              Text("Submit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue800, titleContentColor = Color.White)
        )
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        if (currentQuestion != null) {
          Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Question Palette Navigation Grid
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(questions.indices.toList()) { idx ->
                val isAnswered = selectedAnswers.containsKey(idx)
                val isCurrent = idx == currentQuestionIdx
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                      when {
                        isCurrent -> OrangeAccent
                        isAnswered -> EmeraldLive
                        else -> MaterialTheme.colorScheme.surfaceVariant
                      }
                    )
                    .clickable { onGoToQuestion(idx) },
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${idx + 1}",
                    color = if (isCurrent || isAnswered) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                }
              }
            }

            // Question Statement Card
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surface,
              tonalElevation = 2.dp,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = "Q${currentQuestionIdx + 1}. " + if (isUrdu) currentQuestion.questionUrdu else currentQuestion.questionHindi,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  lineHeight = 22.sp
                )
              }
            }

            // 4 Options
            val options = listOf(
              0 to (if (isUrdu) currentQuestion.optionAUrdu else currentQuestion.optionAHindi),
              1 to (if (isUrdu) currentQuestion.optionBUrdu else currentQuestion.optionBHindi),
              2 to (if (isUrdu) currentQuestion.optionCUrdu else currentQuestion.optionCHindi),
              3 to (if (isUrdu) currentQuestion.optionDUrdu else currentQuestion.optionDHindi)
            )

            options.forEach { (optIdx, optText) ->
              val isSelected = selectedAnswers[currentQuestionIdx] == optIdx
              Surface(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) RoyalBlue800 else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                color = if (isSelected) RoyalBlue800.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onSelectAnswer(currentQuestionIdx, optIdx) }
                  .testTag("option_${optIdx}_q_${currentQuestionIdx}")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(28.dp)
                      .clip(CircleShape)
                      .background(if (isSelected) RoyalBlue800 else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = ('A' + optIdx).toString(),
                      color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp
                    )
                  }
                  Text(text = optText, fontSize = 14.sp, modifier = Modifier.weight(1f))
                }
              }
            }
          }

          // Bottom Prev / Next Navigation Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            OutlinedButton(
              onClick = { if (currentQuestionIdx > 0) onGoToQuestion(currentQuestionIdx - 1) },
              enabled = currentQuestionIdx > 0,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Previous / पिछला")
            }

            if (currentQuestionIdx < questions.size - 1) {
              Button(
                onClick = { onGoToQuestion(currentQuestionIdx + 1) },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue800),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("Next / अगला")
              }
            } else {
              Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("Submit Test / जमा करें", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestResultScorecardView(
  result: TestAttemptEntity,
  test: TestEntity,
  questions: List<QuestionEntity>,
  userAnswers: Map<Int, Int>,
  currentLanguage: Language,
  onClose: () -> Unit
) {
  val isUrdu = currentLanguage == Language.URDU

  CompositionLocalProvider(
    LocalLayoutDirection provides (if (isUrdu) LayoutDirection.Rtl else LayoutDirection.Ltr)
  ) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text("Scorecard & Solutions / परिणाम व समाधान", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
          actions = {
            IconButton(onClick = onClose, modifier = Modifier.testTag("result_close_btn")) {
              Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue800, titleContentColor = Color.White)
        )
      }
    ) { innerPadding ->
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // 1. Score Summary Banner
        item {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = RoyalBlue800,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(20.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = "YOUR TEST PERFORMANCE",
                color = OrangeAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${result.score} / ${result.totalMarks}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
              )
              Text(
                text = "Accuracy: ${result.accuracyPercent}% • Correct: ${result.correctAnswers} • Wrong: ${result.wrongAnswers}",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
              )
            }
          }
        }

        // 2. Question Explanations & Solutions Header
        item {
          Text(
            text = "Detailed Answer Key & Explanations / विस्तृत व्याख्या:",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }

        // 3. Questions with Explanations
        items(questions.indices.toList()) { idx ->
          val q = questions[idx]
          val userSelected = userAnswers[idx]
          val isCorrect = userSelected == q.correctOptionIndex

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "Question ${idx + 1}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RoyalBlue800)
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = if (isCorrect) EmeraldLive.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f)
                ) {
                  Text(
                    text = if (isCorrect) "✓ Correct" else "✗ Incorrect",
                    color = if (isCorrect) EmeraldLive else Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Text(
                text = if (isUrdu) q.questionUrdu else q.questionHindi,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
              )

              Divider(modifier = Modifier.padding(vertical = 4.dp))

              Text(
                text = "Correct Answer: " + ('A' + q.correctOptionIndex) + ". " + when (q.correctOptionIndex) {
                  0 -> if (isUrdu) q.optionAUrdu else q.optionAHindi
                  1 -> if (isUrdu) q.optionBUrdu else q.optionBHindi
                  2 -> if (isUrdu) q.optionCUrdu else q.optionCHindi
                  else -> if (isUrdu) q.optionDUrdu else q.optionDHindi
                },
                color = EmeraldLive,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(8.dp)) {
                  Text(text = "Explanation / व्याख्या:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  Text(
                    text = if (isUrdu) q.explanationUrdu else q.explanationHindi,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
