package com.example.mytourism

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mytourism.Prevalent.Prevalent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserFragment : Fragment() {

    private var selectedImageUri: Uri? = null
    private lateinit var imageViewCircle: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_user, container, false)

        // Получаем ссылку на TextView username
        val usernameTextView = view.findViewById<TextView>(R.id.username)

        val userName = Paper.book().read(Prevalent.UserName, "")
        usernameTextView.text = userName

        setHasOptionsMenu(true)

        return view

    }


    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.exit)
            .setMessage(R.string.exit_2)
            .setPositiveButton(R.string.Yes) { _, _ ->
                // Переключаемся на другое окно
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton(R.string.No) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewCircle = view.findViewById(R.id.image_profile)
        imageViewCircle.setOnClickListener {
            openGallery()
        }

        val btnExit = view.findViewById<Button>(R.id.exit)

        btnExit.setOnClickListener {

            Paper.book().destroy()

            showExitConfirmationDialog()
        }

        val cartButton = view.findViewById<Button>(R.id.korsina)
        cartButton.setOnClickListener {
            val intent = Intent(requireContext(), CartActivity::class.java)
            startActivity(intent)
        }

        val myToursButtonn = view.findViewById<Button>(R.id.history_shop)
        myToursButtonn.setOnClickListener {
            val intent = Intent(requireContext(), PurchaseHistory::class.java)
            startActivity(intent)
        }

        val my_profilebtn = view.findViewById<Button>(R.id.myprofile)

        my_profilebtn.setOnClickListener {

            val intent = Intent(requireContext(), UserProfileSettings::class.java)
            startActivity(intent)

        }


        setupProfileImageClickListener()

    }

    private fun setupProfileImageClickListener() {
        imageViewCircle.setOnClickListener {
            showImageOptionsDialog()
        }
    }

    companion object {
        private const val REQUEST_CODE_GALLERY = 100
        private const val REQUEST_CODE_CAMERA = 101
        private const val REQUEST_CODE_STORAGE_PERMISSION = 100
        private const val REQUEST_CODE_CAMERA_PERMISSION = 101
    }


    private fun showImageOptionsDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.profile_photo_options)
            .setItems(requireContext().resources.getStringArray(R.array.profile_photo_options)) { _, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> openGallery()
                    2 -> deleteProfileImage()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
    }


    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA_PERMISSION)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }
    }


    private fun loadProfileImage() {
        selectedImageUri?.let { uri ->
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(imageViewCircle)
        } ?: run {
            imageViewCircle.setImageDrawable(null)
        }
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE_PERMISSION)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            data?.extras?.get("data")?.let { bitmap ->
                val file = File.createTempFile("profile_image", ".jpg", requireContext().cacheDir)
                val outputStream = FileOutputStream(file)
                (bitmap as Bitmap).compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                selectedImageUri = Uri.fromFile(file)
                loadProfileImage()
            }
        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                loadProfileImage()
            }
        }
    }

    private fun deleteProfileImage() {
        selectedImageUri = null
        Glide.with(this)
            .clear(imageViewCircle)
        imageViewCircle.setImageDrawable(null)
    }


    private fun saveProfileImageUri(uri: Uri?) {
        val sharedPreferences = requireContext().getSharedPreferences("profile_image", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            if (uri != null) {
                putString("profile_image_uri", uri.toString())
            } else {
                remove("profile_image_uri")
            }
            apply()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedImageUri?.let { uri ->
            saveProfileImageUri(uri)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("profile_image", Context.MODE_PRIVATE)
        val uriString = sharedPreferences.getString("profile_image_uri", null)
        selectedImageUri = uriString?.let { Uri.parse(it) }
        loadProfileImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        selectedImageUri?.let { uri ->
            saveProfileImageUri(uri)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_setting -> {
                val intent = Intent(requireActivity(), SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_help -> {
                val intent = Intent(requireActivity(), HelpActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}