package com.agrawalsuneet.svgloaderspack.loaders

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.agrawalsuneet.svgloaderspack.R
import com.agrawalsuneet.svgloaderspack.basicviews.LoaderContract
import com.agrawalsuneet.svgloaderspack.data.ShapeData
import com.agrawalsuneet.svgloaderspack.helper.PathParser

/**
 * Created by suneet on 12/6/17.
 */
class EcgPulseLoader : View, LoaderContract {

    private val TAG = "SVGLoader"

    var markerLength: Int = 50

    private var shapeString: String = resources.getString(R.string.ecgpulseloader_path)

    var traceColor: Int = resources.getColor(android.R.color.black)
    var traceResidueColor: Int = resources.getColor(android.R.color.darker_gray)

    private lateinit var shapeData: ShapeData

    private val viewportWidth: Float = 5208f
    private val viewportHeight: Float = 5208f

    var timePerPulse: Int = 2000

    var interpolator: Interpolator = AccelerateDecelerateInterpolator()

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var animStartTime: Long = 0

    private enum class ECGPusleLoaderState {
        StateLoading, StateFill, StateEnd
    }

    private var state: ECGPusleLoaderState = ECGPusleLoaderState.StateEnd


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initAttributes(attrs)
    }

    override fun initAttributes(attrs: AttributeSet) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SVGLoader)

        markerLength = typedArray.getDimensionPixelSize(R.styleable.SVGLoader_svgloader_markerLength, 50)


        typedArray.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        rebuildShapeData()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = View.MeasureSpec.getSize(widthMeasureSpec)
        var height = View.MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (height <= 0 && width <= 0 && heightMode == View.MeasureSpec.UNSPECIFIED &&
                widthMode == View.MeasureSpec.UNSPECIFIED) {
            width = 0
            height = 0
        } else if (height <= 0 && heightMode == View.MeasureSpec.UNSPECIFIED) {
            height = (width * viewportHeight / viewportWidth).toInt()
        } else if (width <= 0 && widthMode == View.MeasureSpec.UNSPECIFIED) {
            width = (height * viewportWidth / viewportHeight).toInt()
        } else if (width * viewportHeight > viewportWidth * height) {
            width = (height * viewportWidth / viewportHeight).toInt()
        } else {
            height = (width * viewportHeight / viewportWidth).toInt()
        }

        super.onMeasure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))

        rebuildShapeData()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (ECGPusleLoaderState.StateEnd == state) return

        val timeDiff = (System.currentTimeMillis() - animStartTime).toFloat()

        //draw the path
        val currentPhase = constrain(0f, 1f, timeDiff / timePerPulse)

        val distanceToDraw = interpolator.getInterpolation(currentPhase) * shapeData.length

        shapeData.paint.color = traceResidueColor
        shapeData.paint.pathEffect = DashPathEffect(
                floatArrayOf(distanceToDraw, shapeData.length), 0f)
        canvas.drawPath(shapeData.path, shapeData.paint)

        // draw the marker
        shapeData.paint.color = traceColor

        shapeData.paint.pathEffect = DashPathEffect(
                floatArrayOf(0f, distanceToDraw,
                        if (currentPhase > 0) markerLength.toFloat() else 0f,
                        shapeData.length), 0f)
        canvas.drawPath(shapeData.path, shapeData.paint)

        if (timeDiff > timePerPulse) {
            animStartTime = System.currentTimeMillis()
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun constrain(min: Float, max: Float, v: Float): Float {
        return Math.max(min, Math.min(max, v))
    }

    private fun rebuildShapeData() {

        val sx = mWidth / viewportWidth
        val sy = mHeight / viewportHeight

        val scaleMatrix = Matrix()
        val outerRect = RectF(sx, sx, sy, sy)
        scaleMatrix.setScale(sx, sy, outerRect.centerX(), outerRect.centerY())

        shapeData = ShapeData()

        try {
            shapeData.path = PathParser.createPathFromPathData(shapeString)
            shapeData.path.transform(scaleMatrix)
        } catch (ex: Exception) {
            shapeData.path = Path()
            Log.e(TAG, "Couldn't parse path", ex)
        }

        val pathMeasure = PathMeasure(shapeData.path, false)
        while (true) {
            shapeData.length = Math.max(shapeData.length, pathMeasure.length)
            if (!pathMeasure.nextContour()) {
                break
            }
        }
        shapeData.paint = Paint()
        shapeData.paint.style = Paint.Style.STROKE
        shapeData.paint.isAntiAlias = true
        shapeData.paint.color = Color.WHITE
        shapeData.paint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)

    }


    fun startAnimation() {
        animStartTime = System.currentTimeMillis()
        state = ECGPusleLoaderState.StateLoading
        ViewCompat.postInvalidateOnAnimation(this)
    }
}