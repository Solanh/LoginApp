package com.example.loginapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
fun OnboardingScreen(onSignInClicked: () -> Unit, onCreateAccountClicked: () -> Unit){

    Surface{
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            Button(onClick = onSignInClicked) {
                Text("Sign In")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onCreateAccountClicked){
                Text("Create Account")
            }
        }
    }
}