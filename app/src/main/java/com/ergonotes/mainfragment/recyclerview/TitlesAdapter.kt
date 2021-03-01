package com.ergonotes.mainfragment.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.database.NoteEntry
import com.ergonotes.databinding.ListItemTitleBinding

class TitlesAdapter(
    private var onEdit: (NoteEntry) -> Unit
) : ListAdapter<NoteEntry, TitlesAdapter.TitlesViewHolder>(TitlesDiffCallback()) {

    class TitlesViewHolder(
        private val binding: ListItemTitleBinding,
        private var onEdit: (NoteEntry) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        private var noteId: Long = -1
        private var titleString = binding.titleString
        private var note: NoteEntry? = null

        fun bind(item: NoteEntry) {
            noteId = item.noteId
            titleString.text = item.noteEntryTitle
           this.note = item

            binding.root.setOnClickListener {
                onEdit(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): TitlesViewHolder {

        return TitlesViewHolder(
            ListItemTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ),
            onEdit
        )
    }

    override fun onBindViewHolder(holder: TitlesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class TitlesDiffCallback : DiffUtil.ItemCallback<NoteEntry>() {
    override fun areItemsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem == newItem
    }
}