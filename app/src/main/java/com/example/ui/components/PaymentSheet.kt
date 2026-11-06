package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CourseEntity
import com.example.model.Language
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBottomSheet(
  course: CourseEntity,
  currentLanguage: Language,
  onDismiss: () -> Unit,
  onPaymentSuccess: (paymentMethod: String) -> Unit
) {
  var selectedMethod by remember { mutableStateOf("UPI (Google Pay / PhonePe)") }
  var isProcessing by remember { mutableStateOf(false) }
  var isSuccess by remember { mutableStateOf(false) }
  var generatedTxnId by remember { mutableStateOf("") }
  val scope = rememberCoroutineScope()

  ModalBottomSheet(
    onDismissRequest = { if (!isProcessing) onDismiss() },
    modifier = Modifier.testTag("payment_bottom_sheet"),
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .padding(bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      if (isSuccess) {
        // Payment Success Receipt View
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .background(EmeraldLive.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              Icons.Default.CheckCircle,
              contentDescription = "Success",
              tint = EmeraldLive,
              modifier = Modifier.size(52.dp)
            )
          }

          Text(
            text = "प्रवेश सफल हुआ! / داخلہ کامیاب",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = RoyalBlue800
          )

          Text(
            text = "Payment of ₹${course.price} received via $selectedMethod",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(text = "Transaction ID: $generatedTxnId", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Text(text = "Course: ${course.titleHindi}", fontSize = 12.sp)
              Text(text = "Status: ACTIVE • Instant Access Unlocked", fontSize = 11.sp, color = EmeraldLive, fontWeight = FontWeight.Bold)
            }
          }

          Button(
            onClick = {
              onPaymentSuccess(selectedMethod)
            },
            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue800),
            modifier = Modifier.fillMaxWidth().testTag("payment_done_btn"),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Go to My Course / पढ़ाई शुरू करें")
          }
        }
      } else {
        // Payment Checkout Form
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldLive, modifier = Modifier.size(18.dp))
            Text(
              text = "Razorpay Secure Checkout",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = EmeraldLive
            )
          }
          IconButton(onClick = onDismiss, enabled = !isProcessing) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        // Course Summary Card
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = course.titleHindi,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = RoyalBlue800
            )
            Text(
              text = "Target: ${course.classLevel.replace("_", " ").uppercase()} • Full Syllabus Access",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = "₹${course.price}",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = OrangeAccent
                )
                Text(
                  text = "₹${course.originalPrice}",
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  style = MaterialTheme.typography.bodySmall.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                )
              }
              Surface(
                color = OrangeAccent.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "Save ₹${course.originalPrice - course.price}",
                  color = OrangeAccent,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }
        }

        Text(
          text = "Select Payment Method / भुगतान विकल्प चुनें:",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )

        // Payment Options List
        val paymentOptions = listOf(
          "UPI (Google Pay / PhonePe / Paytm)" to Icons.Default.QrCodeScanner,
          "Debit Card / Credit Card" to Icons.Default.CreditCard,
          "Net Banking (All Indian Banks)" to Icons.Default.AccountBalance,
          "Pay Later / EMI" to Icons.Default.Payments
        )

        paymentOptions.forEach { (method, icon) ->
          val isSelected = selectedMethod == method
          Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(
              width = if (isSelected) 2.dp else 1.dp,
              color = if (isSelected) RoyalBlue800 else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            ),
            color = if (isSelected) RoyalBlue800.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface,
            modifier = Modifier
              .fillMaxWidth()
              .clickable(enabled = !isProcessing) { selectedMethod = method }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) RoyalBlue800 else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
              )
              Text(
                text = method,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
              )
              RadioButton(
                selected = isSelected,
                onClick = { selectedMethod = method },
                colors = RadioButtonDefaults.colors(selectedColor = RoyalBlue800)
              )
            }
          }
        }

        // Pay Button
        Button(
          onClick = {
            isProcessing = true
            scope.launch {
              delay(1600) // Simulated secure Razorpay authorization handshake
              generatedTxnId = "RZP_TXN_" + System.currentTimeMillis().toString().takeLast(7)
              isProcessing = false
              isSuccess = true
            }
          },
          enabled = !isProcessing,
          colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("pay_now_btn"),
          shape = RoundedCornerShape(8.dp)
        ) {
          if (isProcessing) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Verifying Payment with Razorpay...", color = Color.White, fontWeight = FontWeight.Bold)
          } else {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Pay ₹${course.price} Securely", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }
        }

        Text(
          text = "100% Refundable within 7 days if not satisfied • 256-bit SSL encrypted",
          fontSize = 10.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.align(Alignment.CenterHorizontally)
        )
      }
    }
  }
}
