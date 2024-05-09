package com.example.loginapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PasswordManagerScreen(addPasswordButtonCLicked: () -> Unit){
    var showAddPassword by rememberSaveable {
        mutableStateOf(false)
    }
    Surface(modifier = Modifier.fillMaxSize()){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            , contentAlignment = Alignment.BottomEnd){
            
            AddButton(onClick = addPasswordButtonCLicked)


        }
    }

}

@Composable
fun AddPassword(backClicked: () -> Unit){
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var websiteOrApp by remember { mutableStateOf("") }

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
                placeholder = { Text("Enter Email") })
            Spacer(Modifier.padding(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true,
                placeholder = { Text("Enter username") })
            Spacer(Modifier.padding(16.dp))
            OutlinedTextField(
                value = websiteOrApp,
                onValueChange = { websiteOrApp = it },
                singleLine = true,
                placeholder = { Text("Enter Website/App name") })
            Spacer(Modifier.padding(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                placeholder = { Text("Enter password") })
            Spacer(Modifier.padding(16.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Add Information")
            }
        }

    }
}