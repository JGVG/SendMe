package com.j_gaby_1997.sendme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.databinding.ActivityAuthBinding
import com.j_gaby_1997.sendme.fragments.log_in.LogInFrag

class AuthActivity : AppCompatActivity() {

    private val b: ActivityAuthBinding by lazy { ActivityAuthBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            showStartDestination()
        }
        setContentView(b.root)
    }

    // - START AUTH SCREEN -
    private fun showStartDestination() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcContent, LogInFrag::class.java, null)
        }
    }
}