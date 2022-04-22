package com.rchdr.myapplication.adapter

import com.rchdr.myapplication.R
import com.rchdr.myapplication.data.model.Story
import com.rchdr.myapplication.data.response.ListStoryItem
import com.rchdr.myapplication.view.detail.DetailStoryActivity

package com.rchdr.myapplication.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class StoriesAdapter(private val listStories: ArrayList<ListStoryItem>) : RecyclerView.Adapter<StoriesAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.iv_item_img)
        val name: TextView = view.findViewById(R.id.tv_item_username)
        val date: TextView = view.findViewById(R.id.tv_item_date)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_story, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = listStories[position].name
        viewHolder.date.text = listStories[position].createdAt
        Glide.with(viewHolder.itemView.context)
            .load(listStories[position].photoUrl)
            .centerCrop()
            .into(viewHolder.img)

        viewHolder.itemView.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    viewHolder.itemView.context as Activity,
                    Pair(viewHolder.img, "img_story"),
                    Pair(viewHolder.name, "name_story"),
                    Pair(viewHolder.date, "date_story")
                )

            val intent = Intent(viewHolder.itemView.context, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.EXTRA_DETAIL, listStories[position])
            viewHolder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return listStories.size
    }
}