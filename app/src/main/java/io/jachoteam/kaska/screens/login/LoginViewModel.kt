package io.jachoteam.kaska.screens.login

import android.app.Application
import android.arch.lifecycle.LiveData
import io.jachoteam.kaska.common.AuthManager
import io.jachoteam.kaska.common.SingleLiveEvent
import io.jachoteam.kaska.screens.common.BaseViewModel
import io.jachoteam.kaska.screens.common.CommonViewModel
import com.google.android.gms.tasks.OnFailureListener
import io.jachoteam.kaska.R

class LoginViewModel(private val authManager: AuthManager,
                     private val app: Application,
                     private val commonViewModel: CommonViewModel,
                     onFailureListener: OnFailureListener) : BaseViewModel(onFailureListener) {
    private val _goToHomeScreen = SingleLiveEvent<Unit>()
    val goToHomeScreen: LiveData<Unit> = _goToHomeScreen
    private val _goToRegisterScreen = SingleLiveEvent<Unit>()
    val goToRegisterScreen: LiveData<Unit> = _goToRegisterScreen

    fun onLoginClick(email: String, password: String) {
        if (validate(email, password)) {
            authManager.signIn(email, password).addOnSuccessListener {
                _goToHomeScreen.value = Unit
            }.addOnFailureListener(onFailureListener)
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email_and_password))
        }
    }

    private fun validate(email: String, password: String) =
            email.isNotEmpty() && password.isNotEmpty()

    fun onRegisterClick() {
        _goToRegisterScreen.call()
    }
}