package hr.algebra.formula1data.provider

import android.content.Context
import androidx.room.Room
import hr.algebra.formula1data.db.F1Database

object F1DatabaseProvider {

    @Volatile
    private var INSTANCE: F1Database? = null

    fun getDatabase(context: Context): F1Database {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                F1Database::class.java,
                "f1_database"
            ).fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}