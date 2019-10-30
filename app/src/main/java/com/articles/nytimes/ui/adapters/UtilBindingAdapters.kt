
package com.articles.nytimes.ui.adapters


import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.articles.nytimes.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.squareup.picasso.NetworkPolicy


@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {


    if (!imageUrl.isNullOrEmpty()) {

        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.place_holder)
        Glide.with(view.context).load(imageUrl).apply(requestOptions).into(view)

    }
}



@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean?) {
    if (isGone == null || isGone) {
        collapse(view)
    } else {
        expand(view)
    }
}


/***
 *
 * @param expandView
 */
fun expand(expandView: View) {
    //set Visible
    expandView.visibility = View.VISIBLE

    val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    expandView.measure(widthSpec, heightSpec)
    val mAnimator = slideAnimator(0, expandView.measuredHeight, expandView)
    mAnimator.start()
}

/**
 *
 * @param start
 * @param end
 * @param expandView
 * @return
 */
private fun slideAnimator(start: Int, end: Int, expandView: View): ValueAnimator {
    val animator = ValueAnimator.ofInt(start, end)
    animator.addUpdateListener { valueAnimator ->
        //Update Height
        val value = valueAnimator.animatedValue as Int
        val layoutParams = expandView.layoutParams
        layoutParams.height = value
        expandView.layoutParams = layoutParams
    }
    return animator
}

/***
 *@param view
 */
fun collapse(view: View) {

    val mAnimator = slideAnimator(view.measuredHeight, 0, view)
    mAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            view.visibility = View.GONE
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })

    mAnimator.start()
}

