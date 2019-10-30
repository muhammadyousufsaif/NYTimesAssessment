package com.articles.nytimes.ui.views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.articles.nytimes.R


class ExpandableLinearLayout : LinearLayout {

    var isExpanded: Boolean = false
        set(expanded) {
            Log.e("layout", expanded.toString() + "")
            field = expanded
        }
    private var duration: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    private fun init(attributeSet: AttributeSet?) {
        val customValues =
            context.obtainStyledAttributes(attributeSet, R.styleable.ExpandableLinearLayout)
        duration = customValues.getInt(R.styleable.ExpandableLinearLayout_expandDuration, -1)
        customValues.recycle()
    }

    fun toggle() {
        if (isExpanded)
            expandView(this)
        else
            hideView(this)
    }

    private fun expandView(view: View) {
        view.measure(
            WindowManager.LayoutParams.MATCH_PARENT,
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val targetHeight = view.measuredHeight
        view.layoutParams.height = 1
        view.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view.layoutParams.height = if (interpolatedTime == 1f)
                    targetHeight
                else
                    (targetHeight * interpolatedTime).toInt()
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        if (duration == -1)
            a.duration =
                (targetHeight / view.context.resources.displayMetrics.density * 1.5).toInt()
                    .toLong()
        else
            a.duration = duration.toLong()
        view.startAnimation(a)
    }

    private fun hideView(view: View) {
        val initialHeight = view.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        if (duration == -1)
            a.duration =
                (initialHeight / view.context.resources.displayMetrics.density * 1.5).toInt()
                    .toLong()
        else
            a.duration = duration.toLong()
        view.startAnimation(a)
    }

}
