package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.repository.ChamparanRepository
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LiveChatMessage(
  val id: String,
  val senderName: String,
  val role: String, // "Teacher", "Student", "Admin"
  val message: String,
  val time: String,
  val isPinned: Boolean = false
)

enum class AuthScreenMode {
  LOGIN,
  REGISTER,
  OTP_VERIFY,
  FORGOT_PASSWORD
}

class MainViewModel(private val repository: ChamparanRepository) : ViewModel() {

  // User & Localization
  private val _userSession = MutableStateFlow(UserSession())
  val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

  private val _currentLanguage = MutableStateFlow(Language.HINDI)
  val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

  private val _isDarkModeOverride = MutableStateFlow<Boolean?>(null)
  val isDarkModeOverride: StateFlow<Boolean?> = _isDarkModeOverride.asStateFlow()

  // Navigation
  private val _currentTab = MutableStateFlow(NavTab.HOME)
  val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

  // Data from Repository
  val allCourses: StateFlow<List<CourseEntity>> = repository.getAllCourses()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val enrolledCourses: StateFlow<List<CourseEntity>> = repository.getEnrolledCourses()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val continueWatchingLectures: StateFlow<List<LectureEntity>> = repository.getContinueWatchingLectures()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allLiveClasses: StateFlow<List<LiveClassEntity>> = repository.getAllLiveClasses()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allTests: StateFlow<List<TestEntity>> = repository.getTests("")
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val testAttempts: StateFlow<List<TestAttemptEntity>> = repository.getTestAttempts()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val notifications: StateFlow<List<NotificationEntity>> = repository.getAllNotifications()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val payments: StateFlow<List<PaymentEntity>> = repository.getAllPayments()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Active Modals & Subscreens
  private val _selectedCourse = MutableStateFlow<CourseEntity?>(null)
  val selectedCourse: StateFlow<CourseEntity?> = _selectedCourse.asStateFlow()

  private val _activeLecture = MutableStateFlow<LectureEntity?>(null)
  val activeLecture: StateFlow<LectureEntity?> = _activeLecture.asStateFlow()

  private val _activePdfNote = MutableStateFlow<PdfNoteEntity?>(null)
  val activePdfNote: StateFlow<PdfNoteEntity?> = _activePdfNote.asStateFlow()

  private val _activeLiveClass = MutableStateFlow<LiveClassEntity?>(null)
  val activeLiveClass: StateFlow<LiveClassEntity?> = _activeLiveClass.asStateFlow()

  private val _checkoutCourse = MutableStateFlow<CourseEntity?>(null)
  val checkoutCourse: StateFlow<CourseEntity?> = _checkoutCourse.asStateFlow()

  // Test Runner State
  private val _activeTest = MutableStateFlow<TestEntity?>(null)
  val activeTest: StateFlow<TestEntity?> = _activeTest.asStateFlow()

  private val _testQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
  val testQuestions: StateFlow<List<QuestionEntity>> = _testQuestions.asStateFlow()

  private val _currentQuestionIndex = MutableStateFlow(0)
  val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

  private val _selectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap()) // Question Index -> Option Index (0..3)
  val selectedAnswers: StateFlow<Map<Int, Int>> = _selectedAnswers.asStateFlow()

  private val _testTimerSeconds = MutableStateFlow(0)
  val testTimerSeconds: StateFlow<Int> = _testTimerSeconds.asStateFlow()

  private val _testResultAttempt = MutableStateFlow<TestAttemptEntity?>(null)
  val testResultAttempt: StateFlow<TestAttemptEntity?> = _testResultAttempt.asStateFlow()

  private var timerJob: Job? = null

  // Live Chat State
  private val _liveChatMessages = MutableStateFlow<List<LiveChatMessage>>(emptyList())
  val liveChatMessages: StateFlow<List<LiveChatMessage>> = _liveChatMessages.asStateFlow()

  // Auth Screen State
  private val _isAuthVisible = MutableStateFlow(false)
  val isAuthVisible: StateFlow<Boolean> = _isAuthVisible.asStateFlow()

  private val _authMode = MutableStateFlow(AuthScreenMode.LOGIN)
  val authMode: StateFlow<AuthScreenMode> = _authMode.asStateFlow()

  // Search Filter
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  // Class Filter
  private val _selectedClassFilter = MutableStateFlow<AcademicClass?>(null)
  val selectedClassFilter: StateFlow<AcademicClass?> = _selectedClassFilter.asStateFlow()

  init {
    viewModelScope.launch {
      repository.ensureDataPopulated()
      initializeMockLiveChat()
    }
  }

  // Language & RTL
  fun setLanguage(language: Language) {
    _currentLanguage.value = language
    _userSession.value = _userSession.value.copy(preferredLanguage = language)
  }

  fun toggleDarkMode(enable: Boolean?) {
    _isDarkModeOverride.value = enable
  }

  // Navigation
  fun selectTab(tab: NavTab) {
    _currentTab.value = tab
    _selectedCourse.value = null
    _activeLiveClass.value = null
    _activeTest.value = null
  }

  fun setClassFilter(cls: AcademicClass?) {
    _selectedClassFilter.value = cls
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  // Role Switching (Student, Teacher, Admin)
  fun switchUserRole(newRole: UserRole) {
    _userSession.value = _userSession.value.copy(role = newRole)
  }

  fun updateUserProfile(name: String, mobile: String, email: String, cls: AcademicClass) {
    _userSession.value = _userSession.value.copy(
      name = name,
      phone = mobile,
      email = email,
      selectedClass = cls
    )
  }

  // Course Details & Lectures
  fun openCourseDetail(course: CourseEntity) {
    _selectedCourse.value = course
  }

  fun closeCourseDetail() {
    _selectedCourse.value = null
  }

  fun getLecturesForCurrentCourse(courseId: Long): Flow<List<LectureEntity>> {
    return repository.getLecturesForCourse(courseId)
  }

  fun getNotesForCurrentCourse(courseId: Long): Flow<List<PdfNoteEntity>> {
    return repository.getNotesForCourse(courseId)
  }

  // Video Player
  fun playLecture(lecture: LectureEntity) {
    _activeLecture.value = lecture
  }

  fun closeVideoPlayer() {
    _activeLecture.value = null
  }

  fun updateLectureProgress(lectureId: Long, positionSecs: Int, completed: Boolean) {
    viewModelScope.launch {
      repository.updatePlaybackProgress(lectureId, positionSecs, completed)
      _activeLecture.value = _activeLecture.value?.copy(
        lastPositionSeconds = positionSecs,
        isCompleted = completed
      )
    }
  }

  fun toggleFavoriteLecture(lecture: LectureEntity) {
    viewModelScope.launch {
      val newFav = !lecture.isFavorite
      repository.toggleFavorite(lecture.id, newFav)
      if (_activeLecture.value?.id == lecture.id) {
        _activeLecture.value = _activeLecture.value?.copy(isFavorite = newFav)
      }
    }
  }

  // PDF Reader & Download
  fun openPdfNote(note: PdfNoteEntity) {
    _activePdfNote.value = note
  }

  fun closePdfReader() {
    _activePdfNote.value = null
  }

  fun toggleDownloadPdf(note: PdfNoteEntity) {
    viewModelScope.launch {
      val newStatus = !note.isDownloaded
      repository.toggleDownloadNote(note.id, newStatus)
      if (_activePdfNote.value?.id == note.id) {
        _activePdfNote.value = _activePdfNote.value?.copy(isDownloaded = newStatus)
      }
    }
  }

  // Live Classes
  fun joinLiveClass(liveClass: LiveClassEntity) {
    _activeLiveClass.value = liveClass
  }

  fun leaveLiveClass() {
    _activeLiveClass.value = null
  }

  private fun initializeMockLiveChat() {
    _liveChatMessages.value = listOf(
      LiveChatMessage("1", "डॉ. एस. एन. सिन्हा (Teacher)", "Teacher", "सभी विद्यार्थियों का चंपारण क्लासेज में स्वागत है! आज हम विजयनगर साम्राज्य पढ़ेंगे।", "08:00 PM", true),
      LiveChatMessage("2", "रोहित कुमार", "Student", "प्रणाम सर! क्या यह टॉपिक इस बार बोर्ड परीक्षा में 5 नंबर में आएगा?", "08:02 PM"),
      LiveChatMessage("3", "आयशा परवीन", "Student", "سر کیا اس کا پی ڈی ایف نوٹس بھی ملے گا؟", "08:03 PM"),
      LiveChatMessage("4", "डॉ. एस. एन. सिन्हा (Teacher)", "Teacher", "हाँ आयशा, क्लास समाप्त होते ही उर्दू और हिन्दी दोनों में नोट्स अपलोड हो जाएँगे।", "08:04 PM")
    )
  }

  fun sendLiveChatMessage(text: String) {
    if (text.isBlank()) return
    val user = _userSession.value
    val msg = LiveChatMessage(
      id = System.currentTimeMillis().toString(),
      senderName = user.name,
      role = if (user.role == UserRole.TEACHER) "Teacher" else "Student",
      message = text.trim(),
      time = "Just now"
    )
    _liveChatMessages.value = _liveChatMessages.value + msg
  }

  // Online Test Series
  fun startTest(test: TestEntity) {
    viewModelScope.launch {
      _activeTest.value = test
      _testResultAttempt.value = null
      _currentQuestionIndex.value = 0
      _selectedAnswers.value = emptyMap()
      val questions = repository.getQuestionsForTest(test.id)
      _testQuestions.value = questions
      _testTimerSeconds.value = test.durationMinutes * 60

      timerJob?.cancel()
      timerJob = viewModelScope.launch {
        while (_testTimerSeconds.value > 0) {
          delay(1000)
          _testTimerSeconds.value -= 1
        }
        submitActiveTest()
      }
    }
  }

  fun selectAnswer(questionIdx: Int, optionIdx: Int) {
    _selectedAnswers.value = _selectedAnswers.value + (questionIdx to optionIdx)
  }

  fun goToQuestion(idx: Int) {
    if (idx in _testQuestions.value.indices) {
      _currentQuestionIndex.value = idx
    }
  }

  fun submitActiveTest() {
    timerJob?.cancel()
    val test = _activeTest.value ?: return
    val questions = _testQuestions.value
    val answers = _selectedAnswers.value

    var correctCount = 0
    var wrongCount = 0

    questions.forEachIndexed { index, question ->
      val selected = answers[index]
      if (selected != null) {
        if (selected == question.correctOptionIndex) {
          correctCount++
        } else {
          wrongCount++
        }
      }
    }

    val totalAttempted = correctCount + wrongCount
    val unattempted = questions.size - totalAttempted
    val marksPerQuestion = if (questions.isNotEmpty()) test.totalMarks / questions.size else 5
    val totalScore = correctCount * marksPerQuestion
    val accuracy = if (totalAttempted > 0) (correctCount * 100) / totalAttempted else 0

    val attempt = TestAttemptEntity(
      testId = test.id,
      testTitleHindi = test.titleHindi,
      score = totalScore,
      totalMarks = test.totalMarks,
      correctAnswers = correctCount,
      wrongAnswers = wrongCount,
      unattempted = unattempted,
      accuracyPercent = accuracy,
      completedAtMillis = System.currentTimeMillis()
    )

    viewModelScope.launch {
      repository.submitTestAttempt(attempt)
      _testResultAttempt.value = attempt
    }
  }

  fun closeTestRunner() {
    timerJob?.cancel()
    _activeTest.value = null
    _testResultAttempt.value = null
  }

  // Checkout & Payments (Razorpay style)
  fun openCheckout(course: CourseEntity) {
    _checkoutCourse.value = course
  }

  fun closeCheckout() {
    _checkoutCourse.value = null
  }

  fun completeEnrollmentPayment(method: String) {
    val course = _checkoutCourse.value ?: return
    viewModelScope.launch {
      repository.processCourseEnrollment(
        courseId = course.id,
        courseTitle = course.titleHindi,
        amount = course.price,
        paymentMethod = method
      )
      _checkoutCourse.value = null
      _selectedCourse.value = course.copy(isEnrolled = true)
    }
  }

  // Auth Modal
  fun showAuth(mode: AuthScreenMode) {
    _authMode.value = mode
    _isAuthVisible.value = true
  }

  fun hideAuth() {
    _isAuthVisible.value = false
  }

  fun completeLogin(name: String, mobile: String) {
    _userSession.value = _userSession.value.copy(
      name = if (name.isNotBlank()) name else _userSession.value.name,
      phone = mobile,
      isLoggedIn = true
    )
    _isAuthVisible.value = false
  }

  fun logout() {
    _userSession.value = _userSession.value.copy(isLoggedIn = false)
    _isAuthVisible.value = true
    _authMode.value = AuthScreenMode.LOGIN
  }

  // Admin / Teacher Operations
  fun adminAddCourse(
    titleHindi: String,
    titleUrdu: String,
    cls: String,
    subject: String,
    desc: String,
    price: Int
  ) {
    viewModelScope.launch {
      val newCourse = CourseEntity(
        titleHindi = titleHindi,
        titleUrdu = if (titleUrdu.isNotBlank()) titleUrdu else titleHindi,
        classLevel = cls,
        subject = subject,
        descriptionHindi = desc,
        descriptionUrdu = desc,
        price = price,
        originalPrice = price + 1000,
        rating = 5.0f,
        totalLectures = 1,
        totalPdfs = 1,
        isEnrolled = false
      )
      repository.insertCourse(newCourse)
    }
  }

  fun adminScheduleLive(
    title: String,
    subject: String,
    cls: String,
    teacher: String,
    time: String,
    topic: String
  ) {
    viewModelScope.launch {
      val live = LiveClassEntity(
        titleHindi = title,
        titleUrdu = title,
        subject = subject,
        classLevel = cls,
        teacherName = teacher,
        scheduledTime = time,
        isLiveNow = false,
        viewerCount = 0,
        streamTopic = topic
      )
      repository.insertLiveClass(live)
    }
  }

  fun teacherToggleLiveStream(liveClass: LiveClassEntity, startLive: Boolean) {
    viewModelScope.launch {
      repository.setLiveStatus(liveClass.id, startLive, if (startLive) 840 else 0)
    }
  }

  fun adminUploadLecture(
    courseId: Long,
    title: String,
    chapter: String,
    subject: String,
    duration: Int,
    url: String
  ) {
    viewModelScope.launch {
      repository.insertLecture(
        LectureEntity(
          courseId = courseId,
          titleHindi = title,
          titleUrdu = title,
          chapterNameHindi = chapter,
          chapterNameUrdu = chapter,
          subject = subject,
          durationMinutes = duration,
          videoUrl = url
        )
      )
    }
  }

  fun adminUploadNote(
    courseId: Long,
    title: String,
    chapter: String,
    subject: String,
    lang: String,
    content: String
  ) {
    viewModelScope.launch {
      repository.insertPdfNote(
        PdfNoteEntity(
          courseId = courseId,
          titleHindi = title,
          titleUrdu = title,
          chapterNameHindi = chapter,
          chapterNameUrdu = chapter,
          subject = subject,
          language = lang,
          contentMarkdown = content
        )
      )
    }
  }

  fun adminSendNotification(title: String, message: String) {
    viewModelScope.launch {
      repository.addNotification(title, message)
    }
  }

  fun adminDeleteCourse(courseId: Long) {
    viewModelScope.launch {
      repository.deleteCourse(courseId)
    }
  }
}
