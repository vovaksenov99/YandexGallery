package com.yandexgallery.akscorp.yandexgallery.Utilities

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Aksenov Vladimir
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */

@GlideModule
class GlideModule : AppGlideModule()
{
    override fun applyOptions(context: Context, builder: GlideBuilder)
    {
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
    }
    
    override fun registerComponents(context: Context, glide: Glide, registry: Registry)
    {
    }
}
