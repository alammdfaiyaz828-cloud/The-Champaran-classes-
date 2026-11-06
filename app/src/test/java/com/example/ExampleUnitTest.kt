package com.example

import com.example.data.local.InitialData
import com.example.model.AcademicClass
import com.example.model.AppStrings
import com.example.model.Language
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testAppStrings() {
    val hiTitle = AppStrings.get("app_name", Language.HINDI)
    val urTitle = AppStrings.get("app_name", Language.URDU)
    val enTitle = AppStrings.get("app_name", Language.ENGLISH)

    assertEquals("द चंपारण क्लासेज", hiTitle)
    assertEquals("دی چمپارن کلاسز", urTitle)
    assertEquals("The Champaran Classes", enTitle)
  }

  @Test
  fun testInitialCourses() {
    val courses = InitialData.getCourses()
    assertTrue(courses.isNotEmpty())
    val class10Course = courses.find { it.classLevel == AcademicClass.CLASS_10.id }
    assertNotNull(class10Course)
  }

  @Test
  fun testInitialQuestions() {
    val questions = InitialData.getQuestions()
    assertTrue(questions.isNotEmpty())
    questions.forEach { q ->
      assertTrue(q.correctOptionIndex in 0..3)
      assertTrue(q.questionHindi.isNotBlank())
    }
  }
}
