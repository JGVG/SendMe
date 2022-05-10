package com.j_gaby_1997.sendme

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.databinding.ActivitySearchBinding
import com.j_gaby_1997.sendme.fragments.contacts.ContactsFrag
import com.j_gaby_1997.sendme.fragments.search.SearchFrag

@RequiresApi(Build.VERSION_CODES.O)
class SearchActivity : AppCompatActivity() {

    private val b: ActivitySearchBinding by lazy { ActivitySearchBinding .inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        val bundle = intent.extras
        val email = bundle?.getString("email")

        if (savedInstanceState == null) {
            if (email != null) {
                showStartDestination(email)
            }else{
                Toast.makeText(this, R.string.error_message_ilegal_access, Toast.LENGTH_LONG).show()
                onBackPressed()
            }
        }
    }

    // - START APP SCREEN -
    private fun showStartDestination(email: String) {
        val searchFrag = SearchFrag.newInstance(email)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcContent, searchFrag, null)
        }
    }
}