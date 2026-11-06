package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppStrings
import com.example.model.Language
import com.example.model.UserRole
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
  currentLanguage: Language,
  userRole: UserRole,
  unreadNotificationCount: Int,
  onLanguageClick: () -> Unit,
  onNotificationClick: () -> Unit,
  onRoleClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  TopAppBar(
    modifier = modifier.testTag("app_top_bar"),
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = RoyalBlue800,
      titleContentColor = Color.White,
      actionIconContentColor = Color.White
    ),
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(OrangeAccent),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.School,
            contentDescription = "School Logo",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
        }
        Column {
          Text(
            text = AppStrings.get("app_name", currentLanguage),
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = AppStrings.get("tagline", currentLanguage),
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.85f),
            maxLines = 1
          )
        }
      }
    },
    actions = {
      // Role Badge
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = when (userRole) {
          UserRole.ADMIN -> Color(0xFFEF4444)
          UserRole.TEACHER -> OrangeAccent
          UserRole.STUDENT -> Color.White.copy(alpha = 0.2f)
        },
        modifier = Modifier
          .padding(end = 4.dp)
          .clickable { onRoleClick() }
          .testTag("role_badge_button")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = when (userRole) {
              UserRole.ADMIN -> Icons.Default.AdminPanelSettings
              UserRole.TEACHER -> Icons.Default.Psychology
              UserRole.STUDENT -> Icons.Default.Person
            },
            contentDescription = "Role",
            tint = Color.White,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = userRole.name,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Language Switcher Button
      IconButton(
        onClick = onLanguageClick,
        modifier = Modifier.testTag("language_switch_button")
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = when (currentLanguage) {
              Language.HINDI -> "हिन्दी"
              Language.URDU -> "اردو"
              Language.ENGLISH -> "EN"
            },
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Notification Bell with Badge
      IconButton(
        onClick = onNotificationClick,
        modifier = Modifier.testTag("notifications_button")
      ) {
        BadgedBox(
          badge = {
            if (unreadNotificationCount > 0) {
              Badge(containerColor = OrangeAccent) {
                Text("$unreadNotificationCount", color = Color.White, fontSize = 9.sp)
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color.White
          )
        }
      }
    }
  )
}
