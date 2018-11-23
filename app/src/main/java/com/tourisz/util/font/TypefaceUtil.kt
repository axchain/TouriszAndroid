package com.tourisz.util.font


import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.Log

import java.lang.reflect.Field
import java.util.HashMap

/**
 * @author nikhil
 * Class to override the default application font to the custom font
 */

object TypefaceUtil {

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT
     * TYPEFACE WHICH WILL BE OVERRIDDEN
     *
     * @param context                    to work with assets
     */
    fun overrideFonts(context: Context) {
        val lightFontTypeface = Typeface.createFromAsset(context.assets, "fonts/roboto_light.ttf")
        val regularFontTypeface = Typeface.createFromAsset(context.assets, "fonts/roboto_regular.ttf")
        val mediumFontTypeface = Typeface.createFromAsset(context.assets, "fonts/roboto_medium.ttf")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val newMap = HashMap<String, Typeface>()
            newMap["sans-serif"] = regularFontTypeface
            newMap["sans-serif-light"] = regularFontTypeface
            newMap["sans-serif-medium"] = mediumFontTypeface
            try {
                val staticField = Typeface::class.java
                        .getDeclaredField("sSystemFontMap")
                staticField.isAccessible = true
                staticField.set(null, newMap)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        } else {
            try {
                val defaultFontTypefaceField = Typeface::class.java.getDeclaredField("sans")
                defaultFontTypefaceField.isAccessible = true
                defaultFontTypefaceField.set(null, regularFontTypeface)

                val lightFontTypefaceField = Typeface::class.java.getDeclaredField("sans-serif-light")
                lightFontTypefaceField.isAccessible = true
                lightFontTypefaceField.set(null, lightFontTypeface)

                val mediumFontTypefaceField = Typeface::class.java.getDeclaredField("sans-serif-medium")
                mediumFontTypefaceField.isAccessible = true
                mediumFontTypefaceField.set(null, mediumFontTypeface)
            } catch (e: Exception) {
                Log.e(TypefaceUtil::class.java.simpleName, "Can not set custom fonts " + regularFontTypeface.toString() + " and " +
                        lightFontTypeface.toString())
            }

        }
    }
}