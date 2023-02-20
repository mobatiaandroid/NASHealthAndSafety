package com.nas.healthandsafety.custom_fields.text_view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.nas.healthandsafety.R

class CustomFontTextViewBoldWhite : androidx.appcompat.widget.AppCompatTextView{
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        // ...
        val type= Typeface.createFromAsset(context.assets,"fonts/SourceSansPro-Bold.otf")
        this.typeface = type
        this.setTextColor(resources.getColor(R.color.white))
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        val type =
            Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Bold.otf")
        this.typeface = type
        this.setTextColor(context.resources.getColor(R.color.white))
    }
}