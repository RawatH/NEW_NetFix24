package com.netfix.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.netfix.models.UserVO
import com.netfix.models.network.response.login.UserLoginResponseModel


@Dao
interface UserLoginResponseDao {

    @Query("SELECT * FROM user_login_response")
    suspend fun getLoggedUser():List<UserLoginResponseModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(model: UserLoginResponseModel)

    @Query("DELETE FROM user_login_response")
    suspend fun deleteAll()
}