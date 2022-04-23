package com.rchdr.myapplication.view.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.rchdr.myapplication.R
import com.rchdr.myapplication.api.RetrofitApiConfig
import com.rchdr.myapplication.data.model.UserPreference
import com.rchdr.myapplication.data.response.StoryResp
import com.rchdr.myapplication.data.viewmodel.*
import com.rchdr.myapplication.databinding.ActivityAddStoryBinding
import com.rchdr.myapplication.view.main.MainActivity
import com.rchdr.myapplication.view.reduceFileImage
import com.rchdr.myapplication.view.rotateBitmap
import com.rchdr.myapplication.view.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryActivity : AppCompatActivity() {

    private lateinit var AddBinding: ActivityAddStoryBinding
    private lateinit var AddViewModel: AllViewModel
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AddBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(AddBinding.root)

        setupViewModel()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        AddBinding.ibAddCamera.setOnClickListener {
            startCamera()
        }

        AddBinding.ibAddGallery.setOnClickListener {
            startGallery()
        }

        AddBinding.btnPost.setOnClickListener {
            uploadImage()
        }
    }

    private fun setupViewModel() {
        AddViewModel = ViewModelProvider(this,ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AllViewModel::class.java]
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.denied), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        showProgressBar(true)

        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description = AddBinding.etAddDescription.text.toString()
                .toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            AddViewModel.getUser().observe(this) {
                if (it != null) {
                    val client = RetrofitApiConfig.getApiService()
                        .postStory("Bearer " + it.token,  description, imageMultipart)
                    client.enqueue(object : Callback<StoryResp> {
                        override fun onResponse(
                            call: Call<StoryResp>,
                            response: Response<StoryResp>
                        ) {
                            showProgressBar(false)
                            val responseBody = response.body()
                            Log.d(TAG, "onResponse: $responseBody")
                            if (response.isSuccessful && responseBody?.message == "Story created successfully") {
                                Toast.makeText(
                                    this@AddStoryActivity,
                                    getString(R.string.upload_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.e(TAG, "onFailure1: ${response.message()}")
                                Toast.makeText(
                                    this@AddStoryActivity,
                                    getString(R.string.upload_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<StoryResp>, t: Throwable) {
                            showProgressBar(false)
                            Log.e(TAG, "onFailure2: ${t.message}")
                            Toast.makeText(
                                this@AddStoryActivity,
                                getString(R.string.upload_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }

        }

    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            AddBinding.ivAddImg.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            AddBinding.ivAddImg.setImageURI(selectedImg)
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            AddBinding.progressBar.visibility = View.VISIBLE
        } else {
            AddBinding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "AddStoryActivity"
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}



