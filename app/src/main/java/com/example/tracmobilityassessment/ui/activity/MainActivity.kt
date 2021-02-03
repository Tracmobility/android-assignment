package com.example.tracmobilityassessment.ui.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tracmobilityassessment.R
import com.example.tracmobilityassessment.logic.managers.UserManager
import com.example.tracmobilityassessment.ui.fragment.login.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        drawerToggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.app_name,
            R.string.app_name
        )
        drawer_layout.addDrawerListener(drawerToggle)
        openFragment(LoginFragment())
    }

    fun openFragment(fragment: Fragment) {

        drawerToggle.isDrawerIndicatorEnabled = fragment !is LoginFragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_activity_frame, fragment, "")
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UserManager.logout(applicationContext)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}