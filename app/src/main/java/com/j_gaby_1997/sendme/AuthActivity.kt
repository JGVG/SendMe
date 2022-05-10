package com.j_gaby_1997.sendme

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.ActivityAuthBinding
import com.j_gaby_1997.sendme.fragments.log_in.LogInFrag
import com.j_gaby_1997.sendme.fragments.log_in.LogInViewModel
import com.j_gaby_1997.sendme.fragments.log_in.LogInViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class AuthActivity : AppCompatActivity() {

    private val b: ActivityAuthBinding by lazy { ActivityAuthBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        startScreen()
    }

    // - START SCREEN -
    private fun startScreen() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcContent, LogInFrag::class.java, null)
        }
    }

}