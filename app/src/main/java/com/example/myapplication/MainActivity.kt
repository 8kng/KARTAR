package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.view.screen.Auth.NotLoggedInActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        Log.d(ContentValues.TAG, "onStart()")

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