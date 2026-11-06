package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PdfNoteEntity
import com.example.model.Language
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfReaderScreen(
  note: PdfNoteEntity,
  currentLanguage: Language,
  onClose: () -> Unit,
  onToggleDownload: (PdfNoteEntity) -> Unit
) {
  var fontSizeSp by remember { mutableStateOf(14) }
  var isDarkReadingMode by remember { mutableStateOf(false) }
  var isDownloading by remember { mutableStateOf(false) }

  val readingBg = if (isDarkReadingMode) Color(0xFF1E293B) else Color(0xFFFFFBEB) // Light warm parchment or dark slate
  val readingText = if (isDarkReadingMode) Color(0xFFF8FAFC) else Color(0xFF1E293B)

  val isUrdu = note.language == "ur" || currentLanguage == Language.URDU

  CompositionLocalProvider(
    LocalLayoutDirection provides (if (isUrdu) LayoutDirection.Rtl else LayoutDirection.Ltr)
  ) {
    Scaffold(
      modifier = Modifier
        .fillMaxSize()
        .testTag("pdf_reader_screen"),
      topBar = {
        TopAppBar(
          title = {
            Column {
              Text(
                text = if (isUrdu) note.titleUrdu else note.titleHindi,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
              )
              Text(
                text = "${note.subject} • PDF Study Material",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.8f)
              )
            }
          },
          navigationIcon = {
            IconButton(onClick = onClose, modifier = Modifier.testTag("pdf_close_btn")) {
              Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
          },
          actions = {
            // Text size zoom
            IconButton(
              onClick = { if (fontSizeSp > 11) fontSizeSp -= 2 }
            ) {
              Icon(Icons.Default.TextDecrease, contentDescription = "Smaller", tint = Color.White)
            }
            IconButton(
              onClick = { if (fontSizeSp < 22) fontSizeSp += 2 }
            ) {
              Icon(Icons.Default.TextIncrease, contentDescription = "Larger", tint = Color.White)
            }

            // Dark reading mode toggle
            IconButton(
              onClick = { isDarkReadingMode = !isDarkReadingMode }
            ) {
              Icon(
                imageVector = if (isDarkReadingMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = "Reading Theme",
                tint = Color.White
              )
            }

            // Download Button
            IconButton(
              onClick = {
                onToggleDownload(note)
              },
              modifier = Modifier.testTag("pdf_download_toggle_btn")
            ) {
              Icon(
                imageVector = if (note.isDownloaded) Icons.Default.DownloadDone else Icons.Outlined.FileDownload,
                contentDescription = "Download",
                tint = if (note.isDownloaded) Color(0xFF10B981) else Color.White
              )
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RoyalBlue800,
            titleContentColor = Color.White
          )
        )
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .background(readingBg)
      ) {
        // Download status bar
        if (note.isDownloaded) {
          Surface(
            color = Color(0xFF10B981).copy(alpha = 0.15f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "उपलब्ध ऑफ़लाइन (2.8 MB) / دستیاب آف لائن",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF047857)
              )
            }
          }
        }

        // PDF Content Scrollable Viewer
        Box(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = readingBg,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              // Watermark / Header
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "THE CHAMPARAN CLASSES",
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 12.sp,
                  color = OrangeAccent
                )
                Text(
                  text = "BIHAR BOARD SPECIAL",
                  fontSize = 10.sp,
                  color = RoyalBlue800,
                  fontWeight = FontWeight.Bold
                )
              }

              Divider(color = readingText.copy(alpha = 0.15f))

              Text(
                text = note.contentMarkdown,
                fontSize = fontSizeSp.sp,
                lineHeight = (fontSizeSp * 1.5).sp,
                color = readingText
              )

              Spacer(modifier = Modifier.height(24.dp))

              // End of Note Watermark
              Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "— द चंपारण क्लासेज • Learn Today, Lead Tomorrow —",
                  fontSize = 11.sp,
                  color = readingText.copy(alpha = 0.6f),
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }
    }
  }
}
