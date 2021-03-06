package com.j_gaby_1997.sendme.fragments.edit

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.DlgEditBinding
import com.j_gaby_1997.sendme.fragments.loading.LoadingDlg
import com.j_gaby_1997.sendme.services.saveUpdateDataUser
import com.j_gaby_1997.sendme.utils.checkForInternet
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("IntentReset")
class ProfileEditDlg: DialogFragment(R.layout.dlg_edit) {

    private var _b: DlgEditBinding? = null
    private val b get() = _b!!
    private var uriImage: Uri? = null
    private var email : String? = null
    private val loadingDialog: DialogFragment =  LoadingDlg()
    private val imageSelectionCall = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                result.data?.data?.let {
                    val bitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(it))
                    val resizedBitmap = bitmap.scale(bitmap.width/3, bitmap.height/3)

                    uriImage = Uri.parse(MediaStore.Images.Media.insertImage(requireActivity().contentResolver, resizedBitmap,"avatar", null))
                    Glide.with(b.profileImage)
                        .load(uriImage)
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .into(b.profileImage)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog.show(requireActivity().supportFragmentManager, "customDialog")
        _b = DlgEditBinding.bind(requireView())
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setFragmentResultListener("requestKey") { _, bundle ->
            b.edtUsername.text = Editable.Factory.getInstance().newEditable(bundle.getString("bundleName"))
            b.edtDescription.text = Editable.Factory.getInstance().newEditable(bundle.getString("bundleDescription"))
            b.edtLocation.text = Editable.Factory.getInstance().newEditable(bundle.getString("bundleLocation"))
            b.edtSite.text = Editable.Factory.getInstance().newEditable(bundle.getString("bundleWebSite"))
            email = bundle.getString("bundleEmail").toString()

            b.buttonSave.setOnClickListener { saveChanges(bundle.getString("bundleEmail").toString()) }
            b.buttonCamera.setOnClickListener { getImageFromGallery() }

            //Load image from Firebase
            val uri = bundle.getString("bundleAvatar")?.toUri()
            Firebase.storage.reference.child("avatars/${email}/${uri?.lastPathSegment}").downloadUrl.addOnSuccessListener {
                Glide.with(requireActivity())
                    .load(it)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(b.profileImage)
                requireActivity().supportFragmentManager.beginTransaction().remove(loadingDialog).commitAllowingStateLoss()
            }
        } //It collects the data to show profile edit.
    }

    // - METHODS -
    private fun saveChanges(email: String){
        if(!checkForInternet(requireActivity())) {
            Toast.makeText(requireActivity(), "No Internet connection", Toast.LENGTH_SHORT).show()
        }else{
            if(b.edtUsername.text.toString() == ""){
                Toast.makeText(requireActivity().application, "Required user name", Toast.LENGTH_LONG).show()
            }else{
                saveUpdateDataUser(email, uriImage, b.edtUsername.text.toString() , b.edtDescription.text.toString(), b.edtLocation.text.toString(), b.edtSite.text.toString())
                dismiss()
            }
        }
    }
    private fun getImageFromGallery(){
        if(!checkForInternet(requireActivity())) {
            Toast.makeText(requireActivity(), "No Internet connection", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/"
            imageSelectionCall.launch(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
