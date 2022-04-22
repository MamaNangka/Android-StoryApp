package com.rchdr.myapplication.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rchdr.myapplication.data.response.ListStoryItem
import com.rchdr.myapplication.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var DetailBinding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DetailBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(DetailBinding.root)

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_DETAIL) as ListStoryItem
        Glide.with(this)
            .load(story.photoUrl)
            .into(DetailBinding.ivDetailImg)
        DetailBinding.tvDetailUsername.text = story.name
        DetailBinding.tvDetailDesc.text = story.description
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

}