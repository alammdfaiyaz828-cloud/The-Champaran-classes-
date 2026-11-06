package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class ChamparanRepository(private val db: AppDatabase) {

  private val courseDao = db.courseDao()
  private val lectureDao = db.lectureDao()
  private val pdfNoteDao = db.pdfNoteDao()
  private val liveClassDao = db.liveClassDao()
  private val testDao = db.testDao()
  private val paymentDao = db.paymentDao()
  private val notificationDao = db.notificationDao()
  private val userDao = db.userDao()

  suspend fun ensureDataPopulated() = withContext(Dispatchers.IO) {
    val existing = courseDao.getAllCourses().firstOrNull()
    if (existing.isNullOrEmpty()) {
      AppDatabase.populateDatabase(db)
    }
  }

  // Courses
  fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()
  fun getCoursesByClass(classLevel: String): Flow<List<CourseEntity>> = courseDao.getCoursesByClass(classLevel)
  fun getEnrolledCourses(): Flow<List<CourseEntity>> = courseDao.getEnrolledCourses()
  suspend fun getCourseById(id: Long) = withContext(Dispatchers.IO) { courseDao.getCourseById(id) }
  suspend fun insertCourse(course: CourseEntity) = withContext(Dispatchers.IO) { courseDao.insertCourse(course) }
  suspend fun deleteCourse(id: Long) = withContext(Dispatchers.IO) { courseDao.deleteCourse(id) }

  // Enroll & Payment
  suspend fun processCourseEnrollment(
    courseId: Long,
    courseTitle: String,
    amount: Int,
    paymentMethod: String
  ): PaymentEntity = withContext(Dispatchers.IO) {
    courseDao.markCourseEnrolled(courseId)
    val transaction = PaymentEntity(
      transactionId = "PAY_RZP_" + System.currentTimeMillis().toString().takeLast(8),
      courseId = courseId,
      courseTitle = courseTitle,
      amount = amount,
      paymentMethod = paymentMethod,
      status = "SUCCESS",
      timestampMillis = System.currentTimeMillis()
    )
    paymentDao.insertPayment(transaction)
    notificationDao.insertNotification(
      NotificationEntity(
        title = "प्रवेश सफल हुआ! / داخلہ کامیاب",
        message = "आपने '$courseTitle' में सफलतापूर्वक प्रवेश ले लिया है। अब आप सभी वीडियो और नोट्स देख सकते हैं।",
        timestamp = "अभी (Just Now)",
        isRead = false
      )
    )
    transaction
  }

  // Lectures
  fun getLecturesForCourse(courseId: Long): Flow<List<LectureEntity>> = lectureDao.getLecturesForCourse(courseId)
  fun getFavoriteLectures(): Flow<List<LectureEntity>> = lectureDao.getFavoriteLectures()
  fun getContinueWatchingLectures(): Flow<List<LectureEntity>> = lectureDao.getContinueWatchingLectures()
  suspend fun updatePlaybackProgress(id: Long, positionSecs: Int, completed: Boolean) = withContext(Dispatchers.IO) {
    lectureDao.updatePlaybackProgress(id, positionSecs, completed)
  }
  suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
    lectureDao.setFavorite(id, isFavorite)
  }
  suspend fun insertLecture(lecture: LectureEntity) = withContext(Dispatchers.IO) {
    lectureDao.insertLecture(lecture)
  }

  // PDF Notes
  fun getNotesForCourse(courseId: Long): Flow<List<PdfNoteEntity>> = pdfNoteDao.getNotesForCourse(courseId)
  fun getDownloadedNotes(): Flow<List<PdfNoteEntity>> = pdfNoteDao.getDownloadedNotes()
  suspend fun toggleDownloadNote(id: Long, downloaded: Boolean) = withContext(Dispatchers.IO) {
    pdfNoteDao.updateDownloadStatus(id, downloaded)
  }
  suspend fun insertPdfNote(note: PdfNoteEntity) = withContext(Dispatchers.IO) {
    pdfNoteDao.insertNote(note)
  }

  // Live Classes
  fun getAllLiveClasses(): Flow<List<LiveClassEntity>> = liveClassDao.getAllLiveClasses()
  fun getActiveLiveClass(): Flow<LiveClassEntity?> = liveClassDao.getActiveLiveClass()
  suspend fun insertLiveClass(liveClass: LiveClassEntity) = withContext(Dispatchers.IO) {
    liveClassDao.insertLiveClass(liveClass)
  }
  suspend fun setLiveStatus(id: Long, isLive: Boolean, viewers: Int) = withContext(Dispatchers.IO) {
    liveClassDao.setLiveStatus(id, isLive, viewers)
  }

  // Tests & Questions
  fun getTests(classLevel: String = ""): Flow<List<TestEntity>> = testDao.getTests(classLevel)
  suspend fun getQuestionsForTest(testId: Long): List<QuestionEntity> = withContext(Dispatchers.IO) {
    testDao.getQuestionsForTest(testId)
  }
  suspend fun submitTestAttempt(attempt: TestAttemptEntity) = withContext(Dispatchers.IO) {
    testDao.insertAttempt(attempt)
  }
  fun getTestAttempts(): Flow<List<TestAttemptEntity>> = testDao.getAllAttempts()
  suspend fun insertTest(test: TestEntity, questions: List<QuestionEntity>) = withContext(Dispatchers.IO) {
    val testId = testDao.insertTest(test)
    val mappedQuestions = questions.map { it.copy(testId = testId) }
    testDao.insertQuestions(mappedQuestions)
  }

  // Payments & Notifications
  fun getAllPayments(): Flow<List<PaymentEntity>> = paymentDao.getAllPayments()
  fun getAllNotifications(): Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
  suspend fun markNotificationRead(id: Long) = withContext(Dispatchers.IO) {
    notificationDao.markAsRead(id)
  }
  suspend fun addNotification(title: String, message: String) = withContext(Dispatchers.IO) {
    notificationDao.insertNotification(
      NotificationEntity(
        title = title,
        message = message,
        timestamp = "अभी (Just Now)",
        isRead = false
      )
    )
  }
}
