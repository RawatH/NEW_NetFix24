package com.netfix.models.network.response.login

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.netfix.models.network.dataModel.UserPlanVO
import org.json.JSONArray

@Entity(tableName = "user_login_response")
data class UserLoginResponseModel(
    @PrimaryKey var iD_User: Int,
    var iD_UserType: Int,
    var loginID: String,
    var fullName: String,
    var userPlanPaymentDetails:List<UserPlanVO>
)