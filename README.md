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
