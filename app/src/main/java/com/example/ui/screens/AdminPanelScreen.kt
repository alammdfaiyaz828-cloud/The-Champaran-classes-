package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CourseEntity
import com.example.model.AcademicClass
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
  courses: List<CourseEntity>,
  onClose: () -> Unit,
  onAddCourse: (titleHindi: String, titleUrdu: String, cls: String, subject: String, desc: String, price: Int) -> Unit,
  onScheduleLive: (title: String, subject: String, cls: String, teacher: String, time: String, topic: String) -> Unit,
  onUploadLecture: (courseId: Long, title: String, chapter: String, subject: String, duration: Int, url: String) -> Unit,
  onUploadNote: (courseId: Long, title: String, chapter: String, subject: String, lang: String, content: String) -> Unit,
  onSendNotification: (title: String, message: String) -> Unit,
  onDeleteCourse: (Long) -> Unit
) {
  var showAddCourseDialog by remember { mutableStateOf(false) }
  var showScheduleLiveDialog by remember { mutableStateOf(false) }
  var showUploadLectureDialog by remember { mutableStateOf(false) }
  var showUploadNoteDialog by remember { mutableStateOf(false) }
  var showSendNotificationDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Admin Dashboard / एडमिन पैनल", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        navigationIcon = {
          IconButton(onClick = onClose, modifier = Modifier.testTag("admin_close_btn")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color(0xFFEF4444),
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
      // 1. Stats Overview
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = RoyalBlue800.copy(alpha = 0.1f),
            modifier = Modifier.weight(1f)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(text = "Total Batches", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "${courses.size}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = RoyalBlue800)
            }
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = EmeraldLive.copy(alpha = 0.1f),
            modifier = Modifier.weight(1f)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(text = "Registered Students", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "4,850+", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = EmeraldLive)
            }
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = OrangeAccent.copy(alpha = 0.1f),
            modifier = Modifier.weight(1f)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(text = "Live Attendance", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "1,420", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)
            }
          }
        }
      }

      // 2. Admin Quick Actions Grid
      item {
        Text(text = "Admin Management Tools:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      }

      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = { showAddCourseDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue800),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f).testTag("admin_add_course_btn")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Batch", fontSize = 12.sp)
            }

            Button(
              onClick = { showScheduleLiveDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f).testTag("admin_schedule_live_btn")
            ) {
              Icon(Icons.Default.LiveTv, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Schedule Live", fontSize = 12.sp)
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { showUploadLectureDialog = true },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Upload Video", fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = { showUploadNoteDialog = true },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Upload PDF", fontSize = 12.sp)
            }
          }

          OutlinedButton(
            onClick = { showSendNotificationDialog = true },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Send Broadcast Push Notification to Students")
          }
        }
      }

      // 3. Current Courses List with Delete Option
      item {
        Text(text = "Manage Active Courses (${courses.size}):", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      }

      items(courses) { c ->
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(text = c.titleHindi, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text(text = "${c.subject} • ₹${c.price} • ${c.classLevel}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            IconButton(onClick = { onDeleteCourse(c.id) }) {
              Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
          }
        }
      }
    }
  }

  // Add Course Modal Dialog
  if (showAddCourseDialog) {
    var titleHi by remember { mutableStateOf("") }
    var titleUr by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("999") }
    var selectedCls by remember { mutableStateOf(AcademicClass.CLASS_10) }

    AlertDialog(
      onDismissRequest = { showAddCourseDialog = false },
      title = { Text("Add New Bihar Board Course") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = titleHi,
            onValueChange = { titleHi = it },
            label = { Text("Course Title (Hindi)") },
            singleLine = true
          )
          OutlinedTextField(
            value = titleUr,
            onValueChange = { titleUr = it },
            label = { Text("Course Title (Urdu)") },
            singleLine = true
          )
          OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject (e.g. History, Math, Science)") },
            singleLine = true
          )
          OutlinedTextField(
            value = priceStr,
            onValueChange = { priceStr = it },
            label = { Text("Price (INR ₹)") },
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (titleHi.isNotBlank() && subject.isNotBlank()) {
              onAddCourse(
                titleHi,
                if (titleUr.isNotBlank()) titleUr else titleHi,
                selectedCls.id,
                subject,
                "बिहार बोर्ड विशेष ऑनलाइन कक्षाएं एवं नोट्स",
                priceStr.toIntOrNull() ?: 999
              )
              showAddCourseDialog = false
            }
          }
        ) {
          Text("Create Course")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddCourseDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Schedule Live Class Modal Dialog
  if (showScheduleLiveDialog) {
    var liveTitle by remember { mutableStateOf("") }
    var liveSub by remember { mutableStateOf("") }
    var liveTeacher by remember { mutableStateOf("प्रोफेसर आर. के. सिन्हा") }
    var liveTime by remember { mutableStateOf("07:30 PM Today") }

    AlertDialog(
      onDismissRequest = { showScheduleLiveDialog = false },
      title = { Text("Schedule Live Class") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = liveTitle,
            onValueChange = { liveTitle = it },
            label = { Text("Live Session Title") },
            singleLine = true
          )
          OutlinedTextField(
            value = liveSub,
            onValueChange = { liveSub = it },
            label = { Text("Subject") },
            singleLine = true
          )
          OutlinedTextField(
            value = liveTeacher,
            onValueChange = { liveTeacher = it },
            label = { Text("Teacher Name") },
            singleLine = true
          )
          OutlinedTextField(
            value = liveTime,
            onValueChange = { liveTime = it },
            label = { Text("Scheduled Time") },
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (liveTitle.isNotBlank()) {
              onScheduleLive(liveTitle, liveSub, "class_12_arts", liveTeacher, liveTime, "Live discussion on high yield topics")
              showScheduleLiveDialog = false
            }
          }
        ) {
          Text("Schedule")
        }
      },
      dismissButton = {
        TextButton(onClick = { showScheduleLiveDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Upload Video Lesson Modal Dialog
  if (showUploadLectureDialog) {
    var vidTitle by remember { mutableStateOf("") }
    var chapter by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var durationStr by remember { mutableStateOf("45") }

    AlertDialog(
      onDismissRequest = { showUploadLectureDialog = false },
      title = { Text("Upload Recorded Lecture") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = vidTitle,
            onValueChange = { vidTitle = it },
            label = { Text("Lecture Title") },
            singleLine = true
          )
          OutlinedTextField(
            value = chapter,
            onValueChange = { chapter = it },
            label = { Text("Chapter Name") },
            singleLine = true
          )
          OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject") },
            singleLine = true
          )
          OutlinedTextField(
            value = durationStr,
            onValueChange = { durationStr = it },
            label = { Text("Duration (Mins)") },
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (vidTitle.isNotBlank()) {
              val targetCourseId = courses.firstOrNull()?.id ?: 1L
              onUploadLecture(targetCourseId, vidTitle, chapter, subject, durationStr.toIntOrNull() ?: 45, "https://example.com/stream.mp4")
              showUploadLectureDialog = false
            }
          }
        ) {
          Text("Upload")
        }
      },
      dismissButton = {
        TextButton(onClick = { showUploadLectureDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Upload PDF Note Modal Dialog
  if (showUploadNoteDialog) {
    var noteTitle by remember { mutableStateOf("") }
    var chapter by remember { mutableStateOf("") }
    var lang by remember { mutableStateOf("hi") }
    var content by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showUploadNoteDialog = false },
      title = { Text("Upload PDF Study Notes") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = noteTitle,
            onValueChange = { noteTitle = it },
            label = { Text("Note Title") },
            singleLine = true
          )
          OutlinedTextField(
            value = chapter,
            onValueChange = { chapter = it },
            label = { Text("Chapter") },
            singleLine = true
          )
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = lang == "hi", onClick = { lang = "hi" }, label = { Text("Hindi") })
            FilterChip(selected = lang == "ur", onClick = { lang = "ur" }, label = { Text("Urdu") })
          }
          OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Key Points & Study Content") },
            maxLines = 4
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (noteTitle.isNotBlank()) {
              val targetCourseId = courses.firstOrNull()?.id ?: 1L
              onUploadNote(targetCourseId, noteTitle, chapter, "General", lang, if (content.isNotBlank()) content else "अध्यायवार अध्ययन सामग्री - द चंपारण क्लासेज")
              showUploadNoteDialog = false
            }
          }
        ) {
          Text("Save Notes")
        }
      },
      dismissButton = {
        TextButton(onClick = { showUploadNoteDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Send Broadcast Notification Dialog
  if (showSendNotificationDialog) {
    var notifTitle by remember { mutableStateOf("") }
    var notifMsg by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showSendNotificationDialog = false },
      title = { Text("Send Push Notification") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = notifTitle,
            onValueChange = { notifTitle = it },
            label = { Text("Notification Title") },
            singleLine = true
          )
          OutlinedTextField(
            value = notifMsg,
            onValueChange = { notifMsg = it },
            label = { Text("Message to Students") },
            maxLines = 3
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (notifTitle.isNotBlank() && notifMsg.isNotBlank()) {
              onSendNotification(notifTitle, notifMsg)
              showSendNotificationDialog = false
            }
          }
        ) {
          Text("Broadcast")
        }
      },
      dismissButton = {
        TextButton(onClick = { showSendNotificationDialog = false }) { Text("Cancel") }
      }
    )
  }
}
