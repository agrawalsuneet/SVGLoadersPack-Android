package com.agrawalsuneet.svgloaderspack.utils

import android.graphics.Color

/**
 * Created by suneet on 12/3/17.
 */
object Helper {

    fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }
}
