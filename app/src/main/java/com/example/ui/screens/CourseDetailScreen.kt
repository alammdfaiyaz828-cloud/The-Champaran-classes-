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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CourseEntity
import com.example.data.local.LectureEntity
import com.example.data.local.PdfNoteEntity
import com.example.model.Language
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
  course: CourseEntity,
  currentLanguage: Language,
  lectures: List<LectureEntity>,
  pdfNotes: List<PdfNoteEntity>,
  onClose: () -> Unit,
  onPlayLecture: (LectureEntity) -> Unit,
  onOpenPdf: (PdfNoteEntity) -> Unit,
  onToggleDownloadPdf: (PdfNoteEntity) -> Unit,
  onEnroll: (CourseEntity) -> Unit
) {
  var selectedTabIdx by remember { mutableStateOf(0) } // 0: Lectures, 1: PDF Notes, 2: Course Info

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (currentLanguage == Language.URDU) course.titleUrdu else course.titleHindi,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        },
        navigationIcon = {
          IconButton(onClick = onClose, modifier = Modifier.testTag("course_detail_back")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = RoyalBlue800,
          titleContentColor = Color.White
        )
      )
    },
    bottomBar = {
      if (!course.isEnrolled) {
        Surface(
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 8.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Course Fee", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = "₹${course.price}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)
                Text(
                  text = "₹${course.originalPrice}",
                  fontSize = 13.sp,
                  style = MaterialTheme.typography.bodySmall.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                )
              }
            }

            Button(
              onClick = { onEnroll(course) },
              colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("detail_enroll_btn")
            ) {
              Text("Enroll Now / प्रवेश लें", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // 1. Course Header Overview
      Surface(
        color = RoyalBlue800.copy(alpha = 0.06f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = RoyalBlue800,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = course.subject,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            if (course.isEnrolled) {
              Surface(
                color = EmeraldLive.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "✓ Enrolled",
                  color = EmeraldLive,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }

          Text(
            text = if (currentLanguage == Language.URDU) course.titleUrdu else course.titleHindi,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )

          Text(
            text = if (currentLanguage == Language.URDU) course.descriptionUrdu else course.descriptionHindi,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // 2. Tab Navigation (Lectures / PDF Notes)
      TabRow(
        selectedTabIndex = selectedTabIdx,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = RoyalBlue800
      ) {
        Tab(
          selected = selectedTabIdx == 0,
          onClick = { selectedTabIdx = 0 },
          text = { Text("Video Lectures (${lectures.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
        )
        Tab(
          selected = selectedTabIdx == 1,
          onClick = { selectedTabIdx = 1 },
          text = { Text("PDF Notes (${pdfNotes.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
        )
      }

      // 3. Tab Contents
      if (selectedTabIdx == 0) {
        // Video Lectures List
        if (lectures.isEmpty()) {
          Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("No recorded lectures available yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        } else {
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(lectures) { lecture ->
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onPlayLecture(lecture) }
                  .testTag("lecture_item_${lecture.id}")
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(44.dp)
                      .clip(CircleShape)
                      .background(if (lecture.isCompleted) EmeraldLive.copy(alpha = 0.15f) else RoyalBlue800.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = if (lecture.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                      contentDescription = null,
                      tint = if (lecture.isCompleted) EmeraldLive else RoyalBlue800
                    )
                  }

                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = if (currentLanguage == Language.URDU) lecture.chapterNameUrdu else lecture.chapterNameHindi,
                      color = RoyalBlue800,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = if (currentLanguage == Language.URDU) lecture.titleUrdu else lecture.titleHindi,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 13.sp,
                      maxLines = 2
                    )
                    Text(
                      text = "${lecture.durationMinutes} mins • High Quality Digital Class",
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }

                  IconButton(onClick = { onPlayLecture(lecture) }) {
                    Icon(Icons.Default.PlayCircleFilled, contentDescription = "Play", tint = OrangeAccent)
                  }
                }
              }
            }
          }
        }
      } else {
        // PDF Notes List
        if (pdfNotes.isEmpty()) {
          Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("No PDF notes uploaded yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        } else {
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(pdfNotes) { note ->
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onOpenPdf(note) }
                  .testTag("pdf_item_${note.id}")
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(44.dp)
                      .clip(CircleShape)
                      .background(OrangeAccent.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = OrangeAccent)
                  }

                  Column(modifier = Modifier.weight(1f)) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Surface(
                        color = if (note.language == "ur") Color(0xFF047857) else RoyalBlue800,
                        shape = RoundedCornerShape(3.dp)
                      ) {
                        Text(
                          text = if (note.language == "ur") "URDU" else "HINDI",
                          color = Color.White,
                          fontSize = 8.sp,
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                      }
                      Text(
                        text = if (currentLanguage == Language.URDU) note.chapterNameUrdu else note.chapterNameHindi,
                        color = RoyalBlue800,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                    Text(
                      text = if (currentLanguage == Language.URDU) note.titleUrdu else note.titleHindi,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 13.sp,
                      maxLines = 2
                    )
                    Text(
                      text = "2.8 MB • Chapter Hand-written PDF Notes",
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }

                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                      onClick = { onToggleDownloadPdf(note) },
                      modifier = Modifier.testTag("download_note_btn_${note.id}")
                    ) {
                      Icon(
                        imageVector = if (note.isDownloaded) Icons.Default.DownloadDone else Icons.Outlined.FileDownload,
                        contentDescription = "Download",
                        tint = if (note.isDownloaded) EmeraldLive else MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                    IconButton(
                      onClick = { onOpenPdf(note) },
                      modifier = Modifier.testTag("read_note_btn_${note.id}")
                    ) {
                      Icon(Icons.Default.MenuBook, contentDescription = "Read", tint = RoyalBlue800)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
