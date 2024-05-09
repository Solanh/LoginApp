package com.example.loginapp


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.ui.theme.LoginAppTheme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginAppTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Main()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main() {
    val user = FirebaseAuth.getInstance().currentUser
    var showOnboardingScreen by rememberSaveable { mutableStateOf(user == null) }
    var showSignIn by rememberSaveable { mutableStateOf(false) }
    var showCreateAccount by rememberSaveable { mutableStateOf(false) }
    var showHomeScreen by rememberSaveable { mutableStateOf(user != null) }
    val navController = rememberNavController()
    var showAddPassword by rememberSaveable { mutableStateOf(false) }


    when {

        showOnboardingScreen -> OnboardingScreen(
            onSignInClicked = {
                showSignIn = true
                showOnboardingScreen = false
            },
            onCreateAccountClicked = {
                showCreateAccount = true
                showOnboardingScreen = false
            }
        )
        showSignIn -> SignIn(
            backClicked = {
                showOnboardingScreen = true
                showSignIn = false
            },
            loginSuccess = {
                showHomeScreen = true
                showSignIn = false
            }
        )
        showCreateAccount -> CreateAccount(
            backClicked = {
                showOnboardingScreen = true
                showCreateAccount = false
            }
        )
        showHomeScreen -> AppDrawer(
            navController = navController,
            onLogOutCompleted = {
                showOnboardingScreen = true
                showHomeScreen = false
            },
            onAddPasswordClicked = {
                showAddPassword = true
                showHomeScreen = false
            }
        )
        showAddPassword -> AddPassword(
            backClicked = {
                showHomeScreen = true
                showAddPassword = false

            }
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginAppTheme {
        Greeting("Android")
    }
}
