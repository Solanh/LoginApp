package com.example.loginapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SignIn(backClicked: () -> Unit, loginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface {
        Box(contentAlignment = Alignment.TopStart) {
            IconButton(onClick = backClicked) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                placeholder = { Text("Enter Username or Email") }
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = password ,
                onValueChange = { password = it } ,
                singleLine = true ,
                placeholder = { Text("Enter Password") } ,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation() ,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Row(modifier = Modifier.padding(16.dp)) {
                Checkbox(checked = showPassword, onCheckedChange = { showPassword = it })
                Text("Show Password", modifier = Modifier.padding(top = 12.dp))
            }
            Button(onClick = {
                loginUser(username = email, password = password, onLoginSuccess = loginSuccess, context = context)
            }) {
                Text("Sign In")
            }
        }
    }
}

fun loginUser(username: String, password: String, onLoginSuccess: () -> Unit, context: Context) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onLoginSuccess()
            } else {
                Toast.makeText(context , "Login failed: ${task.exception?.message}" , Toast.LENGTH_LONG).show()
            }
        }
}
