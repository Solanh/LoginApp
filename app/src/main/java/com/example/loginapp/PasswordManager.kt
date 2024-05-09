package com.example.loginapp

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PasswordManagerScreen(addPasswordButtonClicked: () -> Unit) {
    val dataList = remember { mutableStateListOf<DecryptedData>() }

    LaunchedEffect(Unit) { // Trigger the effect on composition
        fetchEncryptedData().collect { list ->
            dataList.clear()
            dataList.addAll(list)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        DisplayDecryptedData(dataList = dataList)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AddButton(onClick = addPasswordButtonClicked)
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPassword(backClicked: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var websiteOrApp by remember { mutableStateOf("") }


    val secretKey = KeystoreHelper.getSecretKey()

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
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            OutlinedTextField(
                value = websiteOrApp,
                onValueChange = { websiteOrApp = it },
                label = { Text("Website/App Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Button(
                onClick =

                {
                    coroutineScope.launch {
                        val encryptedEmail = CryptoUtils.encryptData(email, secretKey)
                        val encryptedUsername = CryptoUtils.encryptData(username, secretKey)
                        val encryptedPassword = CryptoUtils.encryptData(password, secretKey)
                        val encryptedWebsiteOrApp = CryptoUtils.encryptData(websiteOrApp, secretKey)

                        val passwordInfo = hashMapOf(
                            "email" to encryptedEmail,
                            "username" to encryptedUsername,
                            "password" to encryptedPassword,
                            "websiteOrApp" to encryptedWebsiteOrApp
                        )
                        addData(passwordInfo = passwordInfo, context = context)


                    }

                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Add Information")
            }
        }
    }
}

fun addData(context: Context, passwordInfo: HashMap<String, String>){
    db.collection("passwords").add(passwordInfo)
        .addOnSuccessListener { documentReference ->
            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            Toast.makeText(context, "Data was successfully added", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Log.e(ContentValues.TAG, "Error adding document", e)
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show()
        }
}


data class DecryptedData(
    val email: String,
    val username: String,
    val password: String,
    val websiteOrApp: String
)

@RequiresApi(Build.VERSION_CODES.O)
fun fetchEncryptedData(): Flow<List<DecryptedData>> = flow {
    val db = FirebaseFirestore.getInstance()
    val secretKey = KeystoreHelper.getSecretKey()
    val user =

    try {
        val documents = db.collection("passwords").get().await()
        val dataList = mutableListOf<DecryptedData>()

        for (document in documents) {
            val encryptedEmail = document.getString("email") ?: ""
            val encryptedUsername = document.getString("username") ?: ""
            val encryptedPassword = document.getString("password") ?: ""
            val encryptedWebsiteOrApp = document.getString("websiteOrApp") ?: ""

            val email = CryptoUtils.decryptData(encryptedEmail, secretKey)
            val username = CryptoUtils.decryptData(encryptedUsername, secretKey)
            val password = CryptoUtils.decryptData(encryptedPassword, secretKey)
            val websiteOrApp = CryptoUtils.decryptData(encryptedWebsiteOrApp, secretKey)

            dataList.add(DecryptedData(email, username, password, websiteOrApp))
        }
        emit(dataList)
    } catch (e: Exception) {
        println("Error fetching encrypted data: $e")
        emit(emptyList())
    }
}

@Composable
fun DisplayDecryptedData(dataList: List<DecryptedData>) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current


    Column {

        LazyColumn {
            items(dataList) { data ->
                var showPassword by rememberSaveable { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)  // Takes up all available space pushing the Checkbox and Button to the end
                                .padding(end = 8.dp)
                        ) {
                            Text("Email: ${data.email}", style = MaterialTheme.typography.bodyMedium)
                            Text("Username: ${data.username}", style = MaterialTheme.typography.bodyMedium)
                            Text("Website/App: ${data.websiteOrApp}", style = MaterialTheme.typography.bodyMedium)

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Password: ", style = MaterialTheme.typography.bodyLarge)
                                BasicTextField(
                                    value = data.password,
                                    onValueChange = {},
                                    readOnly = true,
                                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                    textStyle = LocalTextStyle.current.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            if (data.password.isEmpty()) {
                                                Text(
                                                    "No Password",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )
                            }
                        }


                            Text("Show Password", style = MaterialTheme.typography.labelMedium)
                            Checkbox(
                                checked = showPassword,
                                onCheckedChange = { showPassword = it }
                            )


                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(data.password))
                                Toast.makeText(context, "Password copied to clipboard!", Toast.LENGTH_SHORT).show()

                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_content_copy_24),
                                contentDescription = "Copy Password",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}