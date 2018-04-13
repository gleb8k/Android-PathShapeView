# Android-PathShapeView
[![](https://jitpack.io/v/gleb8k/Android-PathShapeView.svg)](https://jitpack.io/#gleb8k/Android-PathShapeView)
[![](https://img.shields.io/badge/license-APACHE2-blue.svg)](https://github.com/gleb8k/Android-PathShapeView/blob/master/LICENSE)

This library allows to draw different shapes, lines, marks easily. It's customizable and provides posibility to fill your custom shapes by color, gradient or texture. Also you can fill just by stroke with color, gradient or texture. If you want to add some labels or marks on your shapes or lines it's not difficult with this toolbox.

## Table of Contents:
* **[Setup](#setup)**    
* **[Usage](#usage)**
* **[Samples](#samples)**
* **[License](#license)**
## Setup

Add it in your root `build.gradle` at the end of repositories:

```gradle
allprojects {
	repositories {
		// ... other repositories
		maven { url "https://jitpack.io" }
	}
}
```
Then add the dependencies that you need in your project.

```gradle
dependencies {
	compile 'com.github.gleb8k:Android-PathShapeView:1.3.2'
}
```
## Usage

The main class to show your graphic items is **PathShapeView** 
You can create it and attach to your root view:
```kotlin
	val pathShapeView = PathShapeView(context: Context)
	...
```
Or inflate from xml:
```xml
	<shape.path.view.PathShapeView
            android:id="@+id/path"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
	    ...
```
Use *PathShape* class to config graphic items 
```kotlin
	val pathShape = PathShape.create()
	...
	//or load from .json
	val pathShape = PathShape.fromAsset(context, fileName)
	...
	pathShapeView.setPath(pathShape)
	...
```
Or add *assetShapeResource* attribute to PathShapeView in xml
```xml
	...
	xmlns:app="http://schemas.android.com/apk/res-auto"
	...
	<shape.path.view.PathShapeView
            android:id="@+id/path"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
	    app:assetShapeResource="pathShape.json">
	    ...
```
Here is a [sample of file format in json](https://github.com/gleb8k/Android-PathShapeView/blob/master/app/src/main/assets/pathShape.json)

* **PathProvider** - is the main class which allow to create different graphic items. Each item can be added with the logical operation (**PathOperation**: ADD, SUB, SUB_REVERSE, JOIN, INTERSECT, XOR)  
Just to simple add item use **ADD** operation.
There are several items which you can create:
	- **putLines(list: List<PointF>, isClosed:Boolean, operation: PathOperation)** - add lines by list of points,
	      **isClosed** - allows to close the current contour.
	- **putArc(centerPoint: PointF, width:Float, height:Float, startAngle: Float, sweepAngle: Float, 	operation:		PathOperation)** - add arc to the path as a new contour
	- **putOval(centerPoint: PointF, width:Float, height:Float, operation: PathOperation)** - add a closed oval contour
	- **putCircle(centerPoint: PointF, radius:Float, operation: PathOperation)** - add a closed circle contour
	- **putPoly(centerPoint: PointF, radius:Float, angleRotation: Float, sidesCount:Int, operation: PathOperation)** -
		add equilateral polygon 
	- **putStar(centerPoint: PointF, outerRadius:Float, innerRadius: Float, angleRotation: Float, sidesCount:Int, 	    		   operation: PathOperation)** - add star shape 
	- **putRect(centerPoint: PointF, width:Float, height:Float, operation: PathOperation)** - add a closed rectangle 		contour
	- **putRoundRect(centerPoint: PointF, width:Float, height:Float, cornerRadius: Float, operation: PathOperation)** - 		   add a closed round-rectangle contour
	- **putText(centerPoint: PointF, width: Float, height: Float, text: String, textConfigurator: TextConfigurator, 	       operation: PathOperation)** - add text
	- **putCustomShape(customLinesBuilder: CustomLinesBuilder, operation: PathOperation)** - add **customLinesBuilder** 		   object
	
* **CustomLinesBuilder** - class which helps to create custom lines by points or by function
	- **addPoint(x: Float, y: Float)** - add new point
	- **addGraphPoints(minX: Float, maxX: Float, minY: Float, maxY: Float, function: GraphFunction)** - add points created 			by function and limited by bounds: minX, maxX, minY, maxY.
	- **setClosed(isClosed: Boolean)** - close the current contour
* **GraphFunction** - abstract class which provide function
	- **onFunctionGetValue(xValue: Float, stepValue: Float, maxStepCount: Int): Float** - return function value
	- **offset(dx: Float, dy: Float)** - offset current point by distance
	- **rotate(angle: Float)** - rotate current point by angle
	- **skew(kx: Float, ky: Float)** - skew current point by coefficients
* **WaveFunction(var waveWidth: Float, var waveHeight: Float, var waveType: WaveType)** - class allows to create wave function
	by waveType(SINE, SINE_ARC, SINE_ARC_REVERSE, SQUARE, TRIANGLE, SAWTOOTH)
* **BodyFillProvider** - class which allows to fill your graphic items. There are methods of **BodyFillProvider**:
	- **setColor(color: Int)** - set the fill color
	- **setGradient(gradient: GradientProvider)** - set the fill gradient. There are methods of **GradientProvider**:  
	     - **gradient.setType(type: Type)** - gradient can be(*LINEAR*, *RADIAL* or *SWEEP*)  
	     - **gradient.setAngle(angle: Float)** - set the angle of gradient direction  
	     - **gradient.setLength(length: Float)** - set the length of gradient, by default it fills fit view size  
	     - **gradient.setStartPoint(startPoint: PointF)** - set the start position of gradient  
	     - **gradient.addColor(color: Int)** - add new color to gradient  
	     - **gradient.addColor(color: Int, colorPosition: Float)** - add new color to gradient with color position, 		  colorPosition can be in [0..1]  
	- **setTexture(resId: Int)** - set the fill texture by resource id 
	- **setTexture(bitmap: Bitmap)** - set the fill texture by bitmap
	- **fitTextureToSize(width: Float, height: Float)** - fit texture to current size
	- **fitTextureToSize(width: Float, height: Float, convertWithPointConverter: Boolean)** - fit texture to current size
          with possibility to convert setted size
	- **setFillType(fillType: FillType)** - set fill type for gradient or texture (REPEAT, MIRROR, CLAMP)  
	- **setRoundedCorners(radius: Float)** - set all corners rounded with radius
	- **setGlowEffect(radius: Float, glowType: GlowType)** - set the glow effect with radius and type (NORMAL, SOLID, 	    OUTER, INNER)
	- **setEmbossEffect(angle: Float, embossType: EmbossType)** - set the emboss effect with angle of direction and type
	  (EMBOSS, EXTRUDE)
	- **setShadow(radius: Float, dx: Float, dy: Float, color: Int)** - draws a shadow, with the specified offset and 	   color, and blur radius.
* **ContourFillProvider** - class which allows to draw the contour of your graphic items. Has the same methods with the 	**BodyFillProvider** class and several specified methods:
	- **setWidth(width: Float)** - set the width of contour
	- **setIsDotRounded(isDotRounded: Boolean)** - if your contour is dashed it allows to round your dots
	- **addDotParams(dotLength: Float, dotDistance: Float)** - set your contour is dashed and add dot params. You can add 	    	params more than one time. Each new params will configure the next dot.
* list of **Mark** items - marks which show labels and icons on the graphic items. Each mark can be configured by the 	following methods:
	- **addPosition(point: PointF)** - add new position to current mark
	- **addPosition(point: PointF, label: String?)** - add new position with label to current mark
	- **addPositions(points: List<PointF>)** - add list of positions to current mark
	- **addPositions(points: List<PointF>, labels: List<String>)** - add list of positions and list of labels to current 		mark
	- **setDrawable(resId: Int)** - set image resource to current mark
	- **setDrawable(drawable: Drawable)** - set image drawable to current mark
	- **fitDrawableToSize(width: Float, height: Float)** - scale mark icon to current size
	- **setTextConfigurator(configurator: TextConfigurator)** - set text configuration to mark. It does effect when mark 		contains text labels.
* **TextConfigurator** - main class to configure text params. There are following methods:
	- **setStyle(vararg style: Style)** - set text style(BOLD, UNDERLINE, STRIKE, SUB_PIXEL, ITALIC)
	- **setTextSize(size: Float)** - set text size
	- **setTextColor(color: Int)** - set text color
	- **setTypeface(typeface: Typeface)** set text font
	- **setTextOffset(offset: PointF)** set text offset position
* **PointConverter** - main class which allows convert positions of graphic items. There are 3 types of **PointConverter**:
	- **DefaultPointConverter** - doesn't convert any positions
	- **PercentagePointConverter** - convert all points with percantage aspect ratio to view size. Positions can be in 		[0..1]. If position is out of range converted position will be out of view size.
	- **CoordinateConverter** - convert all positions from old view bounds(width and height) with aspect ratio to current 		   view size 

Set *OnMarkClickListener* to view

*PathShape* contains of:
```kotlin
	pathShapeView.setOnMarkClickListener(object : PathShapeView.OnMarkClickListener {
                    override fun onMarkClick(markId: Int, markItem: MarkItem) {
                         //To change body of created functions use File | Settings | File Templates.
                    }
                })
```
## Samples

* Sample for drawing shapes

![shapes_small](https://user-images.githubusercontent.com/34940037/35919086-a19a2992-0c1c-11e8-8895-ec33c12d3621.jpg)

Draw triangle with rounded corners:
```kotlin
	var list = arrayListOf<PathShape>()
        var pathProvider = PathProvider()
        var points = ArrayList<PointF>()
        points.add(PointF(0.1f, 0.9f))
        points.add(PointF(0.5f, 0.1f))
        points.add(PointF(0.9f, 0.9f))
        pathProvider.putLines(points, true, PathProvider.PathOperation.ADD)
        var body = BodyFillProvider()
        body.setColor(Color.GRAY)
        body.setRoundedCorners(30f)
        var contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(10f)
        contour.setRoundedCorners(30f)
        var pathShape = PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter())
```
Draw rect
```kotlin
	...
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
	...
```
Draw oval
```kotlin
	...
        pathProvider.putOval(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
	...
```
Draw arc
```kotlin
	...
        pathProvider.putArc(PointF(0.5f, 0.5f), 0.9f, 0.7f, 30f, 230f, PathProvider.PathOperation.ADD)
	...
```

* Dashed contour sample

![contour_dots](https://user-images.githubusercontent.com/34940037/35941915-9962edb4-0c5c-11e8-9176-04f6e1ff5195.jpg)
```kotlin
	val pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        val contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(20f)
        contour.addDotParams(20f, 40f)
        contour.addDotParams(40f, 40f)
        contour.addDotParams(40f, 40f)
        contour.setIsDotRounded(true)
        ...
```

* Shape with gradient sample

![gradients](https://user-images.githubusercontent.com/34940037/35941978-cf31331a-0c5c-11e8-8a1a-f566996b7e42.jpg)
```kotlin
	var pathProvider = PathProvider()
        pathProvider.putRoundRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, 0.2f, PathProvider.PathOperation.ADD)
        var gradient = GradientProvider()
        gradient.addColor(Color.BLUE)
                .addColor(Color.WHITE)
                .addColor(Color.BLUE)
                .setType(GradientProvider.Type.SWEEP)
        var body = BodyFillProvider()
        body.setGradient(gradient)
        gradient = GradientProvider()
        gradient.addColor(Color.BLACK, 0f)
                .addColor(Color.RED,0.1f)
                .addColor(Color.WHITE,0.5f)
                .addColor(Color.RED,0.9f)
                .addColor(Color.BLACK,1f)
                .setType(GradientProvider.Type.LINEAR)
        var contour = ContourFillProvider()
        contour.setGradient(gradient)
        contour.setWidth(100f)
        ...
```

* Logical operations with shapes

![set-shapes](https://user-images.githubusercontent.com/34940037/35942689-226e004c-0c5f-11e8-8561-db5d1f64098b.jpg)
```kotlin
	var pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        pathProvider.putOval(PointF(0.5f, 0.5f), 0.6f, 1f, PathProvider.PathOperation.JOIN)
        pathProvider.putOval(PointF(0.5f, 0.5f), 1f, 0.6f, PathProvider.PathOperation.JOIN)
        var contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(20f)
        var body = BodyFillProvider()
        body.setColor(Color.LTGRAY)
	...
```
```kotlin
	...
	pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        pathProvider.putOval(PointF(0.5f, 0.5f), 0.6f, 1f, PathProvider.PathOperation.INTERSECT)
        pathProvider.putOval(PointF(0.5f, 0.5f), 1f, 0.6f, PathProvider.PathOperation.INTERSECT)
	...
```
```kotlin
	...
	pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        pathProvider.putOval(PointF(0.5f, 0.5f), 0.6f, 1f, PathProvider.PathOperation.SUB)
        pathProvider.putOval(PointF(0.5f, 0.5f), 1f, 0.6f, PathProvider.PathOperation.SUB)
	...
```
```kotlin
	...
	pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        pathProvider.putOval(PointF(0.5f, 0.5f), 0.6f, 0.9f, PathProvider.PathOperation.SUB_REVERSE)
        pathProvider.putOval(PointF(0.5f, 0.5f), 0.9f, 0.6f, PathProvider.PathOperation.SUB_REVERSE)
	...
```

* Sample with marks

![marks](https://user-images.githubusercontent.com/34940037/36018528-ba0222b4-0d84-11e8-8b8c-ae08bc655a61.jpg)
```kotlin
	var points = ArrayList()
        points.add(PointF(0.1f, 0.1f))
        points.add(PointF(0.5f, 0.3f))
        points.add(PointF(0.6f, 0.4f))
        points.add(PointF(0.7f, 0.6f))
        points.add(PointF(0.9f, 0.8f))
        var pathProvider = PathProvider()
        pathProvider.putLines(points, false, PathProvider.PathOperation.ADD)
        var contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(20f)
        var mark = Mark()
        mark.setDrawable(R.mipmap.ic_launcher)
        mark.fitDrawableToSize(50f,50f)
        var tc = TextConfigurator()
        tc.setTextColor(Color.BLUE)
        tc.setStyle(TextConfigurator.Style.BOLD, TextConfigurator.Style.UNDERLINE)
        tc.setTextSize(20f)
        tc.setTextOffset(PointF(0f, -30f))
        mark.setTextConfigurator(tc)
        points.forEach { mark.addPosition(it, it.toString()) }
        var pathShape = PathShape.create()
                .setPath(pathProvider)
                .fillContour(contour)
                .addMark(mark)
                .setPointConverter(PercentagePointConverter()
```

* Text sample

![text_sample](https://user-images.githubusercontent.com/34940037/36065565-5a22d7d6-0ea5-11e8-90df-38e7922ff974.jpg)
```kotlin
 	var pathProvider = PathProvider()
        var tc = TextConfigurator()
        tc.setStyle(TextConfigurator.Style.BOLD, TextConfigurator.Style.ITALIC)
        pathProvider.putText(PointF(0.5f, 0.5f), 0.5f, 0.2f,"Hello!", tc, PathProvider.PathOperation.ADD)
        ...
```

* Sample with effects

![effects](https://user-images.githubusercontent.com/34940037/36228997-7dbb8cec-11de-11e8-8488-17f98be755fd.jpg)  
Glow effect
```kotlin
	...
        contour.setGlowEffect(30f, FillProvider.GlowType.SOLID)
        ...
```
Emboss effect
```kotlin
	body.setEmbossEffect(45f, FillProvider.EmbossType.NORMAL)
```
Shadow
```kotlin
	contour.setShadow(15f, 10f, 10f, Color.BLACK)
```

* Sample with list of custom wave shapes

![wave_items](https://user-images.githubusercontent.com/34940037/36630163-8eb9c91c-196a-11e8-95d2-c0a8ee99e0c8.jpg)
```kotlin
	val pathProvider = PathProvider()
        val f = WaveFunction(0.2f, 0.1f, WaveType.SINE)
        f.offset(0f, 0.85f)
        val shape = CustomLinesBuilder()    
        shape.addGraphPoints( 0f, 1f, -1f, 1f, f)
        shape.addPoint(1f, 0f)
        shape.addPoint(0f, 0f)
        shape.setClosed(true)
        pathProvider.putCustomShape(shape, PathProvider.PathOperation.ADD)
	...
```

## License

   Copyright (c) 2018 gleb8k

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
