package com.example.loginapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun SignIn(backClicked: () -> Unit){
    Surface {
        Box(contentAlignment = Alignment.TopStart) {
            IconButton(onClick = backClicked) {
                Icon(imageVector = Icons.Filled.ArrowBack , contentDescription = null)
            }
        }
        Column {

        }
    }
}