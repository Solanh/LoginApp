package com.example.loginapp

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth
import registerUser


@Composable
fun CreateAccount(backClicked: () -> Unit){

    val db = Firebase.firestore


    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var formattedEmail by remember { mutableStateOf(email.lowercase(Locale.ROOT)) }

    val user = hashMapOf(
        "email" to formattedEmail ,
        "username" to username

    )
    var createAccountEnabled by remember { mutableStateOf(false) }

    var createAccountFailed by remember { mutableStateOf(false) }

    var createAccountSuccessful by remember { mutableStateOf(false) }

    var correctInfo by remember { mutableStateOf(false) }

    var emailExistsState by remember { mutableStateOf(false) }

    var validEmail by remember { mutableStateOf(true) }

    var usernameExistsState by remember { mutableStateOf(false) }

    var correctPassword by remember { mutableStateOf(true) }




    Surface {
        Box(contentAlignment = Alignment.TopStart) {
            IconButton(onClick = backClicked) {
                Icon(imageVector = Icons.Filled.ArrowBack , contentDescription = null)
            }
        }
        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){



            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            if (isValidEmail(email)) {
                formattedEmail = email.lowercase(Locale.ROOT)
                user["email"] = formattedEmail
            }



            emailChecker(email.lowercase(Locale.ROOT))
                .addOnSuccessListener { emailExists ->
                    if (emailExists) {
                        emailExistsState = true
                        correctInfo = false
                        // Handle the case where the email exists
                    }
                    else {
                        correctInfo = true
                        emailExistsState = false
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred during the query
                    println("Error checking email: $exception")
                }
            if (emailExistsState && !createAccountSuccessful) {
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
                        correctInfo = false
                        // Handle the case where the email exists
                    }
                    else {
                        correctInfo = true
                        usernameExistsState = false
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred during the query
                    println("Error checking email: $exception")
                }

            if (usernameExistsState && !createAccountSuccessful) {
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
            if (!correctPassword){
                Text("Passwords do not match")
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = confirmPassword ,
                onValueChange = {
                    confirmPassword = it
                } ,
                placeholder = { Text("Confirm Password") },
                singleLine = true
            )


            LaunchedEffect(password, confirmPassword) {
                correctPassword = password == confirmPassword
            }






            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    validEmail = isValidEmail(email) // Validate email when button is clicked
                    if (validEmail) {
                        formattedEmail = email.lowercase(Locale.ROOT)
                    }

                    createAccountEnabled = username.isNotBlank() && password.isNotBlank() && correctPassword && !emailExistsState && validEmail && email.isNotBlank()

                    if (createAccountEnabled) {
                        db.collection("users").add(hashMapOf("email" to formattedEmail, "username" to username, "password" to password))
                            .addOnSuccessListener { documentReference ->
                                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                createAccountSuccessful = true
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding document", e)
                                createAccountFailed = true
                            }
                        registerUser(email = formattedEmail, password = password)
                    } else {
                        createAccountFailed = true
                    }

                },
                enabled = !createAccountSuccessful // Enable the button if create account is not successful
            ) {
                Text("Create Account")
            }



            Spacer(modifier = Modifier.height(20.dp))

            if (createAccountFailed && !createAccountSuccessful){
                Text("Fields not filled correctly")
            }
            if (!validEmail){
                Text("Invalid email")
            }
            if (createAccountSuccessful){
                Text("Account successfully created")
            }
        }

    }
}





