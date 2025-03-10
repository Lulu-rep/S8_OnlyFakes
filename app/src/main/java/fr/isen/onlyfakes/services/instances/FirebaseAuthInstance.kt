package fr.isen.onlyfakes.services.instances

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthInstance {
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}
