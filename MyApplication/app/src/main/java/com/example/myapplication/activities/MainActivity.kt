package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.GoogleSignInViewModel
import com.example.myapplication.R
import com.example.myapplication.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private lateinit var auth: FirebaseAuth
    private val viewModel: GoogleSignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<SignInButton>(R.id.btnGoogleSignIn).setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        firebaseUser?.let {
                            val user = User(
                                uid = it.uid,
                                name = it.displayName ?: "",
                                email = it.email ?: ""
                            )
                            viewModel.insertUser(user)
                            Toast.makeText(this, "Welcome ${it.displayName}", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, UserDetailsActivity::class.java))

                        }
                    } else {
                        Toast.makeText(this, "Auth Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: ApiException) {
                e.printStackTrace()
                //todo remove this
                startActivity(Intent(this, UserDetailsActivity::class.java))

            }
        }
    }
}
