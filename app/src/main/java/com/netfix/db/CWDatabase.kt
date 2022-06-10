package com.netfix.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.netfix.models.UserVO
import com.netfix.models.network.response.login.LoginResponseModel
import com.netfix.models.network.response.login.UserLoginResponseModel
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(UserVO::class , UserLoginResponseModel::class), version = 1, exportSchema = false)
@TypeConverters(Conveter::class)
abstract class CWDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userLoginResponseDao(): UserLoginResponseDao


    companion object {

        @Volatile
        private var INSTANCE: CWDatabase? = null

        fun getDatabase(context: Context): CWDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CWDatabase::class.java,
                    "cw_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

