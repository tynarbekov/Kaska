package io.jachoteam.kaska.common.firebase

import io.jachoteam.kaska.common.AuthManager
import io.jachoteam.kaska.common.toUnit
import io.jachoteam.kaska.data.firebase.common.auth
import com.google.android.gms.tasks.Task

class FirebaseAuthManager : AuthManager {
    override fun signOut() {
        auth.signOut()
    }

    override fun signIn(email: String, password: String): Task<Unit> =
        auth.signInWithEmailAndPassword(email, password).toUnit()
}