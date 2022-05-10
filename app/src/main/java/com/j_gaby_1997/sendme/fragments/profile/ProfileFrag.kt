package com.j_gaby_1997.sendme.fragments.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.j_gaby_1997.sendme.AuthActivity
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.FragmentProfileBinding
import com.j_gaby_1997.sendme.fragments.detail.ContactDetailDlg
import com.j_gaby_1997.sendme.fragments.edit.ProfileEditDlg
import com.j_gaby_1997.sendme.fragments.loading.LoadingDlg

private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

@RequiresApi(Build.VERSION_CODES.O)
class ProfileFrag : Fragment(R.layout.fragment_profile){

    private var _b: FragmentProfileBinding? = null
    private val b get() = _b!!
    private val USEREMAIL: String by lazy {
        requireArguments().getString(ARG_USER_EMAIL, "null")
    }
    private val viewModel: ProfileViewModel by viewModels {
      ProfileViewModelFactory(LocalRepository(CurrentUserDatabase.getInstance(requireContext()).currentUserDao), this)
    }
    private val loadingDialog: DialogFragment =  LoadingDlg()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentProfileBinding.bind(requireView())
    }

    override fun onStart() {
        super.onStart()
        setupViews()
        observeCurrentUser()
    }

    // - SETUPS
    private fun setupViews(){
        loadingDialog.show(requireActivity().supportFragmentManager, "customDialog")
        b.buttonBack.setOnClickListener { navigateToContactScreen() }
        b.buttonSignOut.setOnClickListener { signOut() }
        b.fabNormal.setOnClickListener {
            navigateToChatScreen(USEREMAIL)
        }

        FirebaseFirestore.getInstance().collection("USUARIOS").document(USEREMAIL).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                val user = User()

                //User creation
                user.run {
                    name = snapshot.data!!["nombre"] as String
                    avatarURL = snapshot.data!!["avatar_url"] as String
                    description = snapshot.data!!["biografía"] as String
                    location = snapshot.data!!["direccion"] as String
                    dischargeDate = snapshot.data!!["fecha_alta"] as String
                    webSiteURL = snapshot.data!!["sitio_web"] as String
                    email = USEREMAIL
                }

                //Setups
                b.buttonEdit.setOnClickListener { showEditDialog(user)}
                b.profileImage.setOnClickListener { showDetailDialog(user) }
                b.textName.text = user.name
                b.textEmail.text = USEREMAIL
                b.textDischargeDate.text = String.format(getString(R.string.txtDischargeDate), user.dischargeDate)
                b.textDescript.text = user.description
                b.textHome.text = user.location
                b.textWebSiteURL.text = user.webSiteURL

                //Load image from Firebase
                Firebase.storage.reference.child("avatars/${user.avatarURL.toUri().lastPathSegment}").downloadUrl.addOnSuccessListener {
                    Glide.with(requireActivity())
                        .load(it)
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .into(b.profileImage)
                    requireActivity().supportFragmentManager.beginTransaction().remove(loadingDialog).commitAllowingStateLoss()
                }

                // Displays the contact/user.
                if(user.location == ""){
                    b.icoHome.visibility = View.INVISIBLE
                }else{
                    b.icoHome.visibility = View.VISIBLE
                }
                if(user.webSiteURL == ""){
                    b.icoWebSiteURL.visibility = View.GONE
                }else{
                    b.icoWebSiteURL.visibility = View.VISIBLE
                }
            }
        }

    }
    private fun observeCurrentUser(){
        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if (currentUser != null) {
                if(currentUser.email != USEREMAIL){
                    b.buttonSignOut.visibility = View.GONE
                    b.buttonEdit.visibility = View.GONE
                    b.fabNormal.visibility = View.VISIBLE
                }
            }
        }
    }

    // - NAVIGATE -
    private fun navigateToContactScreen() {
        requireActivity().onBackPressed()
    }
    private fun navigateToChatScreen( email: String ) {
        Toast.makeText(requireActivity().application, "To chat with: $email", Toast.LENGTH_LONG).show()
    }
    private fun showEditDialog(user: User){
        setFragmentResult("requestKey", bundleOf(
            "bundleEmail" to USEREMAIL,
            "bundleAvatar" to user.avatarURL,
            "bundleName" to user.name,
            "bundleDescription" to user.description,
            "bundleLocation" to user.location,
            "bundleWebSite" to user.webSiteURL
        ))
        ProfileEditDlg().show(requireActivity().supportFragmentManager, "customDialog")
    }
    private fun showDetailDialog(user: User) {
        setFragmentResult("requestKey", bundleOf(
            "bundleAvatar" to user.avatarURL,
            "bundleName" to user.name,
            "bundleEmail" to USEREMAIL
        ))
        ContactDetailDlg().show(requireActivity().supportFragmentManager, "customDialog")
    } //Display a dialog for the details of the selected user.

    // - METHODS -
    private fun signOut(){
        viewModel.signOutUser()
        requireActivity().finishAffinity()
        startActivity(Intent(requireActivity().applicationContext, AuthActivity::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object {
        fun newInstance(USEREMAIL: String): ProfileFrag = ProfileFrag().apply { arguments = bundleOf(ARG_USER_EMAIL to USEREMAIL) }
    }
}

