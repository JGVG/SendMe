package com.j_gaby_1997.sendme

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.fragments.log_in.LogInFrag
import com.j_gaby_1997.sendme.fragments.log_in.LogInViewModel
import com.j_gaby_1997.sendme.fragments.log_in.LogInViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private val viewModel: LogInViewModel by viewModels {
        LogInViewModelFactory(LocalRepository(CurrentUserDatabase.getInstance(this).currentUserDao), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAndStart()
    }

    // - START SCREEN -
    private fun observeAndStart() {
        viewModel.currentUser.observe(this) { user ->
            if (user == null || !user.remember) {
                startActivity(Intent(this.applicationContext, AuthActivity::class.java))
                finish()
            } else {
                val appIntent = Intent(this.applicationContext, ContactsActivity::class.java).apply {
                    putExtra("email", user.email)
                }
                startActivity(appIntent)
                finish()
            }
        }
    }
}