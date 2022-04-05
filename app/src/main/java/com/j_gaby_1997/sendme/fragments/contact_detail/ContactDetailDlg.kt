package com.j_gaby_1997.sendme.fragments.contact_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import coil.load
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.ContactDetailDlgBinding

class ContactDetailDlg: DialogFragment(R.layout.contact_detail_dlg) {

    private var _b: ContactDetailDlgBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = ContactDetailDlgBinding.bind(requireView())

        //It collects the data to show profile details.
        setFragmentResultListener("requestKey") { _, bundle ->
            b.imageUser.load(bundle.getString("bundleAvatar"))
            b.textUser.text = bundle.getString("bundleName")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}
