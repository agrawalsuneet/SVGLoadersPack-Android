package com.agrawalsuneet.svgloaders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.agrawalsuneet.svgloaderspack.loaders.SVGLoader;

/**
 * Created by suneet on 12/4/17.
 */

public class MainActivityJava extends AppCompatActivity {

    ConstraintLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SVGLoader svgLoader = new SVGLoader(getBaseContext(),
                getResources().getStringArray(R.array.google_logo_path),
                getResources().getIntArray(R.array.google_logo_colors),
                400f, 400f);

        svgLoader.setLayoutParams(new ViewGroup.LayoutParams(800,800));
        svgLoader.setMarkerLength(50);
        svgLoader.setFillTime(1000);
        svgLoader.setTimePerShape(3000);
        svgLoader.setInterpolator(new DecelerateInterpolator());
        //svgLoader.validateTraceColors();
        svgLoader.setTraceColorsArray(getResources().getIntArray(R.array.google_logo_colors));
        svgLoader.setTraceResidueColorsArray(getResources().getIntArray(R.array.google_logo_colors));
        svgLoader.setListener(new SVGLoader.AnimationListener() {
            @Override
            public void onAnimationEnd() {
                Toast.makeText(getBaseContext(), "Animation end", Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(svgLoader);
        svgLoader.startAnimation();
    }
}
