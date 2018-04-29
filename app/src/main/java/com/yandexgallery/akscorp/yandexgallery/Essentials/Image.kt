package com.yandexgallery.akscorp.yandexgallery.Essentials

import java.io.Serializable

/**
 * Created by AksCorp on 29.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */

data class Image(var name: String? = null, var url: String? = null, var previewUrl: String? =
    null, var size: Long? = null, var type: String? = null, var created: String? = null) : Serializable
{
    fun getInfo(): String
    {
        return """
           Name:$name
           Created:$created
           Type:$type
           Size:$size
        """.trimIndent()
    }
}