package com.agrawalsuneet.svgloaders

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.agrawalsuneet.svgloaderspack.loaders.SVGLoader

class MainActivity : AppCompatActivity() {

    lateinit var svgView: SVGLoader

    lateinit var playPauseBtn: Button
    lateinit var nextSVGBtn: Button

    var currentLogoPos = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        svgView = findViewById(R.id.animated_svg_view)
        svgView.listener = (object : SVGLoader.AnimationListener {
            override fun onAnimationEnd() {
                Toast.makeText(baseContext, "Animation end", Toast.LENGTH_SHORT).show()
                playPauseBtn.isEnabled = true
                nextSVGBtn.isEnabled = true
            }

        })

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

        svgView.startAnimation()
        playPauseBtn.text = "Stop"
        nextSVGBtn.isEnabled = false

        nextSVGBtn.setOnClickListener {
            when (currentLogoPos) {
                1 -> {
                    svgView.shapesStringArray = resources.getStringArray(R.array.twitter_logo_path)
                    svgView.fillColorsArray = resources.getIntArray(R.array.twitter_logo_colors)
                    svgView.setViewportSize(2000.0f, 1625.36f)
                    currentLogoPos = 2
                }
            }
        }
    }
}
