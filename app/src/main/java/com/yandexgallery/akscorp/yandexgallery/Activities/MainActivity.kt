package com.yandexgallery.akscorp.yandexgallery.Activities

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.yandexgallery.akscorp.yandexgallery.Fragments.PreviewListFragment
import com.yandexgallery.akscorp.yandexgallery.PERMISSIONS_REQUEST
import com.yandexgallery.akscorp.yandexgallery.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert

/**
 * Created by AksCorp on 29.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        checkPermission()
        
        var recreateFragment = true
        if(savedInstanceState!=null)
            recreateFragment = savedInstanceState.getBoolean("recreateFragment")
        initStartUI(recreateFragment)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.popup_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item!!.itemId)
        {
            R.id.about ->
            {
                val dialog = alert(getString(R.string.about_info))
                dialog.positiveButton(getString(R.string.ok), { it.cancel() })
                dialog.show()
            }
        }
        return true
    }
    
    /**
     * @param recreateFragment - Used to prevent the fragment from being recreated
     */
    override fun onSaveInstanceState(outState: Bundle?)
    {
        super.onSaveInstanceState(outState)
        outState!!.putBoolean("recreateFragment", false)
    }
    
    /**
     * Initialization main UI component
     */
    private fun initStartUI(recreateFragment:Boolean)
    {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        if(recreateFragment)
            initImagesGrid()
    }
    
    private fun initImagesGrid()
    {
        var fragmentManager = supportFragmentManager
        val fragment = PreviewListFragment()
        fragmentManager.beginTransaction().replace(R.id.main_activity_coordinator_layout, fragment)
            .commit()
    }
    
    /**
     * Displays the permissions dialog box. Show once
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET),
                PERMISSIONS_REQUEST)
        }
    }
    
    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String>, grantResults: IntArray)
    {
        when (requestCode)
        {
            PERMISSIONS_REQUEST ->
            {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                
                } else
                {
                    Toast.makeText(this, getString(R.string.permission_denied), Toast
                        .LENGTH_LONG).show()
                }
            }
        }
    }
}
