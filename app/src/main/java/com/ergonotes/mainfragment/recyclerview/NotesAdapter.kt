package com.ergonotes.mainfragment.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.database.NoteEntry
import com.ergonotes.databinding.ListItemNoteBinding

class NotesAdapter(

) : ListAdapter<NoteEntry, NotesAdapter.NotesViewHolder>(NotesDiffCallback()) {

    class NotesViewHolder(
        private val binding: ListItemNoteBinding

    ) : RecyclerView.ViewHolder(binding.root) {
        private var noteId: Long = -1
        private var noteString = binding.noteString
        private var note: NoteEntry? = null

        fun bind(item: NoteEntry) {
            noteId = item.noteId
            noteString.text = item.noteEntryNote
            this.note = item
        }
    }






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): NotesViewHolder {

        return NotesViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )

        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class NotesDiffCallback : DiffUtil.ItemCallback<NoteEntry>() {
    override fun areItemsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem == newItem
    }
}