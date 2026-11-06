package com.example.data.local

object InitialData {

  fun getCourses(): List<CourseEntity> = listOf(
    CourseEntity(
      id = 1L,
      titleHindi = "बिहार बोर्ड 10वीं टॉपर बैच (सत्र 2026-27)",
      titleUrdu = "بہار بورڈ دسویں ٹاپر بیچ (2026-27)",
      classLevel = "class_10",
      subject = "All Subjects (विज्ञान, गणित, सामाजिक विज्ञान, हिन्दी, उर्दू)",
      descriptionHindi = "मैट्रिक परीक्षा में 450+ अंक लाने हेतु संपूर्ण पाठ्यक्रम, अध्यायवार वीडियो, हस्तलिखित नोट्स एवं 5000+ वस्तुनिष्ठ प्रश्न।",
      descriptionUrdu = "میٹرک امتحان میں 450+ نمبرات حاصل کرنے کیلئے مکمل نصاب، ویڈیوز، ہینڈ رٹن نوٹس اور ایم سی کیوز۔",
      price = 999,
      originalPrice = 2999,
      rating = 4.9f,
      totalLectures = 120,
      totalPdfs = 85,
      isEnrolled = true,
      progressPercent = 42
    ),
    CourseEntity(
      id = 2L,
      titleHindi = "12वीं कला (Arts) संपूर्ण संकल्प बैच",
      titleUrdu = "بارہویں آرٹس مکمل سنکلپ بیچ",
      classLevel = "class_12_arts",
      subject = "इतिहास, राजनीति विज्ञान, भूगोल, अर्थशास्त्र, हिन्दी, उर्दू",
      descriptionHindi = "बिहार बोर्ड 12वीं कला संकाय के विद्यार्थियों के लिए विशेष लाइव व रिकॉर्डेड क्लासेज, मॉडल पेपर्स एवं संशय समाधान।",
      descriptionUrdu = "بہار بورڈ 12ویں جماعت کے طلباء کیلئے تاریخ، پولیٹیکل سائنس، جغرافیہ، اردو اور ہندی کی خصوصی کلاسز۔",
      price = 1499,
      originalPrice = 3999,
      rating = 4.95f,
      totalLectures = 140,
      totalPdfs = 95,
      isEnrolled = true,
      progressPercent = 28
    ),
    CourseEntity(
      id = 3L,
      titleHindi = "11वीं कला (Arts) आधारशिला फाउंडेशन बैच",
      titleUrdu = "گیارہویں آرٹس فاؤنڈیشن بیچ",
      classLevel = "class_11_arts",
      subject = "History, Pol Science, Geography, Economics, Hindi & Urdu",
      descriptionHindi = "11वीं कक्षा के सभी मूल सिद्धांतों को मजबूत बनाने और आगामी बोर्ड परीक्षा की पूर्व तैयारी का सर्वोत्तम बैच।",
      descriptionUrdu = "گیارہویں جماعت کے تمام بنیادی اصولوں اور تصورات کو مضبوط کرنے کیلئے بہترین رہنمائی اور نوٹس۔",
      price = 899,
      originalPrice = 2499,
      rating = 4.8f,
      totalLectures = 90,
      totalPdfs = 60,
      isEnrolled = false,
      progressPercent = 0
    ),
    CourseEntity(
      id = 4L,
      titleHindi = "12वीं उर्दू (درخشاں و کہکشاں) मास्टर कोर्स",
      titleUrdu = "12ویں اردو کہکشاں و قواعد ماسٹر کورس",
      classLevel = "class_12_arts",
      subject = "Urdu 100 Marks (نثر، نظم و قواعد)",
      descriptionHindi = "100 अंक अनिवार्य उर्दू की अध्यायवार व्याख्या, मुंशी प्रेमचंद, ग़ालिब, इकबाल की कृतियाँ एवं व्याकरण।",
      descriptionUrdu = "100 مارکس اردو کے تمام اسباق، مضامین، خطوط نویسی اور گرامر پر مبنی جامع خصوصی کورس۔",
      price = 499,
      originalPrice = 1299,
      rating = 5.0f,
      totalLectures = 35,
      totalPdfs = 25,
      isEnrolled = true,
      progressPercent = 65
    )
  )

  fun getLectures(): List<LectureEntity> = listOf(
    // Course 1 - Class 10th
    LectureEntity(
      id = 101L,
      courseId = 1L,
      titleHindi = "यूरोप में राष्ट्रवाद - उदय, कारण एवं परिणाम (भाग 1)",
      titleUrdu = "یورپ میں قوم پرستی - اسباب اور نتائج (حصہ اول)",
      chapterNameHindi = "इतिहास - अध्याय 1",
      chapterNameUrdu = "تاریخ - باب اول",
      subject = "Social Science",
      durationMinutes = 45,
      videoUrl = "https://example.com/videos/ch1_history.mp4",
      lastPositionSeconds = 1250,
      isCompleted = false,
      isFavorite = true
    ),
    LectureEntity(
      id = 102L,
      courseId = 1L,
      titleHindi = "प्रकाश का परावर्तन एवं अपवर्तन (पूर्ण अध्याय)",
      titleUrdu = "روشنی کا انعکاس اور انعطاف (مکمل باب)",
      chapterNameHindi = "भौतिकी - अध्याय 1",
      chapterNameUrdu = "طبیعیات - باب اول",
      subject = "Science",
      durationMinutes = 55,
      videoUrl = "https://example.com/videos/ch1_physics.mp4",
      lastPositionSeconds = 3300,
      isCompleted = true,
      isFavorite = true
    ),
    LectureEntity(
      id = 103L,
      courseId = 1L,
      titleHindi = "वास्तविक संख्याएँ (Real Numbers) - यूक्लिड विभाजन प्रमेयिका",
      titleUrdu = "حقیقی اعداد اور یوکلیڈ الگورتھم",
      chapterNameHindi = "गणित - अध्याय 1",
      chapterNameUrdu = "ریاضی - باب اول",
      subject = "Mathematics",
      durationMinutes = 40,
      videoUrl = "https://example.com/videos/ch1_maths.mp4",
      lastPositionSeconds = 600,
      isCompleted = false,
      isFavorite = false
    ),
    // Course 2 - Class 12th Arts
    LectureEntity(
      id = 201L,
      courseId = 2L,
      titleHindi = "ईंटें, मनके तथा अस्थियाँ - हड़प्पा सभ्यता (सिंधु घाटी)",
      titleUrdu = "ہڑپہ تہذیب - اینٹیں، منکے اور ہڈیاں",
      chapterNameHindi = "इतिहास - भाग 1, अध्याय 1",
      chapterNameUrdu = "تاریخ - حصہ اول، باب 1",
      subject = "History",
      durationMinutes = 50,
      videoUrl = "https://example.com/videos/ch1_harappa.mp4",
      lastPositionSeconds = 1800,
      isCompleted = false,
      isFavorite = true
    ),
    LectureEntity(
      id = 202L,
      courseId = 2L,
      titleHindi = "दो ध्रुवीयता का अंत - सोवियत संघ का विघटन",
      titleUrdu = "دو قطبیت کا خاتمہ - سوویت یونین کی تباہی",
      chapterNameHindi = "राजनीति विज्ञान - अध्याय 1",
      chapterNameUrdu = "پولیٹیکل سائنس - باب 1",
      subject = "Political Science",
      durationMinutes = 48,
      videoUrl = "https://example.com/videos/ch1_polscience.mp4",
      lastPositionSeconds = 2880,
      isCompleted = true,
      isFavorite = false
    ),
    LectureEntity(
      id = 203L,
      courseId = 2L,
      titleHindi = "मानव भूगोल: प्रकृति एवं विषय क्षेत्र",
      titleUrdu = "انسانی جغرافیہ: نوعیت اور دائرہ کار",
      chapterNameHindi = "भूगोल - अध्याय 1",
      chapterNameUrdu = "جغرافیہ - باب 1",
      subject = "Geography",
      durationMinutes = 42,
      videoUrl = "https://example.com/videos/ch1_geography.mp4",
      lastPositionSeconds = 950,
      isCompleted = false,
      isFavorite = true
    )
  )

  fun getPdfNotes(): List<PdfNoteEntity> = listOf(
    PdfNoteEntity(
      id = 301L,
      courseId = 1L,
      titleHindi = "यूरोप में राष्ट्रवाद - सम्पूर्ण हस्तलिखित नोट्स",
      titleUrdu = "یورپ میں قوم پرستی - مکمل ہاتھ کے لکھے ہوئے نوٹس",
      chapterNameHindi = "इतिहास - अध्याय 1",
      chapterNameUrdu = "تاریخ - باب اول",
      subject = "Social Science",
      language = "hi",
      fileSizeBytes = 3450000L,
      isDownloaded = true,
      contentMarkdown = """
# अध्याय 1: यूरोप में राष्ट्रवाद (Nationalism in Europe)
**द चंपारण क्लासेज - विशेष अध्ययन सामग्री (बिहार बोर्ड मैट्रिक परीक्षा)**

### 1. राष्ट्रवाद क्या है?
राष्ट्रवाद किसी विशेष भौगोलिक, सांस्कृतिक या सामाजिक परिवेश में रहने वाले लोगों में एक होने की भावना (एकता का भाव) है। यह आधुनिक विश्व की राजनीतिक जागृति का महत्वपूर्ण प्रतिफल है।

### 2. 1830 की जुलाई क्रांति के कारण:
- चार्ल्स दशम का निरंकुश शासन।
- पोलिग्नेक को प्रधानमंत्री नियुक्त करना जो प्रतिक्रियावादी था।
- 25 जुलाई 1830 को चार अध्यादेश जारी कर जन स्वतंत्रता का हनन करना।
- परिणाम: बूर्बो राजवंश का अंत हुआ और आर्लेयंस वंश के लुई फिलिप को गद्दी मिली।

### 3. इटली के एकीकरण के प्रमुख नायक:
1. **मेजिनी (Mazzini):** 'यंग इटली' (1831) और 'यंग यूरोप' (1834) की स्थापना की। इटली के एकीकरण का मसीहा कहा जाता है।
2. **काउंट कावूर (Count Cavour):** कूटनीतिज्ञ राजनीतिज्ञ, जिसने क्रीमिया युद्ध में भाग लेकर इटली की समस्या को अंतर्राष्ट्रीय बना दिया।
3. **गैरीबाल्डी (Garibaldi):** 'लाल कुर्ती' (Red Shirts) नामक स्वयंसेवकों की सेना बनाई और दक्षिणी इटली (सिसली व नेपल्स) को जीतकर विक्टर इमैनुएल को सौंप दिया।

### 4. महत्वपूर्ण स्मरणीय तथ्य (MCQ Points):
- वियना कांग्रेस: 1815 ई. (अध्यक्ष: ऑस्ट्रिया का चांसलर मेटरनिख)।
- सेडान का युद्ध: 1870 ई. (फ्रांस और प्रशा के बीच)।
- फ्रैंकफर्ट की संधि: 10 मई 1871 (जर्मनी का एकीकरण पूर्ण हुआ)।
- हंगरी की भाषा: मैग्यार (Magyar)।
      """.trimIndent()
    ),
    PdfNoteEntity(
      id = 302L,
      courseId = 2L,
      titleHindi = "हड़प्पा सभ्यता (ईंटें, मनके तथा अस्थियाँ) - 12वीं कला",
      titleUrdu = "ہڑپہ تہذیب - مفصل نوٹس برائے بارہویں جماعت آرٹس",
      chapterNameHindi = "इतिहास - अध्याय 1",
      chapterNameUrdu = "تاریخ - باب اول",
      subject = "History",
      language = "hi",
      fileSizeBytes = 4120000L,
      isDownloaded = true,
      contentMarkdown = """
# 12वीं इतिहास (बिहार बोर्ड): ईंटें, मनके तथा अस्थियाँ (हड़प्पा सभ्यता)
**द चंपारण क्लासेज - बोर्ड परीक्षा विशेष नोट्स**

### 1. हड़प्पा सभ्यता की खोज:
- 1921 में रायबहादुर दयाराम साहनी ने हड़प्पा की खोज की (रावी नदी के तट पर, मोंटगोमरी, पाकिस्तान)।
- 1922 में राखालदास बनर्जी ने मोहनजोदड़ो की खोज की (सिंधु नदी के तट पर, लरकाना, सिंध)।
- भारतीय पुरातत्व सर्वेक्षण के तत्कालीन महानिदेशक सर जॉन मार्शल थे जिन्होंने 1924 में सिंधु घाटी सभ्यता की विधिवत घोषणा की।

### 2. नगर नियोजन की प्रमुख विशेषताएँ:
- **ग्रिड पद्धति (Grid System):** सड़कें एक-दूसरे को समकोण (90°) पर काटती थीं।
- **उत्कृष्ट जल निकासी प्रणाली:** प्रत्येक घर की नाली गली की मुख्य नाली से जुड़ी होती थी, जो पत्थरों या बड़ी ईंटों से ढंकी होती थी।
- **विशाल स्नानागार (Great Bath):** मोहनजोदड़ो के दुर्ग में स्थित आयताकार जलाशय, जिसमें उतरने के लिए उत्तर और दक्षिण में सीढ़ियाँ थीं।
- **अन्नागार (Granary):** मोहनजोदड़ो और हड़प्पा दोनों में विशाल अन्नागार के साक्ष्य मिले हैं।

### 3. धार्मिक विश्वास व मुहरें:
- मातृदेवी की उपासना सर्वाधिक प्रचलित थी।
- पशुपति शिव (आद्य शिव) की मुहर मोहनजोदड़ो से प्राप्त हुई है।
- मुहरें सेलखड़ी (Steatite) पत्थर की बनी होती थीं।
      """.trimIndent()
    ),
    PdfNoteEntity(
      id = 303L,
      courseId = 4L,
      titleHindi = "12वीं उर्दू - कहकशां हिस्सा दोम (نثر و نظم نوٹس)",
      titleUrdu = "کہکشاں حصہ دوم - مکمل تشریح و سوالات و جوابات",
      chapterNameHindi = "उर्दू 100 अंक - अध्याय 1",
      chapterNameUrdu = "اردو 100 نمبر - باب اول",
      subject = "Urdu",
      language = "ur",
      fileSizeBytes = 2890000L,
      isDownloaded = false,
      contentMarkdown = """
# کہکشاں حصہ دوم: نظیر احمد کی کہانی کچھ ان کی کچھ میری زبانی
**دی چمپارن کلاسز - بہار بورڈ بارہویں جماعت خصوصی مطالعہ**

### مصنف کا تعارف: مرزا فرحت اللہ بیگ
- مرزا فرحت اللہ بیگ اردو کے مشہور خاکہ نگار اور مزاح نگار ہیں۔
- ان کی پیدائش 1883ء میں دہلی میں ہوئی۔
- ڈپٹی نذیر احمد ان کے استاد تھے، جن کا خاکہ انہوں نے بڑی محبت اور خوبصورتی سے لکھا ہے۔

### اہم معروضی نکات (Objective Questions):
1. مرزا فرحت اللہ بیگ اور میاں دانی کس کے یہاں پڑھنے جاتے تھے؟
   **جواب:** ڈپٹی نذیر احمد کے یہاں۔
2. مرزا فرحت اللہ بیگ کی کون سی صنف نصاب میں شامل ہے؟
   **جواب:** خاکہ (نظیر احمد کی کہانی)۔
3. ڈپٹی نذیر احمد کس زبان کی تعلیم دیتے تھے؟
   **جواب:** عربی اور فارسی کی۔
4. فرحت اللہ بیگ کے ہم سبق کا کیا نام تھا؟
   **جواب:** میاں دانی۔

### انشائیہ کیا ہے؟
انشائیہ ادب کی وہ صنف ہے جس میں مصنف اپنے ذاتی خیالات، مشاہدات اور احساسات کو بے تکلف اور شگفتہ انداز میں پیش کرتا ہے۔ اس میں پلاٹ کا ہونا ضروری نہیں ہے۔
      """.trimIndent()
    )
  )

  fun getLiveClasses(): List<LiveClassEntity> = listOf(
    LiveClassEntity(
      id = 401L,
      titleHindi = "12वीं इतिहास: विजयनगर साम्राज्य - एक साम्राज्य की राजधानी",
      titleUrdu = "بارہویں تاریخ: وجے نگر سلطنت لائیو کلاس",
      subject = "History (इतिहास)",
      classLevel = "class_12_arts",
      teacherName = "डॉ. एस. एन. सिन्हा (Senior History Faculty)",
      scheduledTime = "08:00 PM Today",
      isLiveNow = true,
      viewerCount = 1420,
      streamTopic = "हम्पी की खोज, कृष्णदेव राय का शासनकाल एवं मंदिर वास्तुकला"
    ),
    LiveClassEntity(
      id = 402L,
      titleHindi = "10वीं गणित: त्रिकोणमिति के अनुप्रयोग (Height & Distance)",
      titleUrdu = "دسویں ریاضی: ٹرگنومیٹری لائیو سیشن",
      subject = "Mathematics",
      classLevel = "class_10",
      teacherName = "ई. के. के. सर (Gold Medalist Math Mentor)",
      scheduledTime = "09:30 AM Tomorrow",
      isLiveNow = false,
      viewerCount = 0,
      streamTopic = "उन्नयन और अवनमन कोण पर आधारित बोर्ड परीक्षा में 5 अंक वाले पक्के सवाल"
    ),
    LiveClassEntity(
      id = 403L,
      titleHindi = "12वीं उर्दू: ग़ज़लियात व असनाफ-ए-सुखन (غالب و اقبال)",
      titleUrdu = "بارہویں اردو: غالب و اقبال کی غزلوں کی تشریح",
      subject = "Urdu 100 Marks",
      classLevel = "class_12_arts",
      teacherName = "پروفیسر اے آر رحمن (صدر شعبہ اردو)",
      scheduledTime = "06:00 PM Tomorrow",
      isLiveNow = false,
      viewerCount = 0,
      streamTopic = "غالب اور علامہ اقبال کی منتخب غزلوں کی تفصیلی تشریح اور اہم اشعار"
    )
  )

  fun getTests(): List<TestEntity> = listOf(
    TestEntity(
      id = 501L,
      courseId = 1L,
      titleHindi = "बिहार बोर्ड 10वीं सामाजिक विज्ञान - अध्यायवार मेगा टेस्ट",
      titleUrdu = "دسویں سماجی سائنس میگا ٹیسٹ سیریز",
      subject = "Social Science",
      classLevel = "class_10",
      durationMinutes = 20,
      totalQuestions = 6,
      totalMarks = 30,
      passingMarks = 12
    ),
    TestEntity(
      id = 502L,
      courseId = 2L,
      titleHindi = "12वीं इतिहास: हड़प्पा सभ्यता एवं वैदिक काल मॉक टेस्ट",
      titleUrdu = "بارہویں تاریخ: ہڑپہ تہذیب موک ٹیسٹ",
      subject = "History",
      classLevel = "class_12_arts",
      durationMinutes = 25,
      totalQuestions = 6,
      totalMarks = 30,
      passingMarks = 15
    )
  )

  fun getQuestions(): List<QuestionEntity> = listOf(
    // Test 501 Questions
    QuestionEntity(
      id = 601L,
      testId = 501L,
      questionNumber = 1,
      questionHindi = "यूरोप का मरीज (Sick Man of Europe) किसे कहा जाता था?",
      questionUrdu = "یورپ کا مریض کس ملک کو کہا جاتا تھا؟",
      optionAHindi = "तुर्की",
      optionAUrdu = "ترکی",
      optionBHindi = "इटली",
      optionBUrdu = "اٹلی",
      optionCHindi = "इंग्लैंड",
      optionCUrdu = "انگلینڈ",
      optionDHindi = "फ्रांस",
      optionDUrdu = "فرانس",
      correctOptionIndex = 0,
      explanationHindi = "तुर्की (Turkey) को 19वीं सदी में ओटोमन साम्राज्य की कमजोरी के कारण 'यूरोप का मरीज' कहा जाता था।",
      explanationUrdu = "عثمانی سلطنت کے زوال کی وجہ سے ترکی کو یورپ کا مریض کہا جاتا تھا۔"
    ),
    QuestionEntity(
      id = 602L,
      testId = 501L,
      questionNumber = 2,
      questionHindi = "रक्त एवं लौह की नीति (Blood and Iron Policy) का अवलंबन किसने किया था?",
      questionUrdu = "خون اور لوہے کی پالیسی کس نے اختیار کی تھی؟",
      optionAHindi = "मेजिनी",
      optionAUrdu = "میزینی",
      optionBHindi = "हिटलर",
      optionBUrdu = "ہٹلر",
      optionCHindi = "बिस्मार्क",
      optionCUrdu = "بسمارک",
      optionDHindi = "विलियम प्रथम",
      optionDUrdu = "ولیم اول",
      correctOptionIndex = 2,
      explanationHindi = "प्रशा के चांसलर ऑटो वॉन बिस्मार्क ने जर्मनी के एकीकरण के लिए रक्त और लौह की नीति अपनाई थी।",
      explanationUrdu = "جرمنی کے اتحاد کیلئے بسمارک نے خون اور لوہے کی پالیسی کا اعلان کیا تھا۔"
    ),
    QuestionEntity(
      id = 603L,
      testId = 501L,
      questionNumber = 3,
      questionHindi = "वियना कांग्रेस (Vienna Congress) किस वर्ष आयोजित हुआ था?",
      questionUrdu = "ویانا کانگریس کس سال منعقد ہوئی تھی؟",
      optionAHindi = "1815 ई.",
      optionAUrdu = "1815ء",
      optionBHindi = "1820 ई.",
      optionBUrdu = "1820ء",
      optionCHindi = "1830 ई.",
      optionCUrdu = "1830ء",
      optionDHindi = "1848 ई.",
      optionDUrdu = "1848ء",
      correctOptionIndex = 0,
      explanationHindi = "नेपोलियन के पतन के पश्चात 1815 ई. में वियना कांग्रेस ऑस्ट्रिया की राजधानी वियना में आयोजित हुई थी।",
      explanationUrdu = "نپولین کی شکست کے بعد 1815ء میں آسٹریا کے دارالحکومت میں ویانا کانگریس منعقد ہوئی۔"
    ),
    QuestionEntity(
      id = 604L,
      testId = 501L,
      questionNumber = 4,
      questionHindi = "जर्मन राइन राज्य संघ (Confederation of the Rhine) का निर्माण किसने किया था?",
      questionUrdu = "جرمن رائن کنفیڈریشن کی بنیاد کس نے رکھی تھی؟",
      optionAHindi = "लुई 18वां",
      optionAUrdu = "لوئی 18واں",
      optionBHindi = "नेपोलियन बोनापार्ट",
      optionBUrdu = "نپولین بونا پارٹ",
      optionCHindi = "नेपोलियन तृतीय",
      optionCUrdu = "نپولین سوئم",
      optionDHindi = "बिस्मार्क",
      optionDUrdu = "بسمارک",
      correctOptionIndex = 1,
      explanationHindi = "नेपोलियन बोनापार्ट ने 39 छोटे जर्मन राज्यों को मिलाकर 1806 ई. में जर्मन राइन राज्य संघ का निर्माण किया था।",
      explanationUrdu = "نپولین بونا پارٹ نے جرمن ریاستوں کو ملا کر 1806 میں رائن یونین قائم کی تھی۔"
    ),
    QuestionEntity(
      id = 605L,
      testId = 501L,
      questionNumber = 5,
      questionHindi = "जालवेराइन (Zollverein) एक संस्था थी:",
      questionUrdu = "زولورین کس قسم کا ادارہ تھا؟",
      optionAHindi = "क्रांतिकारियों की",
      optionAUrdu = "انقلابیوں کا",
      optionBHindi = "व्यापारियों की",
      optionBUrdu = "تاجروں کا",
      optionCHindi = "विद्वानों की",
      optionCUrdu = "دانشوروں کا",
      optionDHindi = "पादरी और सामंतों की",
      optionDUrdu = "پادریوں کا",
      correctOptionIndex = 1,
      explanationHindi = "जालवेराइन प्रशा द्वारा 1834 ई. में स्थापित एक जर्मन शुल्क व व्यापार संघ (व्यापारियों की संस्था) थी।",
      explanationUrdu = "زولورین جرمن تاجروں کی ایک کسٹم یونین تھی جس نے معاشی اتحاد قائم کیا۔"
    ),
    QuestionEntity(
      id = 606L,
      testId = 501L,
      questionNumber = 6,
      questionHindi = "फ्रैंकफर्ट की संधि (Treaty of Frankfurt) किस वर्ष हुई थी?",
      questionUrdu = "فرینکفرٹ کا معاہدہ کس سال ہوا تھا؟",
      optionAHindi = "1864 ई.",
      optionAUrdu = "1864ء",
      optionBHindi = "1866 ई.",
      optionBUrdu = "1866ء",
      optionCHindi = "1870 ई.",
      optionCUrdu = "1870ء",
      optionDHindi = "1871 ई.",
      optionDUrdu = "1871ء",
      correctOptionIndex = 3,
      explanationHindi = "10 मई 1871 को फ्रैंकफर्ट की संधि हुई जिसके तहत फ्रांस ने अल्सास और लोरेन प्रशा को सौंप दिए।",
      explanationUrdu = "10 مئی 1871ء کو فرینکفرٹ کا معاہدہ ہوا جس کے بعد جرمنی کا اتحاد مکمل ہوا۔"
    )
  )

  fun getNotifications(): List<NotificationEntity> = listOf(
    NotificationEntity(
      id = 1L,
      title = "लाइव क्लास शुरू हो चुकी है! / لائیو کلاس شروع ہو چکی ہے",
      message = "12वीं इतिहास की विशेष लाइव क्लास डॉ. एस. एन. सिन्हा सर द्वारा अभी शुरू हो चुकी है। तुरंत जुड़ें।",
      timestamp = "अभी (Just Now)",
      isRead = false
    ),
    NotificationEntity(
      id = 2L,
      title = "नया टेस्ट जारी: बिहार बोर्ड 10वीं सामाजिक विज्ञान",
      message = "अध्याय 1 के 30 अंकों का वस्तुनिष्ठ टेस्ट उपलब्ध है। अपनी तैयारी परखें।",
      timestamp = "2 घंटे पहले",
      isRead = false
    ),
    NotificationEntity(
      id = 3L,
      title = "The Champaran Classes में आपका स्वागत है!",
      message = "बिहार बोर्ड मैट्रिक और इण्टर आर्ट्स की सर्वोच्च तैयारी के लिए सर्वश्रेष्ठ प्लेटफार्म।",
      timestamp = "कल",
      isRead = true
    )
  )

  fun getPayments(): List<PaymentEntity> = listOf(
    PaymentEntity(
      id = 1L,
      transactionId = "PAY_RZP_99182312",
      courseId = 1L,
      courseTitle = "बिहार बोर्ड 10वीं टॉपर बैच (सत्र 2026-27)",
      amount = 999,
      paymentMethod = "UPI (Google Pay)",
      status = "SUCCESS",
      timestampMillis = System.currentTimeMillis() - 86400000L
    ),
    PaymentEntity(
      id = 2L,
      transactionId = "PAY_RZP_88172940",
      courseId = 2L,
      courseTitle = "12वीं कला (Arts) संपूर्ण संकल्प बैच",
      amount = 1499,
      paymentMethod = "PhonePe UPI",
      status = "SUCCESS",
      timestampMillis = System.currentTimeMillis() - 172800000L
    )
  )
}
