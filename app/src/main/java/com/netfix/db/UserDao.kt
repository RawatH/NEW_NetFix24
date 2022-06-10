package com.netfix.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.netfix.models.UserVO

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getLoggedUser():List<UserVO>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userVO: UserVO)

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}


interface B