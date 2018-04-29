package com.yandexgallery.akscorp.yandexgallery.Utilities

import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DialogTitle
import android.widget.Toast
import org.jetbrains.anko.runOnUiThread
import java.util.*
import javax.security.auth.callback.Callback

/**
 * Usage as notify snackbar
 *
 * Created by AksCorp on 29.04.2018.
 */

/**
 * @param parent context
 * @param layout layout to show snackbar
 */
open class MessageSnackbar(val context: Context, val layout: CoordinatorLayout)
{
    
    //current shackbar
    private var snackbar: Snackbar? = null
    
    //current showing state
    private var isShowing = false
    
    /**
     * Show snackbar with defined message
     */
    fun show(message: String, length: Int = Snackbar.LENGTH_INDEFINITE)
    {
        context.runOnUiThread {
            snackbar = Snackbar.make(layout, message, length)
            snackbar!!.show()
            isShowing = true
            snackbar!!.addCallback(object : Snackbar.Callback()
            {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int)
                {
                    super.onDismissed(transientBottomBar, event)
                    isShowing = false
                }
            }
            )
        }
    }
    
    fun isShow() = isShowing
    
    fun dismiss()
    {
        if (snackbar != null)
            snackbar!!.dismiss()
    }
}