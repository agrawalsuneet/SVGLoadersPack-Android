package com.agrawalsuneet.svgloaders

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.agrawalsuneet.svgloaderspack.loaders.SVGLoader

class MainActivity : AppCompatActivity() {

    lateinit var svgView: SVGLoader

    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        svgView = findViewById(R.id.animated_svg_view)
        svgView.listener = (object : SVGLoader.AnimationListener {
            override fun onAnimationEnd() {
                Toast.makeText(baseContext, "Animation end", Toast.LENGTH_SHORT).show()
            }

        })
        svgView.startAnimation()

        button = findViewById(R.id.button)

        button.setOnClickListener {
            svgView.endAnimation()
        }
    }
}
