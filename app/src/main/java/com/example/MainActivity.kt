package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.ChamparanRepository
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.ChamparanTheme
import com.example.ui.theme.RoyalBlue800
import com.example.viewmodel.AuthScreenMode
import com.example.viewmodel.MainViewModel

class MainViewModelFactory(private val repository: ChamparanRepository) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return MainViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val context = LocalContext.current
      val db = remember { AppDatabase.getDatabase(context) }
      val repository = remember { ChamparanRepository(db) }
      val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(repository))

      val userSession by viewModel.userSession.collectAsStateWithLifecycle()
      val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
      val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
      val isDarkModeOverride by viewModel.isDarkModeOverride.collectAsStateWithLifecycle()

      val systemDark = isSystemInDarkTheme()
      val useDarkMode = isDarkModeOverride ?: systemDark

      ChamparanTheme(darkTheme = useDarkMode) {
        val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
          MainAppContent(viewModel = viewModel, userSession = userSession, currentLanguage = currentLanguage, currentTab = currentTab, isDarkMode = useDarkMode)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
  viewModel: MainViewModel,
  userSession: UserSession,
  currentLanguage: Language,
  currentTab: NavTab,
  isDarkMode: Boolean
) {
  val allCourses by viewModel.allCourses.collectAsStateWithLifecycle()
  val enrolledCourses by viewModel.enrolledCourses.collectAsStateWithLifecycle()
  val continueWatching by viewModel.continueWatchingLectures.collectAsStateWithLifecycle()
  val allLiveClasses by viewModel.allLiveClasses.collectAsStateWithLifecycle()
  val allTests by viewModel.allTests.collectAsStateWithLifecycle()
  val testAttempts by viewModel.testAttempts.collectAsStateWithLifecycle()
  val notifications by viewModel.notifications.collectAsStateWithLifecycle()
  val payments by viewModel.payments.collectAsStateWithLifecycle()

  val selectedCourse by viewModel.selectedCourse.collectAsStateWithLifecycle()
  val activeLecture by viewModel.activeLecture.collectAsStateWithLifecycle()
  val activePdfNote by viewModel.activePdfNote.collectAsStateWithLifecycle()
  val activeLiveClass by viewModel.activeLiveClass.collectAsStateWithLifecycle()
  val checkoutCourse by viewModel.checkoutCourse.collectAsStateWithLifecycle()
  val isAuthVisible by viewModel.isAuthVisible.collectAsStateWithLifecycle()
  val authMode by viewModel.authMode.collectAsStateWithLifecycle()

  // Test Runner States
  val activeTest by viewModel.activeTest.collectAsStateWithLifecycle()
  val testQuestions by viewModel.testQuestions.collectAsStateWithLifecycle()
  val currentQuestionIdx by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
  val selectedAnswers by viewModel.selectedAnswers.collectAsStateWithLifecycle()
  val timerSeconds by viewModel.testTimerSeconds.collectAsStateWithLifecycle()
  val testResult by viewModel.testResultAttempt.collectAsStateWithLifecycle()

  val liveChatMessages by viewModel.liveChatMessages.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedClassFilter by viewModel.selectedClassFilter.collectAsStateWithLifecycle()

  var showLanguageDialog by remember { mutableStateOf(false) }
  var showNotificationsDialog by remember { mutableStateOf(false) }
  var showAdminPanel by remember { mutableStateOf(false) }
  var showTeacherPanel by remember { mutableStateOf(false) }

  val unreadNotifications = remember(notifications) { notifications.count { !it.isRead } }
  val isAnyLiveClassActive = remember(allLiveClasses) { allLiveClasses.any { it.isLiveNow } }
  val currentActiveLive = remember(allLiveClasses) { allLiveClasses.find { it.isLiveNow } }

  // System Back Navigation Handlers
  BackHandler(enabled = activeLecture != null) { viewModel.closeVideoPlayer() }
  BackHandler(enabled = activePdfNote != null) { viewModel.closePdfReader() }
  BackHandler(enabled = activeLiveClass != null) { viewModel.leaveLiveClass() }
  BackHandler(enabled = activeTest != null) { viewModel.closeTestRunner() }
  BackHandler(enabled = checkoutCourse != null) { viewModel.closeCheckout() }
  BackHandler(enabled = selectedCourse != null) { viewModel.closeCourseDetail() }
  BackHandler(enabled = showAdminPanel) { showAdminPanel = false }
  BackHandler(enabled = showTeacherPanel) { showTeacherPanel = false }

  // Root Screen Scaffold
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (activeLecture == null && activePdfNote == null && activeLiveClass == null && activeTest == null && !showAdminPanel && !showTeacherPanel) {
        AppTopBar(
          currentLanguage = currentLanguage,
          userRole = userSession.role,
          unreadNotificationCount = unreadNotifications,
          onLanguageClick = { showLanguageDialog = true },
          onNotificationClick = { showNotificationsDialog = true },
          onRoleClick = {
            // Cycle role easily or open profile
            val nextRole = when (userSession.role) {
              UserRole.STUDENT -> UserRole.TEACHER
              UserRole.TEACHER -> UserRole.ADMIN
              UserRole.ADMIN -> UserRole.STUDENT
            }
            viewModel.switchUserRole(nextRole)
          }
        )
      }
    },
    bottomBar = {
      if (activeLecture == null && activePdfNote == null && activeLiveClass == null && activeTest == null && !showAdminPanel && !showTeacherPanel) {
        AppBottomBar(
          currentTab = currentTab,
          currentLanguage = currentLanguage,
          isLiveClassActive = isAnyLiveClassActive,
          onTabSelected = { viewModel.selectTab(it) }
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Primary Tab Contents
      when {
        showAdminPanel -> {
          AdminPanelScreen(
            courses = allCourses,
            onClose = { showAdminPanel = false },
            onAddCourse = { titleHi, titleUr, cls, sub, desc, pr ->
              viewModel.adminAddCourse(titleHi, titleUr, cls, sub, desc, pr)
            },
            onScheduleLive = { t, s, c, tr, tm, tp ->
              viewModel.adminScheduleLive(t, s, c, tr, tm, tp)
            },
            onUploadLecture = { cId, t, ch, s, dur, u ->
              viewModel.adminUploadLecture(cId, t, ch, s, dur, u)
            },
            onUploadNote = { cId, t, ch, s, l, cont ->
              viewModel.adminUploadNote(cId, t, ch, s, l, cont)
            },
            onSendNotification = { t, m ->
              viewModel.adminSendNotification(t, m)
            },
            onDeleteCourse = { id ->
              viewModel.adminDeleteCourse(id)
            }
          )
        }

        showTeacherPanel -> {
          TeacherPanelScreen(
            userSession = userSession,
            liveClasses = allLiveClasses,
            onClose = { showTeacherPanel = false },
            onToggleLiveStream = { live, start ->
              viewModel.teacherToggleLiveStream(live, start)
            }
          )
        }

        selectedCourse != null -> {
          val courseLectures by viewModel.getLecturesForCurrentCourse(selectedCourse!!.id).collectAsStateWithLifecycle(emptyList())
          val courseNotes by viewModel.getNotesForCurrentCourse(selectedCourse!!.id).collectAsStateWithLifecycle(emptyList())

          CourseDetailScreen(
            course = selectedCourse!!,
            currentLanguage = currentLanguage,
            lectures = courseLectures,
            pdfNotes = courseNotes,
            onClose = { viewModel.closeCourseDetail() },
            onPlayLecture = { viewModel.playLecture(it) },
            onOpenPdf = { viewModel.openPdfNote(it) },
            onToggleDownloadPdf = { viewModel.toggleDownloadPdf(it) },
            onEnroll = { viewModel.openCheckout(it) }
          )
        }

        else -> {
          when (currentTab) {
            NavTab.HOME -> {
              HomeScreen(
                userSession = userSession,
                currentLanguage = currentLanguage,
                enrolledCourses = enrolledCourses,
                popularCourses = allCourses,
                continueWatching = continueWatching,
                activeLiveClass = currentActiveLive,
                onSelectCourse = { viewModel.openCourseDetail(it) },
                onPlayLecture = { viewModel.playLecture(it) },
                onJoinLiveClass = { viewModel.joinLiveClass(it) },
                onNavigateToTab = { viewModel.selectTab(it) },
                onSelectClassFilter = { viewModel.setClassFilter(it) }
              )
            }

            NavTab.COURSES -> {
              CoursesScreen(
                courses = allCourses,
                currentLanguage = currentLanguage,
                selectedClassFilter = selectedClassFilter,
                searchQuery = searchQuery,
                onSearchChange = { viewModel.setSearchQuery(it) },
                onSelectFilter = { viewModel.setClassFilter(it) },
                onSelectCourse = { viewModel.openCourseDetail(it) },
                onEnrollCourse = { viewModel.openCheckout(it) }
              )
            }

            NavTab.LIVE -> {
              LiveClassesScreen(
                liveClasses = allLiveClasses,
                currentLanguage = currentLanguage,
                userRole = userSession.role,
                onJoinLiveClass = { viewModel.joinLiveClass(it) },
                onOpenTeacherLiveModal = { showTeacherPanel = true }
              )
            }

            NavTab.TESTS -> {
              TestsScreen(
                tests = allTests,
                attempts = testAttempts,
                currentLanguage = currentLanguage,
                activeTest = activeTest,
                testQuestions = testQuestions,
                currentQuestionIdx = currentQuestionIdx,
                selectedAnswers = selectedAnswers,
                timerSeconds = timerSeconds,
                testResult = testResult,
                onStartTest = { viewModel.startTest(it) },
                onSelectAnswer = { qIdx, optIdx -> viewModel.selectAnswer(qIdx, optIdx) },
                onGoToQuestion = { viewModel.goToQuestion(it) },
                onSubmitTest = { viewModel.submitActiveTest() },
                onCloseTest = { viewModel.closeTestRunner() }
              )
            }

            NavTab.PROFILE -> {
              ProfileScreen(
                userSession = userSession,
                currentLanguage = currentLanguage,
                isDarkMode = isDarkMode,
                payments = payments,
                onSelectLanguageClick = { showLanguageDialog = true },
                onToggleDarkMode = { viewModel.toggleDarkMode(it) },
                onSwitchRole = { viewModel.switchUserRole(it) },
                onOpenAdminPanel = { showAdminPanel = true },
                onOpenTeacherPanel = { showTeacherPanel = true },
                onLogout = { viewModel.logout() }
              )
            }
          }
        }
      }

      // Overlays & Fullscreen Modals
      if (activeLecture != null) {
        VideoPlayerScreen(
          lecture = activeLecture!!,
          currentLanguage = currentLanguage,
          onClose = { viewModel.closeVideoPlayer() },
          onProgressUpdate = { id, pos, comp -> viewModel.updateLectureProgress(id, pos, comp) },
          onToggleFavorite = { viewModel.toggleFavoriteLecture(it) }
        )
      }

      if (activePdfNote != null) {
        PdfReaderScreen(
          note = activePdfNote!!,
          currentLanguage = currentLanguage,
          onClose = { viewModel.closePdfReader() },
          onToggleDownload = { viewModel.toggleDownloadPdf(it) }
        )
      }

      if (activeLiveClass != null) {
        LiveStreamScreen(
          liveClass = activeLiveClass!!,
          currentLanguage = currentLanguage,
          userRole = userSession.role,
          chatMessages = liveChatMessages,
          onSendMessage = { viewModel.sendLiveChatMessage(it) },
          onClose = { viewModel.leaveLiveClass() }
        )
      }

      if (checkoutCourse != null) {
        PaymentBottomSheet(
          course = checkoutCourse!!,
          currentLanguage = currentLanguage,
          onDismiss = { viewModel.closeCheckout() },
          onPaymentSuccess = { method -> viewModel.completeEnrollmentPayment(method) }
        )
      }

      if (isAuthVisible) {
        AuthScreen(
          currentMode = authMode,
          currentLanguage = currentLanguage,
          onCompleteLogin = { n, m -> viewModel.completeLogin(n, m) },
          onSwitchMode = { viewModel.showAuth(it) },
          onDismiss = { viewModel.hideAuth() }
        )
      }
    }
  }

  // Language Switcher Dialog
  if (showLanguageDialog) {
    LanguageSwitcherDialog(
      currentLanguage = currentLanguage,
      onSelectLanguage = { viewModel.setLanguage(it) },
      onDismiss = { showLanguageDialog = false }
    )
  }

  // Notifications Dialog
  if (showNotificationsDialog) {
    AlertDialog(
      onDismissRequest = { showNotificationsDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(Icons.Default.Notifications, contentDescription = null, tint = RoyalBlue800)
          Text("Notifications / सूचनाएँ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          if (notifications.isEmpty()) {
            item { Text("No notifications yet.") }
          } else {
            items(notifications) { notif ->
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = notif.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text(text = notif.timestamp, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(text = notif.message, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showNotificationsDialog = false }) { Text("Close / बंद करें") }
      }
    )
  }
}
