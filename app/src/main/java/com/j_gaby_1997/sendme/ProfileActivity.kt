package com.j_gaby_1997.sendme

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.databinding.ActivityProfileBinding
import com.j_gaby_1997.sendme.fragments.profile.ProfileFrag

@RequiresApi(Build.VERSION_CODES.O)
class ProfileActivity : AppCompatActivity() {

    private val b: ActivityProfileBinding by lazy { ActivityProfileBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
        val profileFrag = ProfileFrag.newInstance(email)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcContent, profileFrag, null)
        }
    }

}