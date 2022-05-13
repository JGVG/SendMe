package com.j_gaby_1997.sendme.fragments.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import coil.load
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.j_gaby_1997.sendme.ProfileActivity
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.DlgDetailBinding
import com.j_gaby_1997.sendme.fragments.loading.LoadingDlg

@RequiresApi(Build.VERSION_CODES.O)
class ContactDetailDlg: DialogFragment(R.layout.dlg_detail) {

    private var _b: DlgDetailBinding? = null
    private val b get() = _b!!
    private val loadingDialog: DialogFragment =  LoadingDlg()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = DlgDetailBinding.bind(requireView())
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.show(requireActivity().supportFragmentManager, "customDialog")

        setFragmentResultListener("requestKey") { _, bundle ->
            b.textUser.text = bundle.getString("bundleName")

            if(requireActivity().localClassName == "ProfileActivity"){
                b.iconName.visibility = View.GONE
            }else{
                b.iconName.setOnClickListener { navigateToProfileScreen(bundle.getString("bundleEmail").toString()) }
            }

            //Load image from Firebase
            val uri = bundle.getString("bundleAvatar")?.toUri()
            Firebase.storage.reference.child("avatars/${bundle.getString("bundleEmail").toString()}/${uri?.lastPathSegment}").downloadUrl.addOnSuccessListener {
                Glide.with(requireActivity())
                    .load(it)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(b.imageUser)
                requireActivity().supportFragmentManager.beginTransaction().remove(loadingDialog).commitAllowingStateLoss()
            }

        } //It collects the data to show profile details.
    }

    // - NAVIGATE -
    private fun navigateToProfileScreen(contactEmail:String){
        val appIntent = Intent(requireActivity().applicationContext, ProfileActivity::class.java).apply{
            putExtra("email", contactEmail)
        }
        dismiss()
        startActivity(appIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}
