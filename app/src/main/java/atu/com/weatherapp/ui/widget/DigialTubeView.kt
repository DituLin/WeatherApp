package atu.com.weatherapp.ui.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.util.Log
import android.util.TypedValue
import atu.com.weatherapp.R
import atu.com.weatherapp.extensions.toWeekly
import java.util.*
import kotlin.concurrent.timerTask


/**
 *
 * 数码管时钟显示器
 * Created by atu on 2017/8/7.
 */

class DigialTubeView : View {

    private lateinit var mPaint: Paint
    var mCenterX: Float = 0.0f
    var mOffset: Float = 80f
    var mCenterY: Float = 0.0f
    val mTubeHY: Float = 36f
    val mTubeX: Float = 16f
    val mTubeY: Float = 18f

    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    fun initPaint() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.color = context.resources.getColor(R.color.color_03a9f4)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mCenterX = measuredWidth / 2f
        mCenterY = measuredHeight / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val offset = dp2px(mOffset)
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        drawDigitalTube(canvas,hour / 10,-offset*2.5f,mTubeHY,mTubeX,mTubeY,8f,10f)
        drawDigitalTube(canvas,hour % 10,-offset*1.5f,mTubeHY,mTubeX,mTubeY,8f,10f)

        drawDigitalTubeDot(canvas,-offset*0.8f)

        drawDigitalTube(canvas,min / 10,0f,mTubeHY,mTubeX,mTubeY,8f,10f)
        drawDigitalTube(canvas,min % 10,offset,mTubeHY,mTubeX,mTubeY,8f,10f)

        drawDigitalTube(canvas,second / 10,offset*1.7f,0f,2f,5f,5f,5f)
        drawDigitalTube(canvas,second % 10,offset*2,0f,2f,5f,5f,5f)

        mPaint.alpha = 255
        mPaint.strokeWidth = 5f
        mPaint.textSize = 50f
        canvas.drawText(calendar.toWeekly(),mCenterX + (-offset*2.2f),mCenterY + dp2px(mTubeHY) + 80,mPaint)

    }


    /**
     * 数码管样式
     */
    //      1
    //      ——
    //   2 |  | 3
    //      —— 4
    //   5 |  | 6
    //      ——
    //       7
    fun drawDigitalTube(canvas: Canvas,num: Int,xOffset: Float,tubeHy: Float,tubeX: Float,tubeY: Float,gap: Float,width: Float) {

        val x = mCenterX + xOffset
        val y = mCenterY - dp2px(tubeHy)
        val lx = dp2px(tubeX)
        val ly = dp2px(tubeY)
        val gap = dp2px(gap)
        mPaint.strokeWidth = dp2px(width)


        // 1
        mPaint.alpha = if (num === -1 || num === 1 || num === 4) 25 else 255
        //利用 path 画 多边形
        canvas.drawLine(x - lx, y, x + lx, y, mPaint)
        // 2
        mPaint.alpha = if (num === -1 || num === 1 || num === 2 || num === 3 || num === 7) 25 else 255
        canvas.drawLine(x - lx.toFloat() - gap.toFloat(), y + gap, x - lx.toFloat() - gap.toFloat(), y + gap.toFloat() + ly.toFloat(), mPaint)
        // 3
        mPaint.alpha = if (num === -1 || num === 5 || num === 6) 25 else 255
        canvas.drawLine(x + lx.toFloat() + gap.toFloat(), y + gap, x + lx.toFloat() + gap.toFloat(), y + gap.toFloat() + ly.toFloat(), mPaint)
        // 4
        mPaint.alpha = if (num === -1 || num === 0 || num === 1 || num === 7) 25 else 255
        canvas.drawLine(x - lx, y + (gap * 2).toFloat() + ly.toFloat(), x + lx, y + (gap * 2).toFloat() + ly.toFloat(), mPaint)
        // 5
        mPaint.alpha = if (num === -1 || num === 1 || num === 3 || num === 4 || num === 5 || num === 7
                || num === 9)
            25
        else
            255
        canvas.drawLine(x - lx.toFloat() - gap.toFloat(), y + (gap * 3).toFloat() + ly.toFloat(),
                x - lx.toFloat() - gap.toFloat(), y + (gap * 3).toFloat() + (ly * 2).toFloat(), mPaint)
        // 6
        mPaint.alpha = if (num === -1 || num === 2) 25 else 255
        canvas.drawLine(x + lx.toFloat() + gap.toFloat(), y + (gap * 3).toFloat() + ly.toFloat(),
                x + lx.toFloat() + gap.toFloat(), y + (gap * 3).toFloat() + (ly * 2).toFloat(), mPaint)
        // 7
        mPaint.alpha = if (num === -1 || num === 1 || num === 4 || num === 7) 25 else 255
        canvas.drawLine(x - lx, y + (gap * 4).toFloat() + (ly * 2).toFloat(), x + lx, y + (gap * 4).toFloat() + (ly * 2).toFloat(), mPaint)

    }


    fun drawDigitalTubeDot(canvas: Canvas,xOffset: Float) {
        //画两个矩形 点
        val x = mCenterX + xOffset
        val y = mCenterY - dp2px(16f)
        val lx = dp2px(9f)
        val ly = dp2px(9f)
        mPaint.style = Paint.Style.FILL
        mPaint.alpha = 255
        canvas.drawRect(x,y,x + lx,y + lx,mPaint)
        canvas.drawRect(x,mCenterY + dp2px(16f),x + ly,mCenterY + dp2px(16f) + ly,mPaint)
    }


    fun dp2px(dp: Float): Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().displayMetrics)
    }

    fun sp2px(sp: Float): Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().displayMetrics)
    }

    fun start() {
        //时钟计时器
        Timer().schedule(timerTask {
            postInvalidate()
        },0,1000)
    }
}
