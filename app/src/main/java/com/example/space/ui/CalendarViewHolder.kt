package com.example.space.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.space.R
import com.example.space.ui.CalendarAdapter.OnItemListener

class CalendarViewHolder constructor(itemView: View, onItemListener: OnItemListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    //@JvmField
    val dayOfMonth: TextView
    private val onItemListener: OnItemListener

    init {
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }
}