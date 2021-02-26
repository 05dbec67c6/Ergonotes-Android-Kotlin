package com.ergonotes.mainfragment.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.database.NoteEntry
import com.ergonotes.databinding.ListItemNoteBinding

class NoteEntryAdapterNotes(val clickListener: NoteEntryNotesListener) : ListAdapter<NoteEntry,
        NoteEntryAdapterNotes.ViewHolder>(NoteEntryNotesDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteEntry, clickListener: NoteEntryNotesListener) {
            binding.note = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNoteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class NoteEntryNotesDiffCallback : DiffUtil.ItemCallback<NoteEntry>() {
    override fun areItemsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem == newItem
    }
}

class NoteEntryNotesListener(val clickListener: (noteId: Long) -> Unit) {
    fun onClick(note: NoteEntry) = clickListener(note.noteId)
}

