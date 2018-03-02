package shape.path.view

import android.annotation.TargetApi
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.os.Build
import shape.path.view.graph.function.CustomLinesBuilder
import shape.path.view.point.converter.PointConverter

/**
 * Created by root on 1/9/18.
 */
class PathProvider {
    internal val path: Path = Path()
    internal var contourPath: Path? = null

    private var isBuilt = false

    enum class PathOperation {
        ADD,
        SUB,
        SUB_REVERSE,
        JOIN,
        INTERSECT,
        XOR
    }

    fun putLines(list: List<PointF>, isClosed:Boolean, operation: PathOperation) {
        if (list.isNotEmpty()) {
            val p = Path()
            p.moveTo(list[0].x, list[0].y)
            for (i in 1 until list.size) {
                p.lineTo(list[i].x, list[i].y)
            }
            if (isClosed) {
                p.close()
            }
            putPath(p, operation)
        }
    }

    fun putCustomShape(customLinesBuilder: CustomLinesBuilder, operation: PathOperation) {
        putPath(customLinesBuilder.getPath(), operation)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun putArc(centerPoint: PointF, width:Float, height:Float, startAngle: Float, sweepAngle: Float, operation: PathOperation) {
        val p = Path()
        val left = centerPoint.x - width / 2
        val top = centerPoint.y - height / 2
        val right = centerPoint.x + width / 2
        val bottom = centerPoint.y + height / 2
        val start = PointF(left, top)
        val end = PointF(right, bottom)
        p.addArc(start.x, start.y, end.x, end.y, startAngle, sweepAngle)
        putPath(p, operation)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun putOval(centerPoint: PointF, width:Float, height:Float, operation: PathOperation) {
        val p = Path()
        val left = centerPoint.x - width / 2
        val top = centerPoint.y - height / 2
        val right = centerPoint.x + width / 2
        val bottom = centerPoint.y + height / 2
        val start = PointF(left, top)
        val end = PointF(right, bottom)
        p.addOval(start.x, start.y, end.x, end.y, Path.Direction.CCW)
        putPath(p, operation)
    }

    fun putCircle(centerPoint: PointF, radius:Float, operation: PathOperation) {
        val p = Path()
        p.addCircle(centerPoint.x, centerPoint.y, radius, Path.Direction.CCW)
        putPath(p, operation)
    }

    fun putPoly(centerPoint: PointF, radius:Float, angleRotation: Float, sidesCount:Int, operation: PathOperation) {
        if (sidesCount < 3) {
            return
        }
        val p = Path()
        val a = Math.toRadians(angleRotation.toDouble())
        for (i in 0 until sidesCount) {
            val x = (Math.sin( i.toDouble() / sidesCount * 2 * Math.PI + a) * radius).toFloat() + centerPoint.x
            val y = (Math.cos( i.toDouble() / sidesCount * 2 * Math.PI + a) * radius).toFloat() + centerPoint.y
            if (i == 0) {
                p.moveTo(x, y)
            }
            else {
                p.lineTo(x, y)
            }
        }
        p.close()
        putPath(p, operation)
    }

    fun putText(centerPoint: PointF, width: Float, height: Float, text: String, textConfigurator: TextConfigurator, operation: PathOperation) {
        val p = textConfigurator.getPath(text, centerPoint, width, height)
        putPath(p, operation)
    }

    fun putRect(centerPoint: PointF, width:Float, height:Float, operation: PathOperation) {
        val p = Path()
        val left = centerPoint.x - width / 2
        val top = centerPoint.y - height / 2
        val right = centerPoint.x + width / 2
        val bottom = centerPoint.y + height / 2
        val start = PointF(left, top)
        val end = PointF(right, bottom)
        p.addRect(start.x, start.y, end.x, end.y, Path.Direction.CCW)
        putPath(p, operation)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun putRoundRect(centerPoint: PointF, width:Float, height:Float, cornerRadius: Float, operation: PathOperation) {
        val p = Path()
        val left = centerPoint.x - width / 2
        val top = centerPoint.y - height / 2
        val right = centerPoint.x + width / 2
        val bottom = centerPoint.y + height / 2
        val start = PointF(left, top)
        val end = PointF(right, bottom)
        p.addRoundRect(start.x, start.y, end.x, end.y, cornerRadius * 1.5f, cornerRadius, Path.Direction.CCW)
        putPath(p, operation)
    }

    fun reset() {
        path.reset()
        contourPath?.reset()
        isBuilt = false
    }

    private fun putPath(p: Path, operation: PathOperation) {
        when (operation) {
            PathOperation.ADD -> path.addPath(p)
            PathOperation.INTERSECT -> path.op(p, Path.Op.INTERSECT)
            PathOperation.SUB -> path.op(p, Path.Op.DIFFERENCE)
            PathOperation.SUB_REVERSE -> path.op(p, Path.Op.REVERSE_DIFFERENCE)
            PathOperation.JOIN -> path.op(p, Path.Op.UNION)
            PathOperation.XOR -> path.op(p, Path.Op.XOR)
        }
    }

    internal fun build(converter: PointConverter) {
        this.build(converter, 0f)
    }

    internal fun build(converter: PointConverter, contourWidth: Float) {
        if (!isBuilt) {
            val m = converter.getMatrix()
            if (!m.isIdentity) {
                path.transform(m)
            }
            fitContourPath(converter.screenWidth, converter.screenHeight, contourWidth)
            isBuilt = true
        }

    }

    private fun fitContourPath(screenWidth: Float, screenHeight: Float, contourWidth: Float) {
        if (contourWidth >= 0f) {
            contourPath = Path(path)
            val d = contourWidth / 2
            val r1 = RectF(0f, 0f, screenWidth, screenHeight)
            val r2 = RectF(d, d, screenWidth - d, screenHeight - d)
            val matrix = Matrix()
            matrix.setRectToRect(r1, r2, Matrix.ScaleToFit.FILL)
            contourPath?.transform(matrix)
            path.transform(matrix)
        }
    }

    internal fun hasContourPath(): Boolean {
        return contourPath != null
    }
}