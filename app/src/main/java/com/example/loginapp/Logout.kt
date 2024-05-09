package com.example.loginapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment

@Composable
fun LogoutScreen(onLogOutCompleted: () -> Unit){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column (verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){
            LogoutButton(onLogOutCompleted = onLogOutCompleted)
        }
    }
}


fun logOutUser(onLogOutCompleted: () -> Unit) {
    FirebaseAuth.getInstance().signOut()

    onLogOutCompleted()
}
