package com.yandexgallery.akscorp.yandexgallery.Activities

import android.R.attr.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.yandexgallery.akscorp.yandexgallery.Essentials.Image
import com.yandexgallery.akscorp.yandexgallery.R
import com.yandexgallery.akscorp.yandexgallery.Utilities.GlideApp
import kotlinx.android.synthetic.main.detail_activity.*
import android.support.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import org.jetbrains.anko.alert

/**
 * Created by AksCorp on 29.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */
class DetailsActivity : AppCompatActivity()
{
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.detail_activity)
        
        val image = intent.getSerializableExtra("image") as Image
        
        GlideApp.with(this)
            .load(image.url)
            
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.color.darkGrey)
            .error(R.drawable.placeholder_image_error)
            .fitCenter()
            .into(object : DrawableImageViewTarget(preview_iv)
            {
                override fun onLoadFailed(errorDrawable: Drawable?)
                {
                    super.onLoadFailed(errorDrawable)
                    progressBar.visibility = View.INVISIBLE
                }
                
                override fun onResourceReady(resource: Drawable,
                    transition: Transition<in Drawable>?)
                {
                    super.onResourceReady(resource, transition)
                    progressBar.visibility = View.INVISIBLE
                }
            })
        
        
        info.setOnClickListener {
            val dialog = alert(image.getInfo())
            dialog.positiveButton(getString(R.string.ok), { it.cancel() })
            dialog.show()
        }
    }
}

