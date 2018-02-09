# Android-PathShapeView
[![](https://jitpack.io/v/gleb8k/Android-PathShapeView.svg)](https://jitpack.io/#gleb8k/Android-PathShapeView)
[![](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/gleb8k/Android-PathShapeView/blob/master/LICENSE)

This library allows to draw different shapes, lines, marks easily. It's customizable and provides posibility to fill your custom shapes by color or gradient. Also you can fill just by stroke with color or gradient. If you want to add some labels or marks on your shapes or lines it's not difficult with this toolbox.

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
	compile 'com.github.gleb8k:Android-PathShapeView:1.1.4'
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
	pathShapeView.setPath(pathShape)
	...
```
*PathShape* contains of:

* **PathProvider** - is the main class which allow to create different graphic items. Each item can be added with the logical operation (**PathOperation**: ADD, SUB, SUB_REVERSE, JOIN, INTERSECT, XOR)
Just to simple add item use **ADD** operation.
There are several items which you can create:
	- **putLines(list: List<PointF>, isClosed:Boolean, operation: PathOperation)** - add lines by list of points,
	      **isClosed** - allows to close the current contour.
	- **putArc(centerPoint: PointF, width:Float, height:Float, startAngle: Float, sweepAngle: Float, 	operation:		PathOperation)** - add arc to the path as a new contour
	- **putOval(centerPoint: PointF, width:Float, height:Float, operation: PathOperation)** - add a closed oval contour
	- **putCircle(centerPoint: PointF, radius:Float, operation: PathOperation)** - add a closed circle contour
	- **putRect(centerPoint: PointF, width:Float, height:Float, operation: PathOperation)** - add a closed rectangle 		contour
	- **putRoundRect(centerPoint: PointF, width:Float, height:Float, cornerRadius: Float, operation: PathOperation)** - 		   add a closed round-rectangle contour
	
* **BodyFillProvider** - class which allows to fill your graphic items. There are methods of **BodyFillProvider**:
	- **setColor(color: Int)** - set the fill color
	- **setGradient(gradient: GradientProvider)** - set the fill gradient. There are methods of **GradientProvider**:  
	     - **gradient.setType(type: Type)** - gradient can be(*LINEAR*, *RADIAL* or *SWEEP*)  
	     - **gradient.setAngle(angle: Float)** - set the angle of gradient direction  
	     - **gradient.setLength(length: Float)** - set the length of gradient, by default it fills fit view size  
	     - **gradient.setStartPoint(startPoint: PointF)** - set the start position of gradient  
	     - **gradient.addColor(color: Int)** - add new color to gradient  
	     - **gradient.addColor(color: Int, colorPosition: Float)** - add new color to gradient with color position, 		  colorPosition ca be in [0..1]  
	- **setRoundedCorners(radius: Float)** - set all corners rounded with radius     
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
	- **setDrawable(drawable: Drawable) - set image drawable to current mark
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
	- **PercentagePointConverter** - convert all points with percantage aspect ratio to view size. Positions can be in 		[0..1]. If position out of range converted position will be out of view size.
	- **CoordinateConverter** - convert all positions from old view bounds(width and height) with aspect ratio to current 		   view size 

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
        val pathShape = PathShape.create()
                .setPath(pathProvider)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter())
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
        var pathShape = PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter())
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
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))
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

# License

MIT License

Copyright (c) 2018 gleb8k

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
