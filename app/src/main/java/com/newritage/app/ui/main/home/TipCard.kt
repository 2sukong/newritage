package com.newritage.app.ui.main.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.newritage.app.R

class TipCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_tip_card, this, true)
        setBackgroundResource(R.drawable.bg_card_rounded)
        val padding = resources.getDimensionPixelSize(R.dimen.spacing_md)
        setPadding(padding, padding, padding, padding)
    }
}
