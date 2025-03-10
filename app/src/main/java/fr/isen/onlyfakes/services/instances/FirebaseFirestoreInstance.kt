package fr.isen.onlyfakes.services.instances

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseFirestoreInstance {
    val db by lazy {
        FirebaseFirestore.getInstance()
    }
}