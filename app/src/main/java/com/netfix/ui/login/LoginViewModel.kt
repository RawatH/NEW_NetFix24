package com.netfix.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.netfix.app.NFApplication
import com.netfix.db.CWDatabase
import com.netfix.models.UserVO
import com.netfix.models.network.request.login.LoginRequestModel
import com.netfix.models.network.response.login.LoginResponseModel
import com.netfix.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class LoginViewModel(application:NFApplication): BaseViewModel(application) {
    private  val TAG = "LoginViewModel"
    var loginReqLiveData:MutableLiveData<LoginResponseModel> = MutableLiveData<LoginResponseModel>()
    var respository:LoginRepository = LoginRepository()


    fun validateUser(loginRequestModel: LoginRequestModel){
        respository.validateUser(loginRequestModel,loginReqLiveData)
    }

    fun persistLoggedUser() {
        Log.d(TAG, "persistLoggedUser: ")
        val db = CWDatabase.getDatabase(getApplication())
        viewModelScope.launch {

            loginReqLiveData.value?.response?.let {
//                var userId = it.iD_User
//                var loginID = it.loginID
//                var fullName = it.fullName
//                var userVO = UserVO(userId,loginID,fullName)
                db.userLoginResponseDao().insert(it)
                Log.d(TAG, "persistLoggedUser: "+db.userDao().getLoggedUser())
            }



        }

    }

}