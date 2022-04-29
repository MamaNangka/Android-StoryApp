package com.rchdr.myapplication.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.rchdr.myapplication.R
import com.rchdr.myapplication.data.model.Story
import com.rchdr.myapplication.data.response.ListStoryItem
import com.rchdr.myapplication.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var DetailBinding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DetailBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(DetailBinding.root)
        showProgressBar(true)
        val story = intent.getParcelableExtra<Story>(EXTRA_DETAIL) as Story
        Glide.with(this)
            .load(story.photo)
            .into(DetailBinding.ivDetailImg)
        DetailBinding.tvDetailUsername.text = story.name
        DetailBinding.tvDetailDesc.text = story.description
        DetailBinding.tvDetailLocation.text = getString(R.string.coordinate)+ story.lat.toString()+", " + story.lon.toString()
        showProgressBar(false)
    }
    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            DetailBinding.progressBar.visibility = View.VISIBLE
        } else {
            DetailBinding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

}