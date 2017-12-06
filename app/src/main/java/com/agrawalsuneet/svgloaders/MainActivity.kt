package com.agrawalsuneet.svgloaders

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.agrawalsuneet.svgloaderspack.loaders.EcgPulseLoader
import com.agrawalsuneet.svgloaderspack.loaders.SVGLoader

class MainActivity : AppCompatActivity() {

    lateinit var container: LinearLayout

    lateinit var svgView: SVGLoader

    lateinit var playPauseBtn: Button
    lateinit var nextSVGBtn: Button

    lateinit var pulseLoader: EcgPulseLoader

    var currentLogoPos = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        container = findViewById(R.id.container)

        //addSVGViewProgrammatically()
        //handleSVGLoader()

        pulseLoader = findViewById(R.id.pulse_loader)
        pulseLoader.startAnimation()
    }

    private fun handleSVGLoader() {
        playPauseBtn = findViewById(R.id.playpauseBtn)
        nextSVGBtn = findViewById(R.id.nextSvgBtn)

        playPauseBtn.setOnClickListener {
            if (svgView.isAnimRunning()) {
                playPauseBtn.text = "Play"
                playPauseBtn.isEnabled = false
                nextSVGBtn.isEnabled = false
                svgView.endAnimation()
            } else {
                svgView.startAnimation()
                playPauseBtn.text = "Stop"
                nextSVGBtn.isEnabled = false
            }
        }

        //return

        svgView = findViewById(R.id.svg_loader)
        svgView.listener = (object : SVGLoader.AnimationListener {
            override fun onAnimationEnd() {
                Toast.makeText(baseContext, "Animation end", Toast.LENGTH_SHORT).show()
                playPauseBtn.isEnabled = true
                nextSVGBtn.isEnabled = true
            }

        })

        svgView.startAnimation()
        playPauseBtn.text = "Stop"
        nextSVGBtn.isEnabled = false

        nextSVGBtn.setOnClickListener {
            when (currentLogoPos) {
                1 -> {
                    svgView.shapesStringArray = resources.getStringArray(R.array.twitter_logo_path)
                    svgView.fillColorsArray = resources.getIntArray(R.array.twitter_logo_colors)
                    svgView.validateTraceColors()
                    svgView.setViewportSize(2000.0f, 1625.36f)
                    currentLogoPos = 3
                }

                2 -> {
                    svgView.shapesStringArray = resources.getStringArray(R.array.github_logo_path)
                    svgView.fillColorsArray = resources.getIntArray(R.array.github_logo_colors)
                    svgView.validateTraceColors()
                    svgView.setViewportSize(512.0f, 512.0f)
                    currentLogoPos = 3
                }

                3 -> {
                    svgView.shapesStringArray = resources.getStringArray(R.array.shotang_logo_path)
                    svgView.fillColorsArray = resources.getIntArray(R.array.shotang_logo_colors)
                    svgView.validateTraceColors()
                    svgView.setViewportSize(500.0f, 500.0f)
                    currentLogoPos = 4
                }

                4 -> {
                    svgView.shapesStringArray = resources.getStringArray(R.array.google_logo_path)
                    svgView.fillColorsArray = resources.getIntArray(R.array.google_logo_colors)
                    svgView.validateTraceColors()
                    svgView.setViewportSize(400.0f, 400.0f)
                    currentLogoPos = 1
                }
            }
        }
    }

    private fun addSVGViewProgrammatically() {
        svgView = SVGLoader(baseContext, resources.getStringArray(R.array.shotang_logo_path),
                resources.getIntArray(R.array.shotang_logo_colors), 512f, 512f)
                .apply {
                    layoutParams = ViewGroup.LayoutParams(800, 800)
                    markerLength = 50
                    fillTime = 1000
                    timePerShape = 2000
                    interpolator = DecelerateInterpolator()
                    validateTraceColors()
                    /*traceResidueColorsArray = resources.getIntArray(R.array.google_logo_colors)
                    traceColorsArray = resources.getIntArray(R.array.google_logo_colors)*/
                }

        svgView.listener = (object : SVGLoader.AnimationListener {
            override fun onAnimationEnd() {
                Toast.makeText(baseContext, "Animation end", Toast.LENGTH_SHORT).show()
                playPauseBtn.isEnabled = true
                nextSVGBtn.isEnabled = true
            }

        })
        container.addView(svgView)
        svgView.startAnimation()
    }
}
