package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    UserEntity::class,
    CourseEntity::class,
    LectureEntity::class,
    PdfNoteEntity::class,
    LiveClassEntity::class,
    TestEntity::class,
    QuestionEntity::class,
    TestAttemptEntity::class,
    PaymentEntity::class,
    NotificationEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun courseDao(): CourseDao
  abstract fun lectureDao(): LectureDao
  abstract fun pdfNoteDao(): PdfNoteDao
  abstract fun liveClassDao(): LiveClassDao
  abstract fun testDao(): TestDao
  abstract fun paymentDao(): PaymentDao
  abstract fun notificationDao(): NotificationDao
  abstract fun userDao(): UserDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "champaran_classes_db"
        )
          .addCallback(DatabaseCallback(scope))
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateDatabase(database)
          }
        }
      }
    }

    suspend fun populateDatabase(db: AppDatabase) {
      db.courseDao().insertCourses(InitialData.getCourses())
      db.lectureDao().insertLectures(InitialData.getLectures())
      db.pdfNoteDao().insertNotes(InitialData.getPdfNotes())
      db.liveClassDao().insertLiveClasses(InitialData.getLiveClasses())
      db.testDao().insertTests(InitialData.getTests())
      db.testDao().insertQuestions(InitialData.getQuestions())
      db.notificationDao().insertNotifications(InitialData.getNotifications())
      for (payment in InitialData.getPayments()) {
        db.paymentDao().insertPayment(payment)
      }
    }
  }
}
