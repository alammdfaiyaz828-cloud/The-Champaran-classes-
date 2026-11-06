package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CourseEntity
import com.example.model.AcademicClass
import com.example.model.Language
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
  courses: List<CourseEntity>,
  currentLanguage: Language,
  selectedClassFilter: AcademicClass?,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  onSelectFilter: (AcademicClass?) -> Unit,
  onSelectCourse: (CourseEntity) -> Unit,
  onEnrollCourse: (CourseEntity) -> Unit
) {
  val filteredCourses = remember(courses, selectedClassFilter, searchQuery) {
    courses.filter { course ->
      val matchesClass = selectedClassFilter == null || course.classLevel == selectedClassFilter.id
      val matchesSearch = searchQuery.isBlank() ||
        course.titleHindi.contains(searchQuery, ignoreCase = true) ||
        course.titleUrdu.contains(searchQuery, ignoreCase = true) ||
        course.subject.contains(searchQuery, ignoreCase = true)
      matchesClass && matchesSearch
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("courses_screen_root")
  ) {
    // 1. Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchChange,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .testTag("courses_search_bar"),
      placeholder = { Text("Search batches, subjects (गणित, इतिहास, Urdu)...", fontSize = 13.sp) },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = RoyalBlue800) },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { onSearchChange("") }) {
            Icon(Icons.Default.Clear, contentDescription = "Clear")
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(12.dp)
    )

    // 2. Class Filter Chips Row
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        FilterChip(
          selected = selectedClassFilter == null,
          onClick = { onSelectFilter(null) },
          label = { Text("All Classes / सभी") },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = RoyalBlue800,
            selectedLabelColor = Color.White
          ),
          modifier = Modifier.testTag("filter_class_all")
        )
      }
      items(AcademicClass.values()) { cls ->
        val isSelected = selectedClassFilter == cls
        FilterChip(
          selected = isSelected,
          onClick = { onSelectFilter(if (isSelected) null else cls) },
          label = {
            Text(
              text = when (cls) {
                AcademicClass.CLASS_10 -> "10th Matric"
                AcademicClass.CLASS_11_ARTS -> "11th Arts"
                AcademicClass.CLASS_12_ARTS -> "12th Arts"
              }
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = RoyalBlue800,
            selectedLabelColor = Color.White
          ),
          modifier = Modifier.testTag("filter_class_${cls.id}")
        )
      }
    }

    // 3. Courses List
    if (filteredCourses.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.SearchOff, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("No courses found matching criteria", fontWeight = FontWeight.Bold)
          TextButton(onClick = { onSearchChange(""); onSelectFilter(null) }) {
            Text("Reset Filters")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(filteredCourses) { course ->
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelectCourse(course) }
              .testTag("course_item_${course.id}")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
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
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = if (currentLanguage == Language.URDU) course.titleUrdu else course.titleHindi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
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
                    Text(text = "₹${course.price}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = OrangeAccent)
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

              Spacer(modifier = Modifier.height(12.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = RoyalBlue800, modifier = Modifier.size(16.dp))
                    Text("${course.totalLectures} Lessons", fontSize = 11.sp)
                  }
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(16.dp))
                    Text("${course.totalPdfs} Notes", fontSize = 11.sp)
                  }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  OutlinedButton(
                    onClick = { onSelectCourse(course) },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                  ) {
                    Text("Syllabus", fontSize = 12.sp)
                  }

                  if (!course.isEnrolled) {
                    Button(
                      onClick = { onEnrollCourse(course) },
                      colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                      shape = RoundedCornerShape(8.dp),
                      contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                      modifier = Modifier.testTag("enroll_btn_${course.id}")
                    ) {
                      Text("Buy Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                  } else {
                    Button(
                      onClick = { onSelectCourse(course) },
                      colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue800),
                      shape = RoundedCornerShape(8.dp),
                      contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                      Text("Study Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
