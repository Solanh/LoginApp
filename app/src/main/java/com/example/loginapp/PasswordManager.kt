package com.example.loginapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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

@Composable
fun PasswordManagerScreen(){
    Surface(modifier = Modifier.fillMaxSize()){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            , contentAlignment = Alignment.BottomEnd){


            AddButton(onClick = {})


        }
    }

}

@Composable
fun AddPassword(){
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var websiteOrApp by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            placeholder = { Text("Enter Email") })
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            singleLine = true,
            placeholder = { Text("Enter username") })
        OutlinedTextField(
            value = websiteOrApp,
            onValueChange = { websiteOrApp = it },
            singleLine = true,
            placeholder = { Text("Enter Website or App") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            placeholder = { Text("Enter password") })

        Button(onClick = { /*TODO*/ }) {

        }

    }
}