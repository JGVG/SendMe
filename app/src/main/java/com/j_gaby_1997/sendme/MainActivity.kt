package com.j_gaby_1997.sendme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.databinding.ActivityMainBinding
import com.j_gaby_1997.sendme.fragments.log_in.LogInFrag

class MainActivity : AppCompatActivity() {

    private val b: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (savedInstanceState == null) {
            showStartDestination()
        }
        setContentView(b.root)
    }

    // - START SCREEN -
    private fun showStartDestination() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcContent, LogInFrag::class.java, null)
        }
    }
}