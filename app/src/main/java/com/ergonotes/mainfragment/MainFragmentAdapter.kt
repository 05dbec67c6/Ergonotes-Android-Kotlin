package com.ergonotes.mainfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.database.NoteEntry
import com.ergonotes.databinding.ListItemTitleBinding

class MainFragmentAdapter(val clickListener: MainFragmentListener) : ListAdapter<NoteEntry,
        MainFragmentAdapter.ViewHolder>(NoteEntryDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: MainFragmentListener, item: NoteEntry) {
            binding.note = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTitleBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class NoteEntryDiffCallback : DiffUtil.ItemCallback<NoteEntry>() {
    override fun areItemsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem == newItem
    }
}

class MainFragmentListener(val clickListener: (noteId: Long) -> Unit) {
    fun onClick(note: NoteEntry) = clickListener(note.noteId)
}