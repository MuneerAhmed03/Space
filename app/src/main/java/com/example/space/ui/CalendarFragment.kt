package com.example.space.ui


import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.space.R
import com.example.space.databinding.FragmentCalendarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {
    private var binding: FragmentCalendarBinding? = null
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var selectedDate: LocalDate? = null

    private lateinit var viewModel: NotesViewModel
    private lateinit var notesListAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NotesViewModel::class.java]
    }

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
                //blank spaces to in calendar for the days before the first day of the month
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
            showdialog(dayText)

            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    fun showdialog(dayText: String?) {

        val dateString = dayText + " " + monthYearFromDate(selectedDate)
        val todayNotes = viewModel.getTodayNotes(dateString)
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null)
        val recyclerView = customView.findViewById<RecyclerView>(R.id.notes_list)
        notesListAdapter = NotesAdapter(todayNotes) {
            AddEventFragment.newInstance(it, dateString)
                .show(childFragmentManager, "Add Event Fragment")
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = notesListAdapter
        todayNotes.observe(viewLifecycleOwner) {
            notesListAdapter.submitList(it)
        }
        val fab = customView.findViewById<FloatingActionButton>(R.id.fab)
        context?.let {
            val eventDialog = MaterialAlertDialogBuilder(it)
                .setView(customView)
                .setTitle(dayText + " " + monthYearFromDate(selectedDate))
                .create()
            val displayMetrics = DisplayMetrics()
            eventDialog.window?.apply {
//                windowManager.currentWindowMetrics
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val maxHeight = (displayMetrics.heightPixels * 0.8).toInt()

                // Set the height to 80% of the screen height
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight)
            }
            fab.setOnClickListener {
                AddEventFragment.newInstance(0, dateString)
                    .show(childFragmentManager, "Add Event Fragment")
            }
            eventDialog.show()
        }

    }
}
