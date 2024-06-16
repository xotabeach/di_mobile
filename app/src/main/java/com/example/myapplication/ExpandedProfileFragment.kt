package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.myapplication.data.model.DatabaseHelper
import java.io.ByteArrayOutputStream

class ExpandedProfileFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var userEmail: String
    private lateinit var avatarImageView: ImageView
    private var avatarBase64: String? = null

    companion object {
        private const val ARG_USER_EMAIL = "user_email"
        private const val REQUEST_CODE_GALLERY = 1001

        fun newInstance(email: String): ExpandedProfileFragment {
            val fragment = ExpandedProfileFragment()
            val args = Bundle()
            args.putString(ARG_USER_EMAIL, email)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expanded_profile, container, false)

        databaseHelper = DatabaseHelper(requireContext())
        userEmail = arguments?.getString(ARG_USER_EMAIL) ?: ""

        val user = databaseHelper.getUserByEmail(userEmail)

        val lastnameField = view.findViewById<EditText>(R.id.lastnameField)
        val firstnameField = view.findViewById<EditText>(R.id.firstnameField)
        val middlenameField = view.findViewById<EditText>(R.id.middlenameField)
        val birthdateField = view.findViewById<EditText>(R.id.birthdateField)
        val phoneField = view.findViewById<EditText>(R.id.phoneField)
        val emailField = view.findViewById<EditText>(R.id.emailField)
        avatarImageView = view.findViewById<ImageView>(R.id.profileImage)

        if (user != null) {
            lastnameField.setText(user.surname)
            firstnameField.setText(user.name)
            //middlenameField.setText(user.middlename)
            //birthdateField.setText(user.birthdate)
            phoneField.setText(user.phone)
            emailField.setText(user.mail)

            // Загрузка аватара из базы данных
            if (user.image.isNotEmpty()) {
                val imageBytes = Base64.decode(user.image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                avatarImageView.setImageBitmap(bitmap)
                avatarBase64 = user.image
            }
        }

        avatarImageView.setOnClickListener {
            openGallery()
        }

        val saveButton = view.findViewById<CardView>(R.id.saveButton)
        saveButton.setOnClickListener {
            val updatedLastname = lastnameField.text.toString()
            val updatedFirstname = firstnameField.text.toString()
            val updatedMiddlename = middlenameField.text.toString()
            val updatedBirthdate = birthdateField.text.toString()
            val updatedPhone = phoneField.text.toString()
            val updatedEmail = emailField.text.toString()

            databaseHelper.updateUser(
                userEmail, updatedLastname, updatedFirstname, updatedMiddlename, updatedBirthdate, updatedPhone, updatedEmail, avatarBase64
            )

            Toast.makeText(context, "Профиль обновлен", Toast.LENGTH_SHORT).show()
        }

        val cancelButton = view.findViewById<CardView>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                val roundedBitmap = getRoundedCroppedBitmap(bitmap)
                avatarImageView.setImageBitmap(roundedBitmap)
                avatarBase64 = bitmapToBase64(roundedBitmap)
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getRoundedCroppedBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val radius = if (width < height) width / 2 else height / 2

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = 0xff424242.toInt()
        val paint = Paint()
        val rect = Rect(0, 0, width, height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color

        canvas.drawCircle(width / 2f, height / 2f, radius.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }
}
