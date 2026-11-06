package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.local.LiveClassEntity
import com.example.model.Language
import com.example.model.UserRole
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.viewmodel.LiveChatMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamScreen(
  liveClass: LiveClassEntity,
  currentLanguage: Language,
  userRole: UserRole,
  chatMessages: List<LiveChatMessage>,
  onSendMessage: (String) -> Unit,
  onClose: () -> Unit
) {
  var chatInput by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  // Scroll to bottom when new messages arrive
  LaunchedEffect(chatMessages.size) {
    if (chatMessages.isNotEmpty()) {
      listState.animateScrollToItem(chatMessages.size - 1)
    }
  }

  val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(700, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  Surface(
    modifier = Modifier
      .fillMaxSize()
      .testTag("live_stream_container"),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // 1. Live Video Stream Area
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
          .background(Color.Black)
      ) {
        // Broadcast stage graphics
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(RoyalBlue900, Color(0xFF070B19))
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(RoyalBlue800),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CastForEducation,
                contentDescription = null,
                tint = OrangeAccent,
                modifier = Modifier.size(36.dp)
              )
            }
            Text(
              text = liveClass.teacherName,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
            Text(
              text = liveClass.streamTopic.ifBlank { "बिहार बोर्ड 2026-27 लाइव डिजिटल क्लास" },
              color = Color.LightGray,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 24.dp)
            )
          }
        }

        // Live Badges & Back Button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)))
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          IconButton(onClick = onClose, modifier = Modifier.testTag("live_back_btn")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Live Tag
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color.Red.copy(alpha = pulseAlpha)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                )
                Text(
                  text = "LIVE",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // Viewers Count
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color.Black.copy(alpha = 0.6f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  Icons.Default.Visibility,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(12.dp)
                )
                Text(
                  text = "${liveClass.viewerCount.coerceAtLeast(1400)} Watching",
                  color = Color.White,
                  fontSize = 10.sp
                )
              }
            }
          }
        }
      }

      // 2. Class Title & Instructor Info
      Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = if (currentLanguage == Language.URDU) liveClass.titleUrdu else liveClass.titleHindi,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Subject: ${liveClass.subject} • Faculty: ${liveClass.teacherName}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // 3. Pinned Teacher Announcement
      chatMessages.find { it.isPinned }?.let { pinned ->
        Surface(
          color = OrangeAccent.copy(alpha = 0.12f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              Icons.Default.PushPin,
              contentDescription = "Pinned",
              tint = OrangeAccent,
              modifier = Modifier.size(16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${pinned.senderName}: ${pinned.message}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      // 4. Live Chat Stream
      Box(modifier = Modifier.weight(1f)) {
        LazyColumn(
          state = listState,
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 6.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(chatMessages) { msg ->
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (msg.role == "Teacher") RoyalBlue800.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
            ) {
              Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(
                    text = msg.senderName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (msg.role == "Teacher") RoyalBlue800 else MaterialTheme.colorScheme.primary
                  )
                  if (msg.role == "Teacher") {
                    Surface(
                      color = RoyalBlue800,
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text(
                        text = "TEACHER",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                      )
                    }
                  }
                  Text(
                    text = msg.time,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = msg.message,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }

      // 5. Send Chat Input Bar
      Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = chatInput,
            onValueChange = { chatInput = it },
            placeholder = { Text("Ask a question in live class...", fontSize = 12.sp) },
            modifier = Modifier
              .weight(1f)
              .testTag("live_chat_input"),
            singleLine = true,
            shape = RoundedCornerShape(24.dp)
          )

          IconButton(
            onClick = {
              if (chatInput.isNotBlank()) {
                onSendMessage(chatInput)
                chatInput = ""
              }
            },
            modifier = Modifier
              .clip(CircleShape)
              .background(RoyalBlue800)
              .testTag("live_send_chat_btn")
          ) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
          }
        }
      }
    }
  }
}
