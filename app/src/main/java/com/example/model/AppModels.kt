package com.example.model

enum class Language(val code: String, val displayName: String, val isRtl: Boolean) {
  HINDI("hi", "हिन्दी (Hindi)", false),
  URDU("ur", "اردو (Urdu)", true),
  ENGLISH("en", "English", false)
}

enum class AcademicClass(val id: String, val titleHindi: String, val titleUrdu: String, val titleEnglish: String) {
  CLASS_10(
    "class_10",
    "बिहार बोर्ड मैट्रिक (कक्षा 10वीं)",
    "بہار بورڈ میٹرک (دسویں جماعت)",
    "Bihar Board Class 10th (Matric)"
  ),
  CLASS_11_ARTS(
    "class_11_arts",
    "बिहार बोर्ड इण्टर 11वीं कला (Arts)",
    "بہار بورڈ گیارہویں آرٹس (انٹرمیڈیٹ)",
    "Bihar Board Class 11th Arts"
  ),
  CLASS_12_ARTS(
    "class_12_arts",
    "बिहार बोर्ड इण्टर 12वीं कला (Arts)",
    "بہار بورڈ بارہویں آرٹس (انٹرمیڈیٹ)",
    "Bihar Board Class 12th Arts"
  )
}

enum class UserRole {
  STUDENT,
  TEACHER,
  ADMIN
}

enum class NavTab {
  HOME,
  COURSES,
  LIVE,
  TESTS,
  PROFILE
}

data class UserSession(
  val id: Long = 1L,
  val name: String = "Md Faiyaz Alam",
  val phone: String = "+91 98765 43210",
  val email: String = "alammdfaiyaz828@gmail.com",
  val selectedClass: AcademicClass = AcademicClass.CLASS_12_ARTS,
  val preferredLanguage: Language = Language.HINDI,
  val role: UserRole = UserRole.STUDENT,
  val isLoggedIn: Boolean = true
)

// Multilingual string lookup
object AppStrings {
  fun get(key: String, language: Language): String {
    return when (language) {
      Language.URDU -> urduStrings[key] ?: hindiStrings[key] ?: englishStrings[key] ?: key
      Language.HINDI -> hindiStrings[key] ?: englishStrings[key] ?: key
      Language.ENGLISH -> englishStrings[key] ?: key
    }
  }

  private val englishStrings = mapOf(
    "app_name" to "The Champaran Classes",
    "tagline" to "Learn Today, Lead Tomorrow",
    "tab_home" to "Home",
    "tab_courses" to "Courses",
    "tab_live" to "Live Classes",
    "tab_tests" to "Test Series",
    "tab_profile" to "Profile",
    "live_badge" to "LIVE NOW",
    "join_class" to "Join Live Class",
    "upcoming_classes" to "Upcoming Live Classes",
    "enrolled_courses" to "My Enrolled Courses",
    "popular_courses" to "Bihar Board Batches",
    "recorded_lectures" to "Recorded Lectures",
    "pdf_notes" to "Chapter PDF Notes",
    "test_series" to "Online Test Series",
    "download_notes" to "Download PDF",
    "read_online" to "Read Online",
    "start_test" to "Start Test",
    "submit_test" to "Submit Test",
    "buy_now" to "Enroll Now",
    "payment_successful" to "Payment Successful!",
    "admin_panel" to "Admin Dashboard",
    "teacher_panel" to "Teacher Panel",
    "switch_role" to "Switch App Role",
    "search_hint" to "Search courses, topics, chapters...",
    "continue_learning" to "Continue Learning",
    "view_all" to "View All",
    "score" to "Score",
    "accuracy" to "Accuracy",
    "performance_report" to "Performance Report",
    "correct_answer" to "Correct Answer",
    "explanation" to "Explanation",
    "hindi_medium" to "Hindi Medium",
    "urdu_medium" to "Urdu Medium",
    "bihar_board_special" to "Bihar Board Special",
    "login" to "Login",
    "register" to "Register",
    "verify_otp" to "Verify OTP",
    "mobile_number" to "Mobile Number",
    "full_name" to "Full Name",
    "select_class" to "Select Your Class",
    "select_language" to "Select Medium / Language"
  )

  private val hindiStrings = mapOf(
    "app_name" to "द चंपारण क्लासेज",
    "tagline" to "आज सीखें, कल नेतृत्व करें",
    "tab_home" to "होम",
    "tab_courses" to "कोर्स",
    "tab_live" to "लाइव क्लास",
    "tab_tests" to "टेस्ट सीरीज",
    "tab_profile" to "प्रोफाइल",
    "live_badge" to "लाइव चल रहा है",
    "join_class" to "लाइव क्लास में जुड़ें",
    "upcoming_classes" to "आगामी लाइव क्लासेस",
    "enrolled_courses" to "मेरे नामांकित कोर्स",
    "popular_courses" to "बिहार बोर्ड स्पेशल बैच",
    "recorded_lectures" to "रिकॉर्डेड वीडियो क्लासेज",
    "pdf_notes" to "अध्यायवार PDF नोट्स",
    "test_series" to "ऑनलाइन टेस्ट सीरीज (MCQ)",
    "download_notes" to "डाउनलोड PDF",
    "read_online" to "ऑनलाइन पढ़ें",
    "start_test" to "टेस्ट शुरू करें",
    "submit_test" to "टेस्ट जमा करें",
    "buy_now" to "अभी प्रवेश लें",
    "payment_successful" to "भुगतान सफल हुआ!",
    "admin_panel" to "एडमिन पैनल",
    "teacher_panel" to "शिक्षक पैनल",
    "switch_role" to "रोल बदलें",
    "search_hint" to "कोर्स, विषय, अध्याय खोजें...",
    "continue_learning" to "पढ़ाई जारी रखें",
    "view_all" to "सभी देखें",
    "score" to "प्राप्तांक",
    "accuracy" to "सटीकता",
    "performance_report" to "प्रदर्शन रिपोर्ट",
    "correct_answer" to "सही उत्तर",
    "explanation" to "व्याख्या / समाधान",
    "hindi_medium" to "हिन्दी माध्यम",
    "urdu_medium" to "उर्दू माध्यम",
    "bihar_board_special" to "बिहार बोर्ड स्पेशल",
    "login" to "लॉग इन",
    "register" to "नया पंजीकरण",
    "verify_otp" to "OTP सत्यापित करें",
    "mobile_number" to "मोबाइल नंबर",
    "full_name" to "पूरा नाम",
    "select_class" to "अपनी कक्षा चुनें",
    "select_language" to "माध्यम / भाषा चुनें"
  )

  private val urduStrings = mapOf(
    "app_name" to "دی چمپارن کلاسز",
    "tagline" to "آج سیکھیں، کل قیادت کریں",
    "tab_home" to "ہوم",
    "tab_courses" to "کورسز",
    "tab_live" to "لائیو کلاسز",
    "tab_tests" to "ٹیسٹ سیریز",
    "tab_profile" to "پروفائل",
    "live_badge" to "ابھی لائیو",
    "join_class" to "لائیو کلاس میں شامل ہوں",
    "upcoming_classes" to "آنے والی لائیو کلاسز",
    "enrolled_courses" to "میرے شامل کردہ کورسز",
    "popular_courses" to "بہار بورڈ بیچز",
    "recorded_lectures" to "ریکارڈ شدہ ویڈیوز",
    "pdf_notes" to "باب وار پی ڈی ایف نوٹس",
    "test_series" to "آن لائن ٹیسٹ سیریز",
    "download_notes" to "ڈاؤن لوڈ کریں",
    "read_online" to "آن لائن پڑھیں",
    "start_test" to "ٹیسٹ شروع کریں",
    "submit_test" to "ٹیسٹ جمع کریں",
    "buy_now" to "ابھی داخلہ لیں",
    "payment_successful" to "ادائیگی کامیاب ہوئی!",
    "admin_panel" to "ایڈمن ڈیش بورڈ",
    "teacher_panel" to "استاد پینل",
    "switch_role" to "کردار تبدیل کریں",
    "search_hint" to "کورس، مضمون، باب تلاش کریں...",
    "continue_learning" to "پڑھائی جاری رکھیں",
    "view_all" to "سبھی دیکھیں",
    "score" to "حاصل کردہ نمبرات",
    "accuracy" to "درستگی",
    "performance_report" to "کارکردگی رپورٹ",
    "correct_answer" to "صحیح جواب",
    "explanation" to "وضاحت / تشریح",
    "hindi_medium" to "ہندی میڈیم",
    "urdu_medium" to "اردو میڈیم",
    "bihar_board_special" to "بہار بورڈ خصوصی",
    "login" to "لاگ ان کریں",
    "register" to "رجسٹریشن",
    "verify_otp" to "او ٹی پی کی تصدیق",
    "mobile_number" to "موبائل نمبر",
    "full_name" to "مکمل نام",
    "select_class" to "اپنی جماعت منتخب کریں",
    "select_language" to "زبان منتخب کریں"
  )
}
