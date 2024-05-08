package com.example.loginapp

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding


@Composable
fun LogoutButton(onLogOutCompleted: () -> Unit) {
    Button(
        onClick = {
            logOutUser(onLogOutCompleted)
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Text("Log Out")
    }
}

fun logOutUser(onLogOutCompleted: () -> Unit) {
    FirebaseAuth.getInstance().signOut()

    onLogOutCompleted()
}
