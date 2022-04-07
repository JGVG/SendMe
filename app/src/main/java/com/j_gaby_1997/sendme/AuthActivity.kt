package com.j_gaby_1997.sendme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.ActivityAuthBinding
import com.j_gaby_1997.sendme.fragments.contacts.ContactsViewModel
import com.j_gaby_1997.sendme.fragments.contacts.ContactsViewModelFactory
import com.j_gaby_1997.sendme.fragments.log_in.LogInFrag
import com.j_gaby_1997.sendme.fragments.log_in.LogInViewModel
import com.j_gaby_1997.sendme.fragments.log_in.LogInViewModelFactory

class AuthActivity : AppCompatActivity() {

    private val b: ActivityAuthBinding by lazy { ActivityAuthBinding.inflate(layoutInflater) }
    private val viewModel: LogInViewModel by viewModels {
        LogInViewModelFactory(LocalRepository(CurrentUserDatabase.getInstance(this).currentUserDao), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            observeAndStart()
        }
        setContentView(b.root)
    }

    // - START AUTH SCREEN -
    private fun observeAndStart() {
        viewModel.currentUser.observe(this) { user ->
            if(user != null){
                val appIntent = Intent(this.applicationContext, ContactsActivity::class.java).apply{
                    putExtra("email", user.email)
                }
                startActivity(appIntent)
                finish()
            }else{
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fcContent, LogInFrag::class.java, null)
                }
            }
        }

    }



}