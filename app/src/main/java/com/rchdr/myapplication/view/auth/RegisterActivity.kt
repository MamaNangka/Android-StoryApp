package com.rchdr.myapplication.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.rchdr.myapplication.R
import com.rchdr.myapplication.api.RetrofitApiConfig
import com.rchdr.myapplication.data.response.RegisterResp
import com.rchdr.myapplication.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var RegisterBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(RegisterBinding.root)

        RegisterBinding.registerTv2.setOnClickListener {
            Intent(this@RegisterActivity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
        RegisterBinding.etRegisterName.check = "name"
        RegisterBinding.etRegisterEmail.check = "email"
        RegisterBinding.etRegisterPass.check = "password"

        RegisterBinding.btnRegister.setOnClickListener {
            val inputName = RegisterBinding.etRegisterName.text.toString()
            val inputEmail = RegisterBinding.etRegisterEmail.text.toString()
            val inputPassword = RegisterBinding.etRegisterPass.text.toString()

            createAccount(inputName, inputEmail, inputPassword)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val logoutMenu = menu.findItem(R.id.logout_menu)
        val mapMenu = menu.findItem(R.id.map_menu)
        logoutMenu.isVisible = false
        mapMenu.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.changeLang_menu -> {
                val it = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(it)
                return true
            }
        }
        return true
    }


    private fun createAccount(inputName: String, inputEmail: String, inputPassword: String) {
        showProgressBar(true)

        val client = RetrofitApiConfig.getApiService().postRegister(inputName, inputEmail, inputPassword)
        client.enqueue(object: Callback<RegisterResp> {
            override fun onResponse(
                call: Call<RegisterResp>,
                response: Response<RegisterResp>
            ) {
                showProgressBar(false)
                val responseBody = response.body()
                Log.d(TAG, "onResponse: $responseBody")
                if(response.isSuccessful && responseBody?.message == "User created") {
                    Toast.makeText(this@RegisterActivity, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                    Toast.makeText(this@RegisterActivity, getString(R.string.register_failed), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResp>, t: Throwable) {
                showProgressBar(false)
                Log.e(TAG, "onFailure2: ${t.message}")
                Toast.makeText(this@RegisterActivity, getString(R.string.register_failed), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            RegisterBinding.progressBar.visibility = View.VISIBLE
        } else {
            RegisterBinding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "Register Activity"
    }
}