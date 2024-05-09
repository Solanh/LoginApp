package com.example.loginapp

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String,

)

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Settings: Screen("settings")
    data object Logout: Screen("logout")
    data object PasswordManager: Screen("password manager")
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(navController: NavHostController, onLogOutCompleted: () -> Unit, onAddPasswordClicked: () -> Unit) {
    val items = listOf(
        NavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Screen.Home.route
        ),
        NavigationItem(
            title = "Password Manager",
            selectedIcon = Icons.Filled.Lock,
            unselectedIcon = Icons.Outlined.Lock,
            route = Screen.PasswordManager.route
        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = Screen.Settings.route
        ),
        NavigationItem(
            title = "Logout",
            selectedIcon = Icons.Filled.ExitToApp,
            unselectedIcon = Icons.Outlined.ExitToApp,
            route = Screen.Logout.route


        )
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    ModalNavigationDrawer(
        drawerContent = {

            ModalDrawerSheet(modifier = Modifier.width(modularAppDrawerWidth())){
                DrawerHeader()
                Divider(Modifier.height(2.dp))
                Spacer(Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                               Icon(
                                   imageVector = if (index == selectedItemIndex) {
                                       item.selectedIcon
                                   }else item.unselectedIcon,
                                   contentDescription = item.title)
                        },
                        badge = item.badgeCount?.let { { Text(it.toString()) } },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("App") } ,
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu , "Menu")
                        }
                    } ,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer ,

                    )
                )

            }
        ) {innerPadding ->
            MainContent(
                navController,
                onLogOutCompleted = onLogOutCompleted,
                padding = innerPadding,
                onAddPasswordClicked = onAddPasswordClicked
            )
        }
    }
}



@Composable
fun DrawerHeader() {
    val email = UserData.userEmail
    var username by remember { mutableStateOf("Loading...")}
    LaunchedEffect(email) {
        if (email != null) {
            getUsernameByEmail(
                email = email,
                onSuccess = { fetchedUsername ->
                    username = fetchedUsername
                },
                onError = { exception ->
                    Log.e("Firestore", "Error fetching user", exception)
                    username = "Error"
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray , shape = RoundedCornerShape(50.dp)) ,
            contentAlignment = Alignment.Center
        ) {
            Text(username , fontWeight = FontWeight.Bold , color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(username , fontWeight = FontWeight.Bold)
        Text("$email", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun MainContent(navController: NavHostController, onLogOutCompleted: () -> Unit, padding: PaddingValues, onAddPasswordClicked: () -> Unit) {
    NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(padding)) {
        composable(
            Screen.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            HomeScreen()
        }
        composable(
            Screen.Settings.route ,
            enterTransition = { EnterTransition.None } ,
            exitTransition = { ExitTransition.None }
        ) {
            SettingsScreen()
        }
        composable(
            Screen.Logout.route ,
            enterTransition = { EnterTransition.None } ,
            exitTransition = { ExitTransition.None }
        ) {
            LogoutScreen(onLogOutCompleted = onLogOutCompleted)
        }
        composable(
            Screen.PasswordManager.route ,
            enterTransition = { EnterTransition.None } ,
            exitTransition = { ExitTransition.None }
        ) {
            PasswordManagerScreen(onAddPasswordClicked)
        }
    }
}

@Composable
fun modularAppDrawerWidth(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    return screenWidth * 0.8f

}

@Preview(showBackground = true)
@Composable
fun PreviewAppDrawer() {
    val navController = rememberNavController()
    AppDrawer(navController = navController, onLogOutCompleted = {}, onAddPasswordClicked = {})
}


