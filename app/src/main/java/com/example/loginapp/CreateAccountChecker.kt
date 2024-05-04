package com.example.loginapp

import android.annotation.SuppressLint
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


import com.google.firebase.firestore.FirebaseFirestore
@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore

fun usernameChecker(username: String): Task<Boolean> {

    // Query to check if username exists
    val usernameQuery = db.collection("users")
        .whereEqualTo("username", username)


    // Execute the username query
    return usernameQuery.get().continueWith { task ->
        if (task.isSuccessful) {
            // Retrieve the query snapshot
            val querySnapshot = task.result

            // Check if any documents exist in the query result
            !querySnapshot.isEmpty
        } else {
            // If the query fails, return false
            false
        }
    }

}




fun emailChecker(email: String): Task<Boolean> {
    // Query to check if email exists
    val emailQuery = db.collection("users")
        .whereEqualTo("email", email)

    return emailQuery.get().continueWith { task ->
        if (task.isSuccessful) {
            // Retrieve the query snapshot
            val querySnapshot = task.result

            // Check if any documents exist in the query result
            !querySnapshot.isEmpty
        } else {
            // If the query fails, return false
            false
        }
    }
}


