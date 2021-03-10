package com.example.projetandroid

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawer : DrawerLayout
    private lateinit var toogle : ActionBarDrawerToggle
    private lateinit var navigationView : NavigationView
    private var indice: Int = -1
    private lateinit var fm: FragmentManager
    private lateinit var fragmentPicture: Fragment
    private lateinit var fragmentAdd: Fragment
    private lateinit var fragmentDownload: Fragment
    private lateinit var fragmentShowValue1: Fragment
    private lateinit var fragmentShowValue0: Fragment
    private lateinit var fragmentSearch: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        drawer = findViewById(R.id.drawer_layout)
        toogle = ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)
        toogle.syncState()
        fm = supportFragmentManager

        val value = intent.getIntExtra("show",-1)
        if (savedInstanceState != null)
        {
              indice = savedInstanceState.getInt("indice")
            if(indice == -1)
            {
                fragmentPicture = supportFragmentManager.findFragmentByTag("picture") as Picture
            }
            if (indice == 0)
            {
                fragmentAdd = supportFragmentManager.findFragmentByTag("add") as AddFlux

            }
            if (indice == 1)
            {
                fragmentDownload = supportFragmentManager.findFragmentByTag("download") as DownloadFlux
            }
            if (indice == 2)
            {
                fragmentShowValue1 = supportFragmentManager.findFragmentByTag("value1") as Show
            }
            if (indice == 3)
            {
                fragmentShowValue0 = supportFragmentManager.findFragmentByTag("value0") as Show
            }
            if (indice == 4)
            {
                fragmentSearch = supportFragmentManager.findFragmentByTag("search") as Search

            }
        }
        if (savedInstanceState == null)
        {
             fragmentPicture = Picture()
            fragmentPicture.arguments = intent.extras
            val supportFrag = supportFragmentManager
            val transition = supportFrag.beginTransaction()
            transition.replace(R.id.fragment_container,fragmentPicture, "picture")
                .commit()

        }
        if (value == 0)
        {
            val fragment = Show()
            var bundle = Bundle()
            bundle.putInt("value",0)
            fragment.arguments = bundle
            lance(fragment,"value0")

        }

    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.home->
            {
                indice = -1
                fragmentPicture = supportFragmentManager.findFragmentByTag("picture") as Picture
                lance(fragmentPicture,"picture")
            }
            R.id.RSS ->{
                indice = 0
                fragmentAdd = AddFlux()
                lance(fragmentAdd,"add")
            }
            R.id.telecharger ->
            {
                  indice = 1
                fragmentDownload = DownloadFlux()
                lance(fragmentDownload,"download")

            }
            R.id.tout ->
            {
                indice = 2
               var bundle = Bundle()
                bundle.putInt("value",1)
                fragmentShowValue1 = Show()
                fragmentShowValue1.arguments = bundle
                lance(fragmentShowValue1,"value1")
            }
            R.id.nouveau->
            {
                indice = 3
                var bundle = Bundle()
                bundle.putInt("value",0)
                fragmentShowValue0 = Show()
                fragmentShowValue0.arguments = bundle
                lance(fragmentShowValue0,"value0")
            }
            R.id.partext ->
            {
                indice = 4
                fragmentSearch = Search()
                lance(fragmentSearch,"search")
            }

        }
        return true
    }
    fun lance(fragment : Fragment, tag: String)
    {
        val fragmenttransition = supportFragmentManager.beginTransaction()
        fragmenttransition.replace(R.id.fragment_container,fragment, tag)
        .addToBackStack(null)
                .commit()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("indice", indice)

    }
}