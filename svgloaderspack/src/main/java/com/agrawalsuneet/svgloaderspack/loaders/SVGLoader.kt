package com.agrawalsuneet.svgloaderspack.loaders

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.agrawalsuneet.svgloader.basicviews.LoaderContract
import com.agrawalsuneet.svgloaderspack.R

/**
 * Created by suneet on 11/17/17.
 */

class SVGLoader : View, LoaderContract {

    var listener: AnimationListener? = null

    var interpolator: Interpolator = LinearInterpolator()

    private lateinit var fillPaint: Paint

    val STATE_DOTTED_LINE: Int = 0x01
    val STATE_CONTINOUS_LINE: Int = 0x02
    val STATE_FILLED_STATE: Int = 0x03


    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
    }

    override fun initAttributes(attrs: AttributeSet) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SVGLoader, 0, 0)

        typedArray.recycle()
    }

    fun initPaints() {
        fillPaint = Paint()
        fillPaint.isAntiAlias = true
        fillPaint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = View.MeasureSpec.getSize(widthMeasureSpec)
        var height = View.MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        super.onMeasure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
    }


    internal class ShapeData {
        lateinit var path: Path
        lateinit var paint: Paint
        var length: Float = 0f

    }

    interface AnimationListener {
        fun onAnimationEnd()
    }


}