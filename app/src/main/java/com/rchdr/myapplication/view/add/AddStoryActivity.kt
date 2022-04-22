package com.rchdr.myapplication.view.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rchdr.myapplication.data.response.ListStoryItem
import com.rchdr.myapplication.databinding.ActivityAddStoryBinding
import com.rchdr.myapplication.databinding.ActivityDetailStoryBinding

class AddStoryActivity : AppCompatActivity() {

    private lateinit var AddBinding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AddBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(AddBinding.root)
    }



}