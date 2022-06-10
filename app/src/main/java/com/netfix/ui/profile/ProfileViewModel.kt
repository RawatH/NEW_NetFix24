package com.netfix.ui.profile

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.network.dataModel.ProfileVO
import com.netfix.ui.base.BaseViewModel

class ProfileViewModel (application: NFApplication): BaseViewModel(application) {
     val repository = ProfileRepository()
     val profileLiveData = MutableLiveData<ProfileVO>()

     fun getProfile(userId:Int){
       repository.getProfile(profileLiveData,userId)
     }

    fun updateProfile() {

        profileLiveData?.value?.let {
            it.createdBy = 5
            repository.updateProfile(profileLiveData, it)
        }
    }
}