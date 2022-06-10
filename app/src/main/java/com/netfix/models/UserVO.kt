package com.netfix.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserVO(@PrimaryKey var iD_User:Int, var loginID:String, var fullName:String)
