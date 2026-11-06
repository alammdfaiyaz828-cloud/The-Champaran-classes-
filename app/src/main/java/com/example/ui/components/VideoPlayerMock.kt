package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LectureEntity
import com.example.model.Language
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerScreen(
  lecture: LectureEntity,
  currentLanguage: Language,
  onClose: () -> Unit,
  onProgressUpdate: (lectureId: Long, positionSecs: Int, completed: Boolean) -> Unit,
  onToggleFavorite: (LectureEntity) -> Unit
) {
  val totalDurationSecs = remember(lecture.durationMinutes) { lecture.durationMinutes * 60 }
  var currentPositionSecs by remember(lecture.id) {
    mutableStateOf(if (lecture.lastPositionSeconds > 0) lecture.lastPositionSeconds else 120)
  }
  var isPlaying by remember { mutableStateOf(true) }
  var playbackSpeed by remember { mutableStateOf(1.0f) }
  var showSpeedMenu by remember { mutableStateOf(false) }
  var videoQuality by remember { mutableStateOf("720p HD") }
  var showQualityMenu by remember { mutableStateOf(false) }
  var isCompleted by remember(lecture.isCompleted) { mutableStateOf(lecture.isCompleted) }

  // Simulated video playback ticker
  LaunchedEffect(isPlaying, playbackSpeed) {
    while (isPlaying && currentPositionSecs < totalDurationSecs) {
      delay((1000 / playbackSpeed).toLong())
      currentPositionSecs += 1
      if (currentPositionSecs >= totalDurationSecs) {
        isPlaying = false
        isCompleted = true
        onProgressUpdate(lecture.id, totalDurationSecs, true)
      }
    }
  }

  // Auto save progress every 5 seconds
  LaunchedEffect(currentPositionSecs) {
    if (currentPositionSecs % 5 == 0) {
      onProgressUpdate(lecture.id, currentPositionSecs, isCompleted)
    }
  }

  fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
  }

  Surface(
    modifier = Modifier
      .fillMaxSize()
      .testTag("video_player_container"),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // 1. Video Display Container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
          .background(Color.Black)
      ) {
        // Video simulated graphics background
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.radialGradient(
                colors = listOf(RoyalBlue900, Color.Black),
                radius = 700f
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = OrangeAccent.copy(alpha = 0.9f)
            ) {
              Text(
                text = "BIHAR BOARD CLASS 10 & 12 ARTS",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
            Text(
              text = if (currentLanguage == Language.URDU) lecture.titleUrdu else lecture.titleHindi,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
              text = "The Champaran Classes • Digital Smart Class",
              color = Color.LightGray,
              fontSize = 11.sp
            )
          }
        }

        // Top Video Controls Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)))
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          IconButton(onClick = onClose, modifier = Modifier.testTag("video_close_btn")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            // Speed Chip
            Box {
              TextButton(onClick = { showSpeedMenu = true }) {
                Text("${playbackSpeed}x", color = Color.White, fontWeight = FontWeight.Bold)
              }
              DropdownMenu(expanded = showSpeedMenu, onDismissRequest = { showSpeedMenu = false }) {
                listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { spd ->
                  DropdownMenuItem(
                    text = { Text("${spd}x") },
                    onClick = {
                      playbackSpeed = spd
                      showSpeedMenu = false
                    }
                  )
                }
              }
            }

            // Quality Chip
            Box {
              TextButton(onClick = { showQualityMenu = true }) {
                Text(videoQuality, color = Color.White, fontSize = 12.sp)
              }
              DropdownMenu(expanded = showQualityMenu, onDismissRequest = { showQualityMenu = false }) {
                listOf("360p", "480p", "720p HD", "1080p FHD").forEach { q ->
                  DropdownMenuItem(
                    text = { Text(q) },
                    onClick = {
                      videoQuality = q
                      showQualityMenu = false
                    }
                  )
                }
              }
            }
          }
        }

        // Center Play / 10s Rewind / 10s Forward Controls
        Row(
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.7f),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = {
              currentPositionSecs = (currentPositionSecs - 10).coerceAtLeast(0)
            }
          ) {
            Icon(
              Icons.Default.Replay10,
              contentDescription = "Replay 10s",
              tint = Color.White,
              modifier = Modifier.size(36.dp)
            )
          }

          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(OrangeAccent)
              .clickable { isPlaying = !isPlaying }
              .testTag("video_play_pause_btn"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isPlaying) "Pause" else "Play",
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }

          IconButton(
            onClick = {
              currentPositionSecs = (currentPositionSecs + 10).coerceAtMost(totalDurationSecs)
            }
          ) {
            Icon(
              Icons.Default.Forward10,
              contentDescription = "Forward 10s",
              tint = Color.White,
              modifier = Modifier.size(36.dp)
            )
          }
        }

        // Bottom Seekbar & Timing
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))))
            .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Slider(
            value = currentPositionSecs.toFloat(),
            onValueChange = { currentPositionSecs = it.toInt() },
            valueRange = 0f..totalDurationSecs.toFloat(),
            colors = SliderDefaults.colors(
              thumbColor = OrangeAccent,
              activeTrackColor = OrangeAccent,
              inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth().height(24.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${formatTime(currentPositionSecs)} / ${formatTime(totalDurationSecs)}",
              color = Color.White,
              fontSize = 11.sp
            )
            Text(
              text = if (isCompleted) "✓ Completed / समाप्त" else "Playing",
              color = if (isCompleted) Color(0xFF10B981) else Color.White.copy(alpha = 0.8f),
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // 2. Video Metadata & Lesson Actions
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (currentLanguage == Language.URDU) lecture.chapterNameUrdu else lecture.chapterNameHindi,
              color = RoyalBlue800,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (currentLanguage == Language.URDU) lecture.titleUrdu else lecture.titleHindi,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Subject: ${lecture.subject} • Duration: ${lecture.durationMinutes} mins",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 12.sp
            )
          }

          // Bookmark / Favorite
          IconButton(
            onClick = { onToggleFavorite(lecture) },
            modifier = Modifier.testTag("video_fav_btn")
          ) {
            Icon(
              imageVector = if (lecture.isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = "Favorite",
              tint = if (lecture.isFavorite) OrangeAccent else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              isCompleted = !isCompleted
              onProgressUpdate(lecture.id, currentPositionSecs, isCompleted)
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isCompleted) Color(0xFF10B981) else RoyalBlue800
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f).testTag("mark_complete_btn")
          ) {
            Icon(
              imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isCompleted) "Completed" else "Mark Complete")
          }

          OutlinedButton(
            onClick = onClose,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(0.8f)
          ) {
            Text("Back / वापस")
          }
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // Lesson Overview & Study Tips
        Text(
          text = "Key Board Exam Focus Points:",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = "• बिहार बोर्ड परीक्षा में इस अध्याय से 8 से 10 अंक के लघु और दीर्घ उत्तरीय प्रश्न पूछे जाते हैं।",
              fontSize = 12.sp
            )
            Text(
              text = "• نوٹس ٹیب سے پی ڈی ایف ڈاؤن لوڈ کریں اور باب وار معروضی سوالات (MCQ) کا ٹیسٹ ضرور دیں۔",
              fontSize = 12.sp,
              color = RoyalBlue800
            )
          }
        }
      }
    }
  }
}
