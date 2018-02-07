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
There are several items which you can create
	- **putLines(list: List<PointF>, isClosed:Boolean, operation: PathOperation)** - add lines by list of points,
	      **isClosed** - allows to close the current contour.
	- **putArc(centerPoint: PointF, width:Float, height:Float, startAngle: Float, sweepAngle: Float, 	operation:		PathOperation)** - add arc to the path as a new contour
	- **putOval(centerPoint: PointF, width:Float, height:Float, operation: PathOperation)** - add a closed oval contour
	- **putCircle(centerPoint: PointF, radius:Float, operation: PathOperation)** - add a closed circle contour
	- **putRect(centerPoint: PointF, width:Float, height:Float, operation: PathOperation)** - add a closed rectangle 		contour
	- **putRoundRect(centerPoint: PointF, width:Float, height:Float, cornerRadius: Float, operation: PathOperation)** - 		   add a closed round-rectangle contour
	
* **BodyFillProvider** - class which allows to fill your graphic items
	- **setColor(color: Int)** - set the fill color
	- **setGradient(gradient: GradientProvider)** - set the fill gradient
	     **gradient.setType(type: Type)** - gradient can be(*LINEAR*, *RADIAL* or *SWEEP*)
	     **gradient.setAngle(angle: Float)** - set the angle of gradient direction
	     **gradient.setLength(length: Float)** - set the length of gradient, by default it fills fit view size
	     **gradient.setStartPoint(startPoint: PointF)** - set the start position of gradient
	     **gradient.addColor(color: Int)** - add new color to gradient
	     **gradient.addColor(color: Int, colorPosition: Float)** - add new color to gradient with color position, 		     colorPosition ca be in [0..1]
	- **setRoundedCorners(radius: Float)** - set all corners rounded with radius     
* **ContourFillProvider** - class which allows to draw the contour of your graphic items. Has the same methods with the 	**BodyFillProvider** class and several specified methods:
	- **setWidth(width: Float)** - set the width of contour
	- **setIsDotRounded(isDotRounded: Boolean)** - if your contour is dashed it allows to round your dots
	- **addDotParams(dotLength: Float, dotDistance: Float)** - set your contour is dashed and add dot params. You can add 	    params more than one time. Each new params will configure the next dot.
* list of **Mark** items
* **PointConverter**

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

...
```kotlin
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
```
...
