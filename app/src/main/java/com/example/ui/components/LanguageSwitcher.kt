package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@Composable
fun LanguageSwitcherDialog(
  currentLanguage: Language,
  onSelectLanguage: (Language) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        imageVector = Icons.Default.Translate,
        contentDescription = "Language",
        tint = RoyalBlue800,
        modifier = Modifier.size(32.dp)
      )
    },
    title = {
      Text(
        text = "Select Language / भाषा चुनें / زبان منتخب کریں",
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Language.values().forEach { lang ->
          val isSelected = lang == currentLanguage
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) RoyalBlue800.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onSelectLanguage(lang)
                onDismiss()
              }
              .testTag("lang_option_${lang.code}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(
                  text = lang.displayName,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) RoyalBlue800 else MaterialTheme.colorScheme.onSurface,
                  fontSize = 15.sp
                )
                if (lang == Language.URDU) {
                  Text(
                    text = "بہار بورڈ کے طلباء کیلئے دائیں سے بائیں (RTL) لے آؤٹ",
                    fontSize = 11.sp,
                    color = OrangeAccent
                  )
                } else if (lang == Language.HINDI) {
                  Text(
                    text = "बिहार बोर्ड मैट्रिक और इण्टर आर्ट्स संपूर्ण पाठ्यक्रम",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Selected",
                  tint = RoyalBlue800
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close / बंद करें", color = RoyalBlue800)
      }
    }
  )
}
