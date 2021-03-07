package com.ergonotes.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.database.NoteEntry
import com.ergonotes.databinding.ListItemNoteBinding


class NotesAdapter : ListAdapter<NoteEntry, RecyclerView.ViewHolder>(NotesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotesViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = getItem(position)
        (holder as NotesViewHolder).bind(note)
    }
}

class NotesViewHolder(private val binding: ListItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NoteEntry) {
        binding.apply {
            binding.note = item
            executePendingBindings()
        }
    }
}

private class NotesDiffCallback : DiffUtil.ItemCallback<NoteEntry>() {

    override fun areItemsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem == newItem
    }
}

