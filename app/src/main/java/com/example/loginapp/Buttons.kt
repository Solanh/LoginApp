package com.example.loginapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Add , "Floating action button.")
    }
}

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