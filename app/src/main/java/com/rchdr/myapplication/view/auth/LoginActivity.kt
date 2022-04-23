package com.rchdr.myapplication.view.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.rchdr.myapplication.R
import com.rchdr.myapplication.api.RetrofitApiConfig
import com.rchdr.myapplication.data.model.User
import com.rchdr.myapplication.data.model.UserPreference
import com.rchdr.myapplication.data.response.LoginResp
import com.rchdr.myapplication.data.viewmodel.AllViewModel
import com.rchdr.myapplication.data.viewmodel.ViewModelFactory
import com.rchdr.myapplication.databinding.ActivityLoginBinding
import com.rchdr.myapplication.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var LoginBinding: ActivityLoginBinding
    private lateinit var LoginViewModel: AllViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(LoginBinding.root)

        LoginBinding.loginTv2.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        setupViewModel()
        LoginBinding.btnLogin.setOnClickListener {
            val inputEmail = LoginBinding.etLoginEmail.text.toString()
            val inputPassword = LoginBinding.etLoginPass.text.toString()

            login(inputEmail, inputPassword)
        }
        LoginBinding.etLoginEmail.check = "email"
        LoginBinding.etLoginPass.check = "password"

    }



    private fun setupViewModel() {
        LoginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AllViewModel::class.java]

        LoginViewModel.getUser().observe(this) { user ->
            if(user.isLogin) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val logoutMenu = menu.findItem(R.id.logout_menu)

        logoutMenu.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changeLang_menu -> {
                val it = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(it)
                return true
            }
        }
        return true

    }

    private fun login(inputEmail: String, inputPassword: String) {
        showLoading(true)

        val client = RetrofitApiConfig.getApiService().postLogin(inputEmail, inputPassword)
        client.enqueue(object: Callback<LoginResp> {
            override fun onResponse(call: Call<LoginResp>, response: Response<LoginResp>) {
                showLoading(false)
                val responseBody = response.body()
                Log.d(TAG, "onResponse: $responseBody")
                if(response.isSuccessful && responseBody?.message == "success") {
                    LoginViewModel.setUser(User(responseBody.loginResult.token, true))
                    Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                    Toast.makeText(this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResp>, t: Throwable) {

                showLoading(false)
                Log.e(TAG, "onFailure2: ${t.message}")
                Toast.makeText(this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            LoginBinding.progressBar.visibility = View.VISIBLE
        } else {
            LoginBinding.progressBar.visibility = View.GONE
        }
    }
    companion object {
        private const val TAG = "Main Activity"
    }
}