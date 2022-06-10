package com.netfix.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.netfix.app.NFApplication

class LoginViewModelFactory(val application:NFApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NFApplication::class.java).newInstance(application)
    }

}