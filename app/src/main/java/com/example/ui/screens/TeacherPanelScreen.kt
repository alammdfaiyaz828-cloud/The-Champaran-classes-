package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.UserSession
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPanelScreen(
  userSession: UserSession,
  liveClasses: List<LiveClassEntity>,
  onClose: () -> Unit,
  onToggleLiveStream: (LiveClassEntity, Boolean) -> Unit
) {
  var isCamOn by remember { mutableStateOf(true) }
  var isMicOn by remember { mutableStateOf(true) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Teacher Studio / शिक्षक पैनल", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        navigationIcon = {
          IconButton(onClick = onClose, modifier = Modifier.testTag("teacher_close_btn")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = OrangeAccent,
          titleContentColor = Color.White
        )
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
      // 1. Teacher Profile Header
      item {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(OrangeAccent),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(text = userSession.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
              Text(text = "Senior Board Faculty • The Champaran Classes", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "1,420 Active Bihar Board Students Connected", fontSize = 11.sp, color = RoyalBlue800, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }

      // 2. Broadcast Hardware Controller
      item {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = RoyalBlue800.copy(alpha = 0.08f),
          border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBlue800.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Broadcast Controls & Peripherals:", fontWeight = FontWeight.Bold, fontSize = 14.sp)

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = { isCamOn = !isCamOn },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isCamOn) RoyalBlue800 else Color.Gray
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(if (isCamOn) Icons.Default.Videocam else Icons.Default.VideocamOff, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isCamOn) "Cam ON" else "Cam OFF", fontSize = 12.sp)
              }

              Button(
                onClick = { isMicOn = !isMicOn },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isMicOn) EmeraldLive else Color.Gray
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(if (isMicOn) Icons.Default.Mic else Icons.Default.MicOff, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isMicOn) "Mic ON" else "Mic Muted", fontSize = 12.sp)
              }
            }
          }
        }
      }

      // 3. Live Broadcast Switcher for Scheduled Classes
      item {
        Text(text = "Live Stream Manager / लाइव प्रसारण प्रबंधन:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      }

      items(liveClasses) { liveClass ->
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = liveClass.titleHindi,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
              )

              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (liveClass.isLiveNow) Color.Red else Color.Gray
              ) {
                Text(
                  text = if (liveClass.isLiveNow) "STREAMING LIVE" else "OFFLINE",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = "Time: ${liveClass.scheduledTime} • Viewers: ${if (liveClass.isLiveNow) liveClass.viewerCount else 0}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              if (liveClass.isLiveNow) {
                Button(
                  onClick = { onToggleLiveStream(liveClass, false) },
                  colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text("End Live Stream")
                }
              } else {
                Button(
                  onClick = { onToggleLiveStream(liveClass, true) },
                  colors = ButtonDefaults.buttonColors(containerColor = EmeraldLive),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text("Start Live Broadcasting")
                }
              }
            }
          }
        }
      }
    }
  }
}
