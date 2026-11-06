package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PaymentEntity
import com.example.model.*
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@Composable
fun ProfileScreen(
  userSession: UserSession,
  currentLanguage: Language,
  isDarkMode: Boolean,
  payments: List<PaymentEntity>,
  onSelectLanguageClick: () -> Unit,
  onToggleDarkMode: (Boolean) -> Unit,
  onSwitchRole: (UserRole) -> Unit,
  onOpenAdminPanel: () -> Unit,
  onOpenTeacherPanel: () -> Unit,
  onLogout: () -> Unit
) {
  var showRoleDialog by remember { mutableStateOf(false) }
  var showPaymentsDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("profile_screen_scroll"),
    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Profile Card
    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(RoyalBlue800),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = userSession.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = userSession.name,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
              )
              Text(
                text = userSession.phone,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = userSession.email,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = when (userSession.role) {
                UserRole.ADMIN -> Color(0xFFEF4444)
                UserRole.TEACHER -> OrangeAccent
                UserRole.STUDENT -> RoyalBlue800
              }
            ) {
              Text(
                text = userSession.role.name,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Divider()

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = "Target Exam / लक्ष्य परीक्षा", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(
                text = userSession.selectedClass.titleHindi,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalBlue800
              )
            }

            TextButton(onClick = { showRoleDialog = true }) {
              Text("Switch Role", color = OrangeAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // 2. Admin & Teacher Management Panels Section
    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Management & Control Panels / प्रबंधन:", fontWeight = FontWeight.Bold, fontSize = 14.sp)

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEF4444).copy(alpha = 0.1f),
            modifier = Modifier
              .weight(1f)
              .clickable { onOpenAdminPanel() }
              .testTag("profile_admin_panel_btn")
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFFEF4444))
              Text(text = "Admin Panel", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFEF4444))
              Text(text = "Manage Courses, Live, Notes", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = OrangeAccent.copy(alpha = 0.1f),
            modifier = Modifier
              .weight(1f)
              .clickable { onOpenTeacherPanel() }
              .testTag("profile_teacher_panel_btn")
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(Icons.Default.CastForEducation, contentDescription = null, tint = OrangeAccent)
              Text(text = "Teacher Panel", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OrangeAccent)
              Text(text = "Start Live & Uploads", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    }

    // 3. Settings & Preferences
    item {
      Text(text = "App Settings / सेटिंग्स:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }

    item {
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          // Language Setting
          ListItem(
            headlineContent = { Text("App Language / भाषा / زبان") },
            supportingContent = { Text(currentLanguage.displayName) },
            leadingContent = { Icon(Icons.Default.Translate, contentDescription = null, tint = RoyalBlue800) },
            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
            modifier = Modifier
              .clickable { onSelectLanguageClick() }
              .testTag("profile_lang_setting")
          )

          Divider()

          // Dark Mode Setting
          ListItem(
            headlineContent = { Text("Dark Theme / डार्क मोड") },
            supportingContent = { Text(if (isDarkMode) "Enabled" else "Disabled") },
            leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = null, tint = OrangeAccent) },
            trailingContent = {
              Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggleDarkMode(it) }
              )
            }
          )

          Divider()

          // Payment History
          ListItem(
            headlineContent = { Text("Payment & Course Invoices") },
            supportingContent = { Text("${payments.size} Transactions recorded") },
            leadingContent = { Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = EmeraldLive) },
            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
            modifier = Modifier.clickable { showPaymentsDialog = true }
          )
        }
      }
    }

    // 4. Contact & Support
    item {
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(text = "The Champaran Classes Support", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text(
            text = "For student admissions, doubts or technical assistance:\n• Helpline: +91 98765 43210\n• Email: support@champaranclasses.com\n• Head Office: Motihari / Bettiah, Champaran, Bihar",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )
        }
      }
    }

    // 5. Logout Button
    item {
      OutlinedButton(
        onClick = onLogout,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("profile_logout_btn")
      ) {
        Icon(Icons.Default.ExitToApp, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Logout / लॉग आउट")
      }
    }
  }

  // Role Switcher Dialog
  if (showRoleDialog) {
    AlertDialog(
      onDismissRequest = { showRoleDialog = false },
      title = { Text("Switch Role (Test Demonstration)") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          UserRole.values().forEach { role ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (userSession.role == role) RoyalBlue800.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onSwitchRole(role)
                  showRoleDialog = false
                }
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "${role.name} Role", fontWeight = FontWeight.Bold)
                if (userSession.role == role) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = RoyalBlue800)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showRoleDialog = false }) { Text("Close") }
      }
    )
  }

  // Payment History Invoices Dialog
  if (showPaymentsDialog) {
    AlertDialog(
      onDismissRequest = { showPaymentsDialog = false },
      title = { Text("Payment & Invoice History") },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          if (payments.isEmpty()) {
            item { Text("No payments found.") }
          } else {
            items(payments) { p ->
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(text = "₹${p.amount}", fontWeight = FontWeight.Bold, color = EmeraldLive)
                    Text(text = p.status, fontSize = 11.sp, color = EmeraldLive, fontWeight = FontWeight.Bold)
                  }
                  Text(text = p.courseTitle, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                  Text(text = "${p.transactionId} • ${p.paymentMethod}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showPaymentsDialog = false }) { Text("Done") }
      }
    )
  }
}
