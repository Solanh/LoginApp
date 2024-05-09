package com.example.loginapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object UserData {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    val userEmail: String?
        get() = firebaseAuth.currentUser?.email
}
fun getUsernameByEmail(email: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                onError(Exception("No user found with email $email"))
            } else {
                // Assuming email is unique and only one document will be returned
                val user = documents.documents[0]
                val username = user.getString("username") ?: ""
                onSuccess(username)
            }
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}
