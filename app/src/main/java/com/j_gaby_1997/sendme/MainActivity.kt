package com.j_gaby_1997.sendme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.fragments.sign_up.Sign_Up_Frag
import com.j_gaby_1997.sendme.databinding.ActivityMainBinding
import com.j_gaby_1997.sendme.fragments.log_in.Log_In_Frag

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

    private fun showStartDestination() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcContent, Log_In_Frag::class.java, null)
        }
    }
}