package com.nas.healthandsafety.custom_fields.text_view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class CustomFontTextViewSemiBoldNoColor : androidx.appcompat.widget.AppCompatTextView{
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        // ...
        val type= Typeface.createFromAsset(context.assets,"fonts/SourceSansPro-Semibold.otf")
        this.typeface = type
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        val type =
            Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Semibold.otf")
        this.typeface = type
    }
}