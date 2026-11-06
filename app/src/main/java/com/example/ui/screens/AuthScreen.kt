package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.model.AcademicClass
import com.example.model.Language
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800
import com.example.viewmodel.AuthScreenMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
  currentMode: AuthScreenMode,
  currentLanguage: Language,
  onCompleteLogin: (name: String, mobile: String) -> Unit,
  onSwitchMode: (AuthScreenMode) -> Unit,
  onDismiss: () -> Unit
) {
  var name by remember { mutableStateOf("Md Faiyaz Alam") }
  var mobile by remember { mutableStateOf("9876543210") }
  var email by remember { mutableStateOf("alammdfaiyaz828@gmail.com") }
  var password by remember { mutableStateOf("Champaran@123") }
  var otpCode by remember { mutableStateOf("") }
  var selectedClass by remember { mutableStateOf(AcademicClass.CLASS_12_ARTS) }
  var selectedLanguage by remember { mutableStateOf(Language.HINDI) }

  val generatedDemoOtp = "5280"

  Surface(
    modifier = Modifier
      .fillMaxSize()
      .testTag("auth_screen_container"),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Close button if dismissible
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close")
        }
      }

      // App Logo Emblem
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(RoyalBlue800),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          Icons.Default.School,
          contentDescription = "Logo",
          tint = OrangeAccent,
          modifier = Modifier.size(42.dp)
        )
      }

      Text(
        text = "The Champaran Classes",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        color = RoyalBlue800
      )

      Text(
        text = "Learn Today, Lead Tomorrow • Bihar Board Special",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      // Tab selector: Login vs Register
      if (currentMode != AuthScreenMode.OTP_VERIFY) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(modifier = Modifier.padding(4.dp)) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (currentMode == AuthScreenMode.LOGIN) RoyalBlue800 else Color.Transparent,
              modifier = Modifier
                .weight(1f)
                .clickable { onSwitchMode(AuthScreenMode.LOGIN) }
            ) {
              Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                Text(
                  text = "Login / लॉग इन",
                  fontWeight = FontWeight.Bold,
                  color = if (currentMode == AuthScreenMode.LOGIN) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (currentMode == AuthScreenMode.REGISTER) RoyalBlue800 else Color.Transparent,
              modifier = Modifier
                .weight(1f)
                .clickable { onSwitchMode(AuthScreenMode.REGISTER) }
            ) {
              Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                Text(
                  text = "Register / नया खाता",
                  fontWeight = FontWeight.Bold,
                  color = if (currentMode == AuthScreenMode.REGISTER) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }

      // Mode-Specific Fields
      when (currentMode) {
        AuthScreenMode.LOGIN -> {
          OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it },
            label = { Text("Mobile Number / मोबाइल नंबर") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            prefix = { Text("+91 ") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("auth_mobile_input"),
            shape = RoundedCornerShape(10.dp)
          )

          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password / पासवर्ड") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("auth_password_input"),
            shape = RoundedCornerShape(10.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            TextButton(onClick = { onSwitchMode(AuthScreenMode.OTP_VERIFY) }) {
              Text("Login via OTP", color = OrangeAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = { onSwitchMode(AuthScreenMode.FORGOT_PASSWORD) }) {
              Text("Forgot Password?", fontSize = 12.sp)
            }
          }

          Button(
            onClick = { onCompleteLogin(name, mobile) },
            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue800),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("auth_login_btn")
          ) {
            Text("Login to Dashboard", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }
        }

        AuthScreenMode.REGISTER -> {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name / विद्यार्थी का नाम") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("auth_reg_name"),
            shape = RoundedCornerShape(10.dp)
          )

          OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it },
            label = { Text("Mobile Number (+91)") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            prefix = { Text("+91 ") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("auth_reg_mobile"),
            shape = RoundedCornerShape(10.dp)
          )

          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )

          // Class Selection Picker
          Text(text = "Select Your Target Class / कक्षा:", fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.align(Alignment.Start))
          AcademicClass.values().forEach { cls ->
            val isSelected = selectedClass == cls
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) RoyalBlue800.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedClass = cls }
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = cls.titleHindi, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                RadioButton(selected = isSelected, onClick = { selectedClass = cls })
              }
            }
          }

          Button(
            onClick = { onSwitchMode(AuthScreenMode.OTP_VERIFY) },
            colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("auth_reg_submit_btn")
          ) {
            Text("Send OTP & Verify", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }
        }

        AuthScreenMode.OTP_VERIFY -> {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = OrangeAccent.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "Demo SMS OTP Sent to +91 $mobile", fontSize = 12.sp, color = OrangeAccent, fontWeight = FontWeight.Bold)
              Text(text = "Your verification code: $generatedDemoOtp", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = RoyalBlue800)
              TextButton(onClick = { otpCode = generatedDemoOtp }) {
                Text("Tap to Autofill ($generatedDemoOtp)", fontSize = 11.sp, color = RoyalBlue800)
              }
            }
          }

          OutlinedTextField(
            value = otpCode,
            onValueChange = { if (it.length <= 4) otpCode = it },
            label = { Text("Enter 4-Digit OTP") },
            leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("auth_otp_input"),
            shape = RoundedCornerShape(10.dp)
          )

          Button(
            onClick = { onCompleteLogin(name, mobile) },
            enabled = otpCode.length == 4,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldLive),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("auth_verify_btn")
          ) {
            Text("Verify & Enter Dashboard", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }

          TextButton(onClick = { onSwitchMode(AuthScreenMode.LOGIN) }) {
            Text("Back to Login / वापस")
          }
        }

        AuthScreenMode.FORGOT_PASSWORD -> {
          Text(text = "Reset Password / पासवर्ड रीसेट", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it },
            label = { Text("Registered Mobile Number") },
            prefix = { Text("+91 ") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )

          Button(
            onClick = { onSwitchMode(AuthScreenMode.OTP_VERIFY) },
            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue800),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Send OTP to Reset")
          }

          TextButton(onClick = { onSwitchMode(AuthScreenMode.LOGIN) }) {
            Text("Back to Login")
          }
        }
      }
    }
  }
}
