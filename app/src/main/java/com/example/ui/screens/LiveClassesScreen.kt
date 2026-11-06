package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LiveClassEntity
import com.example.model.Language
import com.example.model.UserRole
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@Composable
fun LiveClassesScreen(
  liveClasses: List<LiveClassEntity>,
  currentLanguage: Language,
  userRole: UserRole,
  onJoinLiveClass: (LiveClassEntity) -> Unit,
  onOpenTeacherLiveModal: () -> Unit
) {
  val liveNowList = remember(liveClasses) { liveClasses.filter { it.isLiveNow } }
  val upcomingList = remember(liveClasses) { liveClasses.filter { !it.isLiveNow } }

  val remindersState = remember { mutableStateMapOf<Long, Boolean>() }

  val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(750, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("live_classes_screen_scroll"),
    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Teacher Broadcast Action (If user is Teacher or Admin)
    if (userRole == UserRole.TEACHER || userRole == UserRole.ADMIN) {
      item {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = RoyalBlue800,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Teacher Live Studio",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
              Text(
                text = "Start streaming to Bihar Board students or manage stream schedule",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp
              )
            }
            Button(
              onClick = onOpenTeacherLiveModal,
              colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("teacher_start_stream_btn")
            ) {
              Icon(Icons.Default.Podcasts, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Go Live")
            }
          }
        }
      }
    }

    // 2. Currently Live Now Classes
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(Color.Red.copy(alpha = pulseAlpha))
        )
        Text(
          text = "Live Now / अभी चल रहा है",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      }
    }

    if (liveNowList.isEmpty()) {
      item {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
            Text(
              text = "No live sessions streaming right now. Check upcoming schedule below.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(liveNowList) { liveClass ->
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 3.dp,
          border = androidx.compose.foundation.BorderStroke(1.5.dp, OrangeAccent),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onJoinLiveClass(liveClass) }
            .testTag("live_class_card_${liveClass.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                color = Color.Red,
                shape = RoundedCornerShape(4.dp)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White))
                  Text(text = "LIVE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
              }

              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.Visibility, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(16.dp))
                Text(
                  text = "${liveClass.viewerCount.coerceAtLeast(1400)} Watching",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = OrangeAccent
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = if (currentLanguage == Language.URDU) liveClass.titleUrdu else liveClass.titleHindi,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Subject: ${liveClass.subject} • Class: ${liveClass.classLevel.replace("_", " ").uppercase()}",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Faculty: ${liveClass.teacherName}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              color = RoyalBlue800
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = { onJoinLiveClass(liveClass) },
              colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth().testTag("join_live_btn_${liveClass.id}")
            ) {
              Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Join Interactive Class / शामिल हों", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 3. Upcoming Live Class Schedule
    item {
      Text(
        text = "Upcoming Live Classes / आगामी लाइव क्लासेस",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
      )
    }

    items(upcomingList) { upcoming ->
      val hasReminder = remindersState[upcoming.id] ?: false
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(RoyalBlue800.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Event, contentDescription = null, tint = RoyalBlue800)
          }

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = upcoming.scheduledTime,
              color = OrangeAccent,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (currentLanguage == Language.URDU) upcoming.titleUrdu else upcoming.titleHindi,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              maxLines = 2
            )
            Text(
              text = "${upcoming.subject} • ${upcoming.teacherName}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          IconButton(
            onClick = {
              remindersState[upcoming.id] = !hasReminder
            },
            modifier = Modifier.testTag("reminder_btn_${upcoming.id}")
          ) {
            Icon(
              imageVector = if (hasReminder) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsNone,
              contentDescription = "Set Reminder",
              tint = if (hasReminder) OrangeAccent else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
