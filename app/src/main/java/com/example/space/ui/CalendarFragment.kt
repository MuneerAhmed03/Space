package com.example.space.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.space.R
import com.example.space.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {
    private var binding : FragmentCalendarBinding?= null

    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var selectedDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        initWidgets()
        selectedDate = LocalDate.now()
        setMonthView()
        binding?.apply {
            prev.setOnClickListener { previousMonthAction(view) }
            next.setOnClickListener { nextMonthAction(view) }
        }
        return fragmentBinding.root
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }
    private fun initWidgets() {
        calendarRecyclerView = binding?.calendarRecyclerView
        monthYearText = binding?.monthYearTV
    }

    //sets the month view whenever month is changed
    private fun setMonthView() {
        //for the month year heading
        monthYearText!!.text = monthYearFromDate(selectedDate)
        //dataset
        val daysInMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate?): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        //give the year and month of the date passed in the form of 2007-12
        val yearMonth = YearMonth.from(date)
        //gives days in month
        val daysInMonth = yearMonth.lengthOfMonth()
        //withDayOfMonth() gives the the details of the mentioned date of the current date's month and year
        val firstOfMonth = selectedDate!!.withDayOfMonth(1)
        //what is the day on first date of month.
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        //prepares array for the month
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                //blank spaces to in calendar for the days befroe the first day of the month
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date!!.format(formatter)
    }

    fun previousMonthAction(view: View?) {
        selectedDate = selectedDate!!.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        selectedDate = selectedDate!!.plusMonths(1)
        setMonthView()
    }

    override fun onItemClick(position: Int, dayText: String?) {
        if (dayText != "") {
            val message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate)
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

}