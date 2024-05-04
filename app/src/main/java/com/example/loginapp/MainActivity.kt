package com.example.loginapp

import com.example.loginapp.usernameChecker
import com.example.loginapp.emailChecker

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loginapp.ui.theme.LoginAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize() ,
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun UserLogin(){

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

    var emailExistsState by remember { mutableStateOf(false)}

    var usernameExistsState by remember { mutableStateOf(false)}

    Surface(){
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

            OutlinedTextField(
                value = email ,
                onValueChange = {
                    email = it
                } ,
                placeholder = { Text("Enter Email") }
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
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred during the query
                    println("Error checking email: $exception")
                }
            if (emailExistsState) {
                Text("Email already in use")}

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = username ,
                onValueChange = {
                    username = it
                    } ,
                placeholder = { Text("Enter Username") }
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
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred during the query
                    println("Error checking email: $exception")
                }
            if (usernameExistsState) {
                Text("Username already in use")}
            
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password ,
                onValueChange = {
                    password = it
                            } ,
                placeholder = {
                    Text("Enter Password")
                }
            )
            createAccountEnabled = username.isNotBlank() && password.isNotBlank()
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (createAccountEnabled) {
                    db.collection("users").add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG , "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG , "Error adding document" , e)
                        }
                }
            }){
                Text("Create Account")
            }
        }

    }
}




@Composable
fun Greeting(name: String , modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!" ,
        modifier = modifier
    )
    UserLogin()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginAppTheme {
        Greeting("Android")
        UserLogin()
    }
}