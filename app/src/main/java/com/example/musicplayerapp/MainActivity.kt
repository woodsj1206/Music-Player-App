/*
App Name: MusicPlayerApp
Author: woodsj1206 (https://github.com/woodsj1206)
Description: This app uses the DeezerAPI to showcase the top 100 global music tracks. Users can preview a selected track for 30 seconds.
Course: CIS 436
Date Created: 4/23/24
Last Modified: 4/24/24
*/
package com.example.musicplayerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}