package com.yandexgallery.akscorp.yandexgallery.Utilities

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by AksCorp on 30.03.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */

class Utilities() {
    companion object {
        fun isInternetConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null)
                return activeNetwork.isConnected; // WIFI connected
            else
                return false; // no info object implies no connectivity
        }
        
    }
}