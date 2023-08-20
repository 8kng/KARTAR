package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding

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
            val intent = Intent(applicationContext, SecondActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "yeah", Toast.LENGTH_SHORT).show()
        }
    }
}