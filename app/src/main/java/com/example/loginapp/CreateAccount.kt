package com.example.loginapp

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CreateAccount(backClicked: () -> Unit){

    val db = Firebase.firestore


    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }

    val user = hashMapOf(
        "email" to email ,
        "username" to username ,
        "password" to password ,
    )
    var createAccountEnabled by remember { mutableStateOf(false) }

    var emailExistsState by remember { mutableStateOf(false) }

    var usernameExistsState by remember { mutableStateOf(false) }

    Surface {
        Box(contentAlignment = Alignment.TopStart) {
            IconButton(onClick = backClicked) {
                Icon(imageVector = Icons.Filled.ArrowBack , contentDescription = null)
            }
        }
        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){



            OutlinedTextField(
                value = email ,
                onValueChange = {
                    email = it
                } ,
                placeholder = { Text("Enter Email") },
                singleLine = true
            )
            emailChecker(email)
                .addOnSuccessListener { emailExists ->
                    if (emailExists) {
                        emailExistsState = true
                        createAccountEnabled = false
                        // Handle the case where the email exists
                    }
                    else {
                        createAccountEnabled = true
                        emailExistsState = false
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred during the query
                    println("Error checking email: $exception")
                }
            if (emailExistsState) {
                Text("Email already in use")
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = username ,
                onValueChange = {
                    username = it
                } ,
                placeholder = { Text("Enter Username") },
                singleLine = true
            )

            usernameChecker(username)
                .addOnSuccessListener { usernameExists ->
                    if (usernameExists) {
                        usernameExistsState = true
                        createAccountEnabled = false
                        // Handle the case where the email exists
                    }
                    else {
                        createAccountEnabled = true
                        usernameExistsState = false
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred during the query
                    println("Error checking email: $exception")
                }
            if (usernameExistsState) {
                Text("Username already in use")
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password ,
                onValueChange = {
                    password = it
                } ,
                placeholder = { Text("Enter Password") },
                singleLine = true
            )
            createAccountEnabled = username.isNotBlank() && password.isNotBlank()
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (createAccountEnabled) {
                    db.collection("users").add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(ContentValues.TAG , "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG , "Error adding document" , e)
                        }
                }
            }){
                Text("Create Account")
            }
        }

    }
}
