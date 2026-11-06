package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val name: String,
  val mobile: String,
  val email: String,
  val selectedClass: String, // class_10, class_11_arts, class_12_arts
  val preferredLanguage: String, // hi, urdu, en
  val role: String = "STUDENT" // STUDENT, TEACHER, ADMIN
)

@Entity(tableName = "courses")
data class CourseEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val titleHindi: String,
  val titleUrdu: String,
  val classLevel: String, // class_10, class_11_arts, class_12_arts
  val subject: String,
  val descriptionHindi: String,
  val descriptionUrdu: String,
  val price: Int, // in INR
  val originalPrice: Int,
  val rating: Float = 4.9f,
  val totalLectures: Int = 45,
  val totalPdfs: Int = 30,
  val isEnrolled: Boolean = false,
  val progressPercent: Int = 0
)

@Entity(tableName = "lectures")
data class LectureEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val courseId: Long,
  val titleHindi: String,
  val titleUrdu: String,
  val chapterNameHindi: String,
  val chapterNameUrdu: String,
  val subject: String,
  val durationMinutes: Int,
  val videoUrl: String,
  val lastPositionSeconds: Int = 0,
  val isCompleted: Boolean = false,
  val isFavorite: Boolean = false
)

@Entity(tableName = "pdf_notes")
data class PdfNoteEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val courseId: Long,
  val titleHindi: String,
  val titleUrdu: String,
  val chapterNameHindi: String,
  val chapterNameUrdu: String,
  val subject: String,
  val language: String, // "hi" or "ur"
  val fileSizeBytes: Long = 2500000L, // ~2.5 MB
  val isDownloaded: Boolean = false,
  val contentMarkdown: String // Rich structured readable summary & notes
)

@Entity(tableName = "live_classes")
data class LiveClassEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val titleHindi: String,
  val titleUrdu: String,
  val subject: String,
  val classLevel: String,
  val teacherName: String,
  val scheduledTime: String,
  val isLiveNow: Boolean = false,
  val viewerCount: Int = 0,
  val streamTopic: String = ""
)

@Entity(tableName = "tests")
data class TestEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val courseId: Long,
  val titleHindi: String,
  val titleUrdu: String,
  val subject: String,
  val classLevel: String,
  val durationMinutes: Int = 30,
  val totalQuestions: Int = 10,
  val totalMarks: Int = 50,
  val passingMarks: Int = 20
)

@Entity(tableName = "questions")
data class QuestionEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val testId: Long,
  val questionNumber: Int,
  val questionHindi: String,
  val questionUrdu: String,
  val optionAHindi: String,
  val optionAUrdu: String,
  val optionBHindi: String,
  val optionBUrdu: String,
  val optionCHindi: String,
  val optionCUrdu: String,
  val optionDHindi: String,
  val optionDUrdu: String,
  val correctOptionIndex: Int, // 0 for A, 1 for B, 2 for C, 3 for D
  val explanationHindi: String,
  val explanationUrdu: String
)

@Entity(tableName = "test_attempts")
data class TestAttemptEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val testId: Long,
  val testTitleHindi: String,
  val score: Int,
  val totalMarks: Int,
  val correctAnswers: Int,
  val wrongAnswers: Int,
  val unattempted: Int,
  val accuracyPercent: Int,
  val completedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "payments")
data class PaymentEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val transactionId: String,
  val courseId: Long,
  val courseTitle: String,
  val amount: Int,
  val paymentMethod: String, // UPI, Card, NetBanking
  val status: String = "SUCCESS", // SUCCESS, PENDING, REFUNDED
  val timestampMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val message: String,
  val timestamp: String,
  val isRead: Boolean = false
)
