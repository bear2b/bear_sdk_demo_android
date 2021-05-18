package com.bear2b.sampleapp.utils.extension

import android.view.View
import android.view.ViewGroup

fun View.setCutoutMargin(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(if (leftMargin != 0) leftMargin else params.leftMargin,
            if (topMargin != 0) topMargin else params.topMargin,
            if (rightMargin != 0) rightMargin else params.rightMargin,
            if (bottomMargin != 0) bottomMargin else params.bottomMargin)
    layoutParams = params
}