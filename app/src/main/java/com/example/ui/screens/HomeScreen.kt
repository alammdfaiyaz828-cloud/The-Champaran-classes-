package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.CourseEntity
import com.example.data.local.LectureEntity
import com.example.data.local.LiveClassEntity
import com.example.model.*
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@Composable
fun HomeScreen(
  userSession: UserSession,
  currentLanguage: Language,
  enrolledCourses: List<CourseEntity>,
  popularCourses: List<CourseEntity>,
  continueWatching: List<LectureEntity>,
  activeLiveClass: LiveClassEntity?,
  onSelectCourse: (CourseEntity) -> Unit,
  onPlayLecture: (LectureEntity) -> Unit,
  onJoinLiveClass: (LiveClassEntity) -> Unit,
  onNavigateToTab: (NavTab) -> Unit,
  onSelectClassFilter: (AcademicClass) -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.5f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("home_screen_scroll"),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Hero Study Banner with Image Asset
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(RoyalBlue800)
          .testTag("home_hero_banner")
      ) {
        // Hero Background Image
        Image(
          painter = painterResource(id = R.drawable.hero_study_banner),
          contentDescription = "Bihar Board Study Classroom",
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
          contentScale = ContentScale.Crop
        )

        // Gradient Overlay
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color(0xFF0F172A).copy(alpha = 0.88f))
              )
            )
        )

        // Banner Text Content
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Surface(
            color = OrangeAccent,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = "बिहार बोर्ड 2026-27 • BIHAR BOARD SPECIAL",
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Text(
            text = AppStrings.get("tagline", currentLanguage),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
          )
          Text(
            text = "Welcome, ${userSession.name} • ${userSession.selectedClass.titleEnglish}",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 12.sp
          )
        }
      }
    }

    // 2. Active Live Class Alert Banner (if live)
    if (activeLiveClass != null) {
      item {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = OrangeAccent.copy(alpha = 0.08f),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, OrangeAccent),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onJoinLiveClass(activeLiveClass) }
            .testTag("home_live_alert_banner")
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
                .background(Color.Red.copy(alpha = pulseAlpha)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                Icons.Default.LiveTv,
                contentDescription = "Live",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                  color = Color.Red,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "LIVE NOW",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
                Text(
                  text = "${activeLiveClass.viewerCount.coerceAtLeast(1400)} Watching",
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = if (currentLanguage == Language.URDU) activeLiveClass.titleUrdu else activeLiveClass.titleHindi,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = activeLiveClass.teacherName,
                fontSize = 11.sp,
                color = RoyalBlue800
              )
            }

            Button(
              onClick = { onJoinLiveClass(activeLiveClass) },
              colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text("Join / जुड़ें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 3. Quick Academic Category Switcher (Class 10th, 11th Arts, 12th Arts)
    item {
      Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "Select Academic Class / कक्षा चुनें:",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          AcademicClass.values().forEach { cls ->
            val isSelected = userSession.selectedClass == cls
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) RoyalBlue800 else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .weight(1f)
                .clickable {
                  onSelectClassFilter(cls)
                  onNavigateToTab(NavTab.COURSES)
                }
                .testTag("home_class_chip_${cls.id}")
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = when (cls) {
                    AcademicClass.CLASS_10 -> "Class 10th"
                    AcademicClass.CLASS_11_ARTS -> "11th Arts"
                    AcademicClass.CLASS_12_ARTS -> "12th Arts"
                  },
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = when (cls) {
                    AcademicClass.CLASS_10 -> "मैट्रिक / میٹرک"
                    AcademicClass.CLASS_11_ARTS -> "इण्टर / گیارہویں"
                    AcademicClass.CLASS_12_ARTS -> "इण्टर / بارہویں"
                  },
                  fontSize = 10.sp,
                  color = if (isSelected) OrangeAccent else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // 4. Quick Action Hub (Live Classes, Recorded, PDF Notes, Test Series)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Quick Card 1: Live Classes
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToTab(NavTab.LIVE) }
            .testTag("quick_action_live")
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFEF4444).copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.LiveTv, contentDescription = null, tint = Color(0xFFEF4444))
            }
            Text(text = "Live Class", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = "लाइव क्लास", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        // Quick Card 2: PDF Notes
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToTab(NavTab.COURSES) }
            .testTag("quick_action_notes")
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(RoyalBlue800.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = RoyalBlue800)
            }
            Text(text = "PDF Notes", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = "अध्यायवार नोट्स", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        // Quick Card 3: Online Tests
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToTab(NavTab.TESTS) }
            .testTag("quick_action_tests")
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(OrangeAccent.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Quiz, contentDescription = null, tint = OrangeAccent)
            }
            Text(text = "Test Series", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = "MCQ टेस्ट", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    // 5. Continue Learning Section (if lectures in progress)
    if (continueWatching.isNotEmpty()) {
      item {
        Column(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = AppStrings.get("continue_learning", currentLanguage),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )

          continueWatching.take(2).forEach { lecture ->
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surface,
              tonalElevation = 2.dp,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onPlayLecture(lecture) }
                .testTag("continue_lecture_${lecture.id}")
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(RoyalBlue800),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
                }

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = if (currentLanguage == Language.URDU) lecture.titleUrdu else lecture.titleHindi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = "${lecture.subject} • ${lecture.chapterNameHindi}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  LinearProgressIndicator(
                    progress = (lecture.lastPositionSeconds.toFloat() / (lecture.durationMinutes * 60f)).coerceIn(0.1f, 1f),
                    color = OrangeAccent,
                    modifier = Modifier.fillMaxWidth().height(4.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // 6. Enrolled Courses or Popular Bihar Board Batches
    item {
      Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = AppStrings.get("popular_courses", currentLanguage),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          TextButton(onClick = { onNavigateToTab(NavTab.COURSES) }) {
            Text(AppStrings.get("view_all", currentLanguage), color = RoyalBlue800, fontSize = 12.sp)
          }
        }

        popularCourses.forEach { course ->
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelectCourse(course) }
              .testTag("home_course_card_${course.id}")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Surface(
                    color = RoyalBlue800.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                  ) {
                    Text(
                      text = course.subject,
                      color = RoyalBlue800,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = if (currentLanguage == Language.URDU) course.titleUrdu else course.titleHindi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                  )
                }

                if (course.isEnrolled) {
                  Surface(
                    color = EmeraldLive.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Text(
                      text = "✓ Enrolled",
                      color = EmeraldLive,
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                  }
                } else {
                  Column(horizontalAlignment = Alignment.End) {
                    Text(text = "₹${course.price}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = OrangeAccent)
                    Text(
                      text = "₹${course.originalPrice}",
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      style = MaterialTheme.typography.bodySmall.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = if (currentLanguage == Language.URDU) course.descriptionUrdu else course.descriptionHindi,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
              )

              Spacer(modifier = Modifier.height(10.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.PlayCircle, contentDescription = null, tint = RoyalBlue800, modifier = Modifier.size(16.dp))
                    Text("${course.totalLectures} Videos", fontSize = 11.sp)
                  }
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(16.dp))
                    Text("${course.totalPdfs} PDFs", fontSize = 11.sp)
                  }
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                    Text("${course.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }
                }

                Button(
                  onClick = { onSelectCourse(course) },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (course.isEnrolled) RoyalBlue800 else OrangeAccent
                  ),
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = if (course.isEnrolled) "View / देखें" else "Enroll / प्रवेश लें",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
