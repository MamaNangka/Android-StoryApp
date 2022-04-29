package com.rchdr.myapplication.view.main

import android.content.Context
import android.content.Intent
import android.graphics.MaskFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rchdr.myapplication.R
import com.rchdr.myapplication.adapter.Adapter
import com.rchdr.myapplication.adapter.LoadingStateAdapter
import com.rchdr.myapplication.api.RetrofitApiConfig
import com.rchdr.myapplication.data.model.UserPreference
import com.rchdr.myapplication.data.response.ListStoryItem
import com.rchdr.myapplication.data.response.StoryResp
import com.rchdr.myapplication.data.viewmodel.AllViewModel
import com.rchdr.myapplication.data.viewmodel.StoryViewModel
import com.rchdr.myapplication.data.viewmodel.ViewModelFactory
import com.rchdr.myapplication.databinding.ActivityMainBinding
import com.rchdr.myapplication.view.add.AddStoryActivity
import com.rchdr.myapplication.view.auth.LoginActivity
import com.rchdr.myapplication.view.map.MapActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var MainViewModel: AllViewModel
    private lateinit var MainBinding: ActivityMainBinding
    private val MainViewModel1: StoryViewModel by viewModels {
        StoryViewModel.ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(MainBinding.root)
        setupViewModel()

        val layoutManager = LinearLayoutManager(this)
        MainBinding.rvStory.layoutManager = layoutManager

        getStories()

        val fab: View = findViewById(R.id.fab_post)
        fab.setOnClickListener { view ->
            val it = Intent(this, AddStoryActivity::class.java)
            startActivity(it)

        }
    }

    private fun setupViewModel() {
        MainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AllViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map_menu -> {
                val it = Intent(this, MapActivity::class.java)
                startActivity(it)
                return true
            }
            R.id.changeLang_menu -> {
                val it = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(it)
                return true
            }
            R.id.logout_menu -> {
                MainViewModel.logout()
                val it = Intent(this, LoginActivity::class.java)
                it.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(it)
                finish()
                return true
            }
        }
        return true

    }

    private fun getStories() {
        showProgressBar(true)
        val adapter = Adapter()
        MainBinding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        MainViewModel.getUser().observe(this) { user ->
            if(user != null) {
                MainViewModel1.story("Bearer " + user.token).observe(this) { story ->
                    adapter.submitData(lifecycle, story)
                }
            }
        }
        showProgressBar(false)
    }


    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            MainBinding.progressBar.visibility = View.VISIBLE
        } else {
            MainBinding.progressBar.visibility = View.GONE
        }
    }

}