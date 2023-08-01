package com.example.space.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.space.data.Note
import com.example.space.databinding.NotesListItemBinding

class NotesAdapter(
    private val notesList: LiveData<List<Note>>,
    private val itemOnClickListener: (Long) -> Unit) : ListAdapter<Note, NotesAdapter.NotesViewHolder>(DiffCallback) {
    lateinit var binding:NotesListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(NotesListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class NotesViewHolder(private var binding: NotesListItemBinding):RecyclerView.ViewHolder(binding.root){
        init {
                binding.cardLayout.setOnClickListener{
                    getItem(adapterPosition)?.id?.let{
                        it->itemOnClickListener.invoke(it)
                    }
                }
        }
        fun bind(note: Note){
            binding.apply {
                notesListItemTitle.text=note.title
                notesListItemNote.text=note.content
            }
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldNote: Note , newNote: Note): Boolean {
                return oldNote === newNote
            }

            override fun areContentsTheSame(oldNote: Note, newNote: Note): Boolean {
                return oldNote.title==oldNote.title
            }
        }
    }
    override fun getItem(position: Int): Note? {
        return notesList.value?.get(position)
    }
}