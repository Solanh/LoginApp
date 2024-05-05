package com.example.loginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loginapp.ui.theme.LoginAppTheme


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
fun Main(){
    var showCreateAccount by remember {
        mutableStateOf(false)
    }
    var showSignIn by remember {
        mutableStateOf(false)
    }
    var showOnboardingScreen by remember {
        mutableStateOf(true)
    }
    if (showOnboardingScreen) {
        OnboardingScreen(
            onSignInClicked = { showSignIn = true
                              showOnboardingScreen = false} ,
            onCreateAccountClicked = { showCreateAccount = true
                                showOnboardingScreen = false}
        )
    }

    else if (showSignIn){
        SignIn(backClicked = {showOnboardingScreen = true
        showSignIn = false})

    }
    else if (showCreateAccount){
        CreateAccount(backClicked = {showOnboardingScreen = true
        showCreateAccount = false})
    }


}



@Composable
fun Greeting(name: String , modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!" ,
        modifier = modifier
    )
    Main()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginAppTheme {
        Greeting("Android")
        Main()
    }
}