package com.rchdr.myapplication.adapter

import com.rchdr.myapplication.R
import com.rchdr.myapplication.data.response.ListStoryItem
import com.rchdr.myapplication.view.detail.DetailStoryActivity
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rchdr.myapplication.data.model.Story
import com.rchdr.myapplication.databinding.ItemRowStoryBinding


class Adapter : PagingDataAdapter<ListStoryItem, Adapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: ItemRowStoryBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(data: ListStoryItem) {
            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .centerCrop()
                .into(binding.ivItemImg)
            binding.tvItemUsername.text = data.name
            binding.tvItemDate.text = data.createdAt

            binding.root.setOnClickListener {
                val story = Story(
                    data.name,
                    data.photoUrl,
                    data.createdAt,
                    data.description,
                    data.lat,
                    data.lon
                )

                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    binding.root.context as Activity,
                    Pair(binding.ivItemImg, "img_story"),
                    Pair(binding.tvItemUsername, "name_story")
                )

                val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_DETAIL, story)
                binding.root.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null) {
            viewHolder.bind(data)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}