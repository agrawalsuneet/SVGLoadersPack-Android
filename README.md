# SVGLoadersPack
> Android Loaders            [![BuddyBuild](https://dashboard.buddybuild.com/api/statusImage?appID=5a25b47372b6c800015b8455&branch=master&build=latest)](https://dashboard.buddybuild.com/apps/5a25b47372b6c800015b8455/build/latest?branch=master)

> A replacement of default android material progressbar with svg loaders


### SVGLoader


Other loaders: [LinearDotsLoader](https://github.com/agrawalsuneet/DotsLoader), [CircularDotsLoader](https://github.com/agrawalsuneet/DotsLoader), [LazyLoader](https://github.com/agrawalsuneet/DotsLoader), [TashieLoader](https://github.com/agrawalsuneet/DotsLoader), [FourFoldLoader](https://github.com/agrawalsuneet/FourFoldLoader), [ZipZapLoader](https://github.com/agrawalsuneet/FourFoldLoader), [ClockLoader](https://github.com/agrawalsuneet/LoadersPack), [RippleLoader](https://github.com/agrawalsuneet/LoadersPack)

## How To use
include below dependency in build.gradle of application and compile it
```
compile 'com.agrawalsuneet.androidlibs:svgloaderspack:0.1'
```

### SVGLoader
Please add the SVG paths as String arrays and also the colors as int array in res folder. In order to get the paths of any SVG, add that svg file as vector asset in android project and copy all the paths and colors from that vector asset.

```
<string-array name="google_logo_path">
        <item>M142.9,24.2c40.2-13.9,85.3-13.6,125.3,1.1c22.2,8.2,42.5,21,59.9,37.1c-5.8,6.3-12.1,12.2-18.1,18.3 c-11.4,11.4-22.8,22.8-34.2,34.2c-11.3-10.8-25.1-19-40.1-23.6c-17.6-5.3-36.6-6.1-54.6-2.2c-21,4.5-40.5,15.5-55.6,30.9 c-12.2,12.3-21.4,27.5-27,43.9c-20.3-15.8-40.6-31.5-61-47.3C59,73.6,97.6,39.7,142.9,24.2z</item>
        <item>M21.4,163.2c3.3-16.2,8.7-32,16.2-46.8c20.3,15.8,40.6,31.5,61,47.3c-8,23.3-8,49.2,0,72.4 c-20.3,15.8-40.6,31.6-60.9,47.3C18.9,246.7,13.2,203.6,21.4,163.2z</item>
        <item>M37.5,283.5c20.3-15.7,40.6-31.5,60.9-47.3c7.8,22.9,22.8,43.2,42.6,57.1c12.4,8.7,26.6,14.9,41.4,17.9 c14.6,3,29.7,2.6,44.4,0.1c14.6-2.6,28.7-7.9,41-16.2c19.7,15.3,39.4,30.6,59.1,45.9c-21.3,19.7-48,33.1-76.2,39.6 c-31.2,7.1-64.2,7.3-95.2-1c-24.6-6.5-47.7-18.2-67.6-34.1C67,328.9,49.6,307.5,37.5,283.5z</item>
        <item>M203.7,165.1c58.3,0,116.7,0,175,0c5.8,32.7,4.5,66.8-4.7,98.8c-8.5,29.3-24.6,56.5-47.1,77.2 c-19.7-15.3-39.4-30.6-59.1-45.9c19.5-13.1,33.3-34.3,37.2-57.5c-33.8,0-67.6,0-101.4,0C203.7,213.5,203.7,189.3,203.7,165.1z</item>
    </string-array>

    <color name="google_red">#EA4335</color>
    <color name="google_yellow">#FBBC05</color>
    <color name="google_green">#34A853</color>
    <color name="google_blue">#4285F4</color>

    <string-array name="google_logo_colors">
        <item>@color/google_red</item>
        <item>@color/google_yellow</item>
        <item>@color/google_green</item>
        <item>@color/google_blue</item>
    </string-array>
    
    <color name="google_residue_red">#77EA4335</color>
    <color name="google_residue_yellow">#77FBBC05</color>
    <color name="google_residue_green">#7734A853</color>
    <color name="google_residue_blue">#774285F4</color>

    <string-array name="google_logo_trace_residue_colors">
        <item>@color/google_residue_red</item>
        <item>@color/google_residue_yellow</item>
        <item>@color/google_residue_green</item>
        <item>@color/google_residue_blue</item>
    </string-array>
```
The important thing here to take care is the viewport width and height that you can get from the vector asset of logo/image. Make sure you pass the exact same float value of viewport width and height to the SVGLoader.

##### Through XML
```
<com.agrawalsuneet.svgloaderspack.loaders.SVGLoader
        android:id="@+id/svg_loader"
        android:layout_width="250dp"
        android:layout_height="250dp"
        app:svgloader_fillColorsArray="@array/google_logo_colors"
        app:svgloader_fillTime="2000"
        app:svgloader_interpolator="@android:anim/decelerate_interpolator"
        app:svgloader_markerLength="10dp"
        app:svgloader_shapesStringArray="@array/google_logo_path"
        app:svgloader_timePerShape="3000"
        app:svgloader_viewportHeight="400"
        app:svgloader_viewportWidth="400" />
        
        svgView = findViewById(R.id.svg_loader)
        svgView.listener = (object : SVGLoader.AnimationListener {
            override fun onAnimationEnd() {
                Toast.makeText(baseContext, "Animation end", Toast.LENGTH_SHORT).show()
            }

        })

        svgView.startAnimation()
        
        //to stop loading call endAnimation()
        svgView.endAnimation()
```
##### Through Code
* Kotlin
```
        svgView = SVGLoader(baseContext, resources.getStringArray(R.array.shotang_logo_path),
                resources.getIntArray(R.array.shotang_logo_colors), 512f, 512f)
                .apply {
                    layoutParams = ViewGroup.LayoutParams(800, 800)
                    markerLength = 50
                    fillTime = 1000
                    timePerShape = 2000
                    interpolator = DecelerateInterpolator()
                    validateTraceColors()
                    //traceResidueColorsArray = resources.getIntArray(R.array.google_logo_residue_colors)
                    //traceColorsArray = resources.getIntArray(R.array.google_logo_trace_colors)
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
        
        //to stop loading call endAnimation()
        svgView.endAnimation()
```

* Java
```
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
        
        //to stop loading call endAnimation()
        svgView.endAnimation();
```

Please take a 2 mins survey to make this library better [here](https://goo.gl/forms/xCPtiy3WdCOPlTUU2).
It won't take more than 2 mins I promise :) or feel free to drop an email at agrawalsuneet@gmail.com if face any issue or require any additional functionality in it.
```
Copyright 2017 Suneet Agrawal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
