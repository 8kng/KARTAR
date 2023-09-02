package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.view.screen.auth.LoginActivity
import com.example.myapplication.view.screen.auth.NotLoggedInActivity
import com.example.myapplication.view.screen.profile.ProfileSetupActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Firebaseの初期化
        FirebaseApp.initializeApp(this)

    }

    override fun onStart() {
        super.onStart()

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val sharedPref = getSharedPreferences("UserInformation", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("UserName", "")

        if (user != null) {
            if (userName == "") {
                val intent = Intent(applicationContext, ProfileSetupActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        } else {
            val intent = Intent(applicationContext, NotLoggedInActivity::class.java)
            startActivity(intent)
        }

        binding.ARStartButton.setOnClickListener{
            val intent = Intent(applicationContext, AugmentedImageActivity::class.java)
            startActivity(intent)
        }
        binding.imageSaveButton.setOnClickListener{
            //val intent = Intent(applicationContext, ImageSaveActivity::class.java)
            val intent = Intent(applicationContext, NotLoggedInActivity::class.java)
            startActivity(intent)
        }
    }
}