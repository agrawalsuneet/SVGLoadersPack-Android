package com.agrawalsuneet.svgloaderspack.loaders

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import com.agrawalsuneet.svgloader.basicviews.LoaderContract
import com.agrawalsuneet.svgloaderspack.R
import com.agrawalsuneet.svgloaderspack.data.ShapeData
import com.agrawalsuneet.svgloaderspack.helper.PathParser

/**
 * Created by suneet on 12/2/17.
 */
class SVGLoader : View, LoaderContract {


    private val TAG = "SVGLoader"

    var markerLength: Int = 50

    lateinit var shapesStringArray: Array<String>

    lateinit var traceColorsArray: IntArray
    lateinit var traceResidueColorsArray: IntArray
    lateinit var fillColorsArray: IntArray

    private lateinit var shapeDataArray: Array<ShapeData?>

    var viewportWidth: Float = 512f
    var viewportHeight: Float = 512f

    var fillTime: Int = 1000
    var timePerShape: Int = 2000

    var interpolator: Interpolator = DecelerateInterpolator()

    private var aspectRatioWidth = 1f
    private var aspectRatioHeight = 1f

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var animStartTime: Long = 0


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

        viewportWidth = typedArray.getFloat(R.styleable.SVGLoader_svgloader_viewportWidth, 500f)
        viewportHeight = typedArray.getFloat(R.styleable.SVGLoader_svgloader_viewportHeight, 500f)

        fillTime = typedArray.getInteger(R.styleable.SVGLoader_svgloader_fillTime, 1000)
        timePerShape = typedArray.getInteger(R.styleable.SVGLoader_svgloader_timePerShape, 2000)

        interpolator = AnimationUtils.loadInterpolator(context,
                typedArray.getResourceId(R.styleable.SVGLoader_svgloader_interpolator,
                        android.R.anim.decelerate_interpolator))

        aspectRatioWidth = viewportWidth
        aspectRatioHeight = viewportHeight

        val shapesStringArrayId = typedArray.getResourceId(R.styleable.SVGLoader_svgloader_shapesStringArray, 0)
        val traceColorArrayId = typedArray.getResourceId(R.styleable.SVGLoader_svgloader_traceColorsArray, 0)
        val traceResidueColorsArrayId = typedArray.getResourceId(R.styleable.SVGLoader_svgloader_residueColorsArray, 0)
        val fillColorsArrayId = typedArray.getResourceId(R.styleable.SVGLoader_svgloader_fillColorsArray, 0)

        typedArray.recycle()

        if (shapesStringArrayId == 0) {
            throw RuntimeException("You need to set the shapes string array first.")
        }

        shapesStringArray = resources.getStringArray(shapesStringArrayId)

        fillColorsArray = validateColorsArray(fillColorsArrayId, Color.WHITE)
        traceColorsArray = validateColorsArray(traceColorArrayId, Color.BLACK)
        traceResidueColorsArray = validateColorsArray(traceResidueColorsArrayId, Color.GRAY)
    }

    private fun validateColorsArray(arrayId: Int, color: Int): IntArray {
        val colors = IntArray(shapesStringArray.size)

        if (arrayId != 0) {
            val colorsArray = resources.getIntArray(arrayId)
            for (i in 0 until shapesStringArray.size) {
                colors[i] = if (colorsArray.size >= i) colorsArray[i] else color
            }
        } else {
            for (i in 0 until shapesStringArray.size) {
                colors[i] = color
            }
        }
        return colors
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
            height = (width * aspectRatioHeight / aspectRatioWidth).toInt()
        } else if (width <= 0 && widthMode == View.MeasureSpec.UNSPECIFIED) {
            width = (height * aspectRatioWidth / aspectRatioHeight).toInt()
        } else if (width * aspectRatioHeight > aspectRatioWidth * height) {
            width = (height * aspectRatioWidth / aspectRatioHeight).toInt()
        } else {
            height = (width * aspectRatioHeight / aspectRatioWidth).toInt()
        }

        super.onMeasure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val timeDiff = (System.currentTimeMillis() - animStartTime).toFloat()

        for (i in shapeDataArray.indices) {

            //draw the path
            val currentPhase = constrain(0f, 1f, timeDiff / timePerShape)

            val distanceToDraw = interpolator.getInterpolation(currentPhase) * shapeDataArray[i]!!.length

            shapeDataArray[i]!!.paint.color = traceResidueColorsArray[i]
            shapeDataArray[i]!!.paint.pathEffect = DashPathEffect(
                    floatArrayOf(distanceToDraw, shapeDataArray[i]!!.length), 0f)
            canvas.drawPath(shapeDataArray[i]!!.path, shapeDataArray[i]!!.paint)

            // draw the marker
            shapeDataArray[i]!!.paint.color = traceColorsArray[i]

            shapeDataArray[i]!!.paint.pathEffect = DashPathEffect(
                    floatArrayOf(0f, distanceToDraw,
                            if (currentPhase > 0) markerLength.toFloat() else 0f,
                            shapeDataArray[i]!!.length), 0f)
            canvas.drawPath(shapeDataArray[i]!!.path, shapeDataArray[i]!!.paint)
        }

        if (state == SVGLoaderState.StateFill && timeDiff > timePerShape) {

            //end animation called
            //start filling the shapes
            val timeAfterShapesDrawn = timeDiff - timePerShape

            val fillPaint = Paint()
            fillPaint.isAntiAlias = true
            fillPaint.style = Paint.Style.FILL

            //fill the colors
            val currentPhase = constrain(0f, 1f, timeAfterShapesDrawn / fillTime)
            for (i in shapeDataArray.indices) {
                val fillColor = fillColorsArray[i]
                val alpha = (currentPhase * Color.alpha(fillColor)).toInt()
                val red = Color.red(fillColor)
                val green = Color.green(fillColor)
                val blue = Color.blue(fillColor)
                fillPaint.setARGB(alpha, red, green, blue)
                canvas.drawPath(shapeDataArray[i]!!.path, fillPaint)
            }

            if (timeDiff > timePerShape + fillTime) {
                listener?.onAnimationEnd()
                return
            }

        } else if (timeDiff > timePerShape) {
            animStartTime = System.currentTimeMillis()
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun constrain(min: Float, max: Float, v: Float): Float {
        return Math.max(min, Math.min(max, v))
    }

    fun rebuildShapeData() {

        val X = mWidth / viewportWidth
        val Y = mHeight / viewportHeight

        val scaleMatrix = Matrix()
        val outerRect = RectF(X, X, Y, Y)
        scaleMatrix.setScale(X, Y, outerRect.centerX(), outerRect.centerY())

        shapeDataArray = arrayOfNulls(shapesStringArray.size)
        for (i in shapesStringArray.indices) {
            shapeDataArray[i] = ShapeData()
            try {
                shapeDataArray[i]!!.path = PathParser.createPathFromPathData(shapesStringArray[i])
                shapeDataArray[i]!!.path.transform(scaleMatrix)
            } catch (ex: Exception) {
                shapeDataArray[i]!!.path = Path()
                Log.e(TAG, "Couldn't parse path", ex)
            }

            val pathMeasure = PathMeasure(shapeDataArray[i]!!.path, true)
            while (true) {
                shapeDataArray[i]!!.length = Math.max(shapeDataArray[i]!!.length, pathMeasure.length)
                if (!pathMeasure.nextContour()) {
                    break
                }
            }
            shapeDataArray[i]!!.paint = Paint()
            shapeDataArray[i]!!.paint.style = Paint.Style.STROKE
            shapeDataArray[i]!!.paint.isAntiAlias = true
            shapeDataArray[i]!!.paint.color = Color.WHITE
            shapeDataArray[i]!!.paint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
        }
    }

    fun setViewportSize(viewportWidth: Float, viewportHeight: Float) {
        this.viewportWidth = viewportWidth
        this.viewportHeight = viewportHeight
        aspectRatioWidth = viewportWidth
        aspectRatioHeight = viewportHeight
        requestLayout()
    }


    fun startAnimation() {
        animStartTime = System.currentTimeMillis()
        state = SVGLoaderState.StateLoading
        ViewCompat.postInvalidateOnAnimation(this)
    }

    fun endAnimation() {
        state = SVGLoaderState.StateFill
    }

    enum class SVGLoaderState {
        StateLoading, StateFill
    }

    private var state: SVGLoaderState = SVGLoaderState.StateLoading

    var listener: SVGLoader.AnimationListener? = null

    interface AnimationListener {
        fun onAnimationEnd()
    }

}