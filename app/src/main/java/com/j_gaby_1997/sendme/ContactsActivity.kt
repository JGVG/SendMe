package com.j_gaby_1997.sendme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.databinding.ActivityContactsBinding
import com.j_gaby_1997.sendme.fragments.contacts.ContactsFrag

class ContactsActivity : AppCompatActivity() {

    private val b: ActivityContactsBinding by lazy { ActivityContactsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        val bundle = intent.extras
        val email = bundle?.getString("email")

        if (savedInstanceState == null) {
            if (email != null) {
                showStartDestination(email)
            }else{
                onBackPressed()
                Toast.makeText(this, R.string.error_message_ilegal_access, Toast.LENGTH_LONG).show()
            }
        }
    }

    // - START APP SCREEN -
    private fun showStartDestination(email: String) {
        val contactsFrag = ContactsFrag.newInstance(email)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcContent, contactsFrag, null)
        }
    }
}
























