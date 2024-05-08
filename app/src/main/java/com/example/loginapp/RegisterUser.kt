import com.google.firebase.auth.FirebaseAuth

fun registerUser(email: String , password: String) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email , password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseAuth.getInstance().signOut()
            } else {
                // Handle errors
            }
        }
}