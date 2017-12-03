package com.agrawalsuneet.svgloaderspack.data

import android.graphics.Paint
import android.graphics.Path

/**
 * Created by suneet on 11/28/17.
 */
class ShapeData {
    lateinit var path: Path
    lateinit var paint: Paint
    var length: Float = 0f
    var isClosedLoop: Boolean = false
}