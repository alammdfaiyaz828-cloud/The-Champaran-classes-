package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
  @Query("SELECT * FROM courses ORDER BY id ASC")
  fun getAllCourses(): Flow<List<CourseEntity>>

  @Query("SELECT * FROM courses WHERE classLevel = :classLevel ORDER BY id ASC")
  fun getCoursesByClass(classLevel: String): Flow<List<CourseEntity>>

  @Query("SELECT * FROM courses WHERE isEnrolled = 1")
  fun getEnrolledCourses(): Flow<List<CourseEntity>>

  @Query("SELECT * FROM courses WHERE id = :id")
  suspend fun getCourseById(id: Long): CourseEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCourse(course: CourseEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCourses(courses: List<CourseEntity>)

  @Update
  suspend fun updateCourse(course: CourseEntity)

  @Query("UPDATE courses SET isEnrolled = 1 WHERE id = :courseId")
  suspend fun markCourseEnrolled(courseId: Long)

  @Query("DELETE FROM courses WHERE id = :courseId")
  suspend fun deleteCourse(courseId: Long)
}

@Dao
interface LectureDao {
  @Query("SELECT * FROM lectures WHERE courseId = :courseId ORDER BY id ASC")
  fun getLecturesForCourse(courseId: Long): Flow<List<LectureEntity>>

  @Query("SELECT * FROM lectures WHERE isFavorite = 1")
  fun getFavoriteLectures(): Flow<List<LectureEntity>>

  @Query("SELECT * FROM lectures WHERE lastPositionSeconds > 0 ORDER BY id DESC LIMIT 5")
  fun getContinueWatchingLectures(): Flow<List<LectureEntity>>

  @Query("SELECT * FROM lectures WHERE id = :id")
  suspend fun getLectureById(id: Long): LectureEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLecture(lecture: LectureEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLectures(lectures: List<LectureEntity>)

  @Update
  suspend fun updateLecture(lecture: LectureEntity)

  @Query("UPDATE lectures SET lastPositionSeconds = :positionSeconds, isCompleted = :completed WHERE id = :id")
  suspend fun updatePlaybackProgress(id: Long, positionSeconds: Int, completed: Boolean)

  @Query("UPDATE lectures SET isFavorite = :isFavorite WHERE id = :id")
  suspend fun setFavorite(id: Long, isFavorite: Boolean)

  @Query("DELETE FROM lectures WHERE id = :id")
  suspend fun deleteLecture(id: Long)
}

@Dao
interface PdfNoteDao {
  @Query("SELECT * FROM pdf_notes WHERE courseId = :courseId ORDER BY id ASC")
  fun getNotesForCourse(courseId: Long): Flow<List<PdfNoteEntity>>

  @Query("SELECT * FROM pdf_notes WHERE isDownloaded = 1")
  fun getDownloadedNotes(): Flow<List<PdfNoteEntity>>

  @Query("SELECT * FROM pdf_notes WHERE id = :id")
  suspend fun getNoteById(id: Long): PdfNoteEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNote(note: PdfNoteEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotes(notes: List<PdfNoteEntity>)

  @Query("UPDATE pdf_notes SET isDownloaded = :downloaded WHERE id = :id")
  suspend fun updateDownloadStatus(id: Long, downloaded: Boolean)

  @Query("DELETE FROM pdf_notes WHERE id = :id")
  suspend fun deleteNote(id: Long)
}

@Dao
interface LiveClassDao {
  @Query("SELECT * FROM live_classes ORDER BY isLiveNow DESC, id ASC")
  fun getAllLiveClasses(): Flow<List<LiveClassEntity>>

  @Query("SELECT * FROM live_classes WHERE isLiveNow = 1 LIMIT 1")
  fun getActiveLiveClass(): Flow<LiveClassEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLiveClass(liveClass: LiveClassEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLiveClasses(liveClasses: List<LiveClassEntity>)

  @Update
  suspend fun updateLiveClass(liveClass: LiveClassEntity)

  @Query("UPDATE live_classes SET isLiveNow = :isLive, viewerCount = :viewerCount WHERE id = :id")
  suspend fun setLiveStatus(id: Long, isLive: Boolean, viewerCount: Int)

  @Query("DELETE FROM live_classes WHERE id = :id")
  suspend fun deleteLiveClass(id: Long)
}

@Dao
interface TestDao {
  @Query("SELECT * FROM tests WHERE classLevel = :classLevel OR :classLevel = '' ORDER BY id ASC")
  fun getTests(classLevel: String): Flow<List<TestEntity>>

  @Query("SELECT * FROM tests WHERE id = :id")
  suspend fun getTestById(id: Long): TestEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTest(test: TestEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTests(tests: List<TestEntity>)

  @Query("DELETE FROM tests WHERE id = :id")
  suspend fun deleteTest(id: Long)

  // Questions
  @Query("SELECT * FROM questions WHERE testId = :testId ORDER BY questionNumber ASC")
  suspend fun getQuestionsForTest(testId: Long): List<QuestionEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestions(questions: List<QuestionEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestion(question: QuestionEntity): Long

  // Attempts
  @Query("SELECT * FROM test_attempts ORDER BY completedAtMillis DESC")
  fun getAllAttempts(): Flow<List<TestAttemptEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAttempt(attempt: TestAttemptEntity): Long
}

@Dao
interface PaymentDao {
  @Query("SELECT * FROM payments ORDER BY timestampMillis DESC")
  fun getAllPayments(): Flow<List<PaymentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPayment(payment: PaymentEntity): Long
}

@Dao
interface NotificationDao {
  @Query("SELECT * FROM notifications ORDER BY id DESC")
  fun getAllNotifications(): Flow<List<NotificationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotification(notification: NotificationEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotifications(notifications: List<NotificationEntity>)

  @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
  suspend fun markAsRead(id: Long)
}

@Dao
interface UserDao {
  @Query("SELECT * FROM users LIMIT 1")
  suspend fun getCurrentUser(): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity): Long

  @Update
  suspend fun updateUser(user: UserEntity)
}
