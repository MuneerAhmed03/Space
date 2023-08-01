package com.example.space.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.space.R
import com.example.space.data.Note
import com.example.space.databinding.FragmentAddEventBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class AddEventFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: NotesViewModel
    private var _binding : FragmentAddEventBinding?=null
    private val binding get() = _binding!!
    private var dates:MutableList<String>?=null
    private var id: Long = 0L
    private var dateText: String? = null

    lateinit var note:Note

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Set the behavior of the BottomSheetDialog to fit its content
        dialog.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val behavior = bottomSheetDialog.behavior
            behavior?.let {
                setPrivateField(it, "fitToContents", true)
            }
        }

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (resources.displayMetrics.heightPixels * 0.8).toInt())

        return dialog
    }
    private fun setPrivateField(obj: Any, fieldName: String, value: Any) {
        try {
            val field = obj.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(obj, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL,R.style.fullscreendialog)
        viewModel=ViewModelProvider(this)[NotesViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentAddEventBinding.inflate(inflater,container,false)
        val binding=_binding

        arguments?.let {
            id = it.getLong(ARG_ID, 0L)
            dateText = it.getString(ARG_DATE_TEXT)
        }
        Log.i("AddEventFragment", "dateText: $dateText")
        if(id>0L) {
            viewModel.getNotesById(id).observe(this.viewLifecycleOwner) { event ->
                Log.i("AddEventFragment", "idText: $id")
                note = event
                setData(note)
            }
        }
        val localDate = convertStringToLocalDate(dateText!!)
        dates=repetitions(localDate)
        binding?.apply {
            add.setOnClickListener {
                if (id==0L){
                    addNewEvent()
                    dismiss()
                }
                else{
                    updateEvent()
                    dismiss()
                }
            }
        }
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.add_event)
            bottomSheet?.let { sheet ->
                dialog.behavior.peekHeight = sheet.height
                sheet.parent.parent.requestLayout()
            }
        }
    }

    private fun addNewEvent() {
        for (date in dates!!) {
                viewModel.addNote(
                    binding.titleText.text.toString(),
                    binding.noteText.text.toString(),
                    date
                )
            }

    }

    private fun setData(note: Note){
        val binding = _binding!!
        binding.notesDetailTitle.editText?.setText(note.title)
        binding.notesDetailNote.editText?.setText(note.content)
        binding.dismiss.setOnClickListener { showConfirmationDialog() }
    }
    private fun updateEvent(){
        if (dateText != null) {
            viewModel.updateNote(
                id!!,
                binding.titleText.text.toString(),
                binding.noteText.toString(),
                dateText!!
            )
        }
    }

private fun deleteNote() {
    val id = note.id
    val viewLifecycleOwner = viewLifecycleOwner
    val entitiesToDelete = mutableListOf<Note>()

    for (i in id..id + 2) {
        viewModel.getNotesById(i).observe(viewLifecycleOwner) { event ->
            event?.let {
                entitiesToDelete.add(it)

                if (entitiesToDelete.size == 5) {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.deleteNotes(entitiesToDelete)
                    }
                    dismiss()
                }
            }
        }
    }
}

    private fun showConfirmationDialog() {
        val id =note.id
        Log.i("AddEventFragment", "dateText: $id")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Are you sure you want to delete?")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                deleteNote()
            }
            .show()
    }


    companion object {
        private const val ARG_DATE_TEXT = "id"
        private const val ARG_ID="dateText"

        fun newInstance(id:Long,dateText: String): AddEventFragment {
            val args = Bundle().apply {
                putLong(ARG_ID,id)
                putString(ARG_DATE_TEXT, dateText)
            }
            val fragment = AddEventFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private fun convertStringToLocalDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)

        return LocalDate.parse(dateString, formatter)
    }
    private fun localDateToString(localDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        return localDate.format(formatter)
    }
    private fun repetitions(date: LocalDate): MutableList<String>? {
        val dates: MutableList<LocalDate> = mutableListOf(
            date.plusDays(3),
            date.plusDays(7),
            date.plusDays(30),
            date.plusDays(60)
        )
        return mutableListOf(
            localDateToString(date),
            localDateToString(dates[0]),
            localDateToString(dates[1]),
            localDateToString(dates[2]),
            localDateToString(dates[3]),
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
