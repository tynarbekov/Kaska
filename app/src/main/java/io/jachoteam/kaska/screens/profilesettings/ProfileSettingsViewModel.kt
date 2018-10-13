package io.jachoteam.kaska.screens.profilesettings

import io.jachoteam.kaska.common.AuthManager
import io.jachoteam.kaska.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener

class ProfileSettingsViewModel(private val authManager: AuthManager,
                               onFailureListener: OnFailureListener) :
        BaseViewModel(onFailureListener),
        AuthManager by authManager