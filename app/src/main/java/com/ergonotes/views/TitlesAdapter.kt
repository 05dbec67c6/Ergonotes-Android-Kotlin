package com.ergonotes.views

import android.view.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.database.NoteEntry
import com.ergonotes.databinding.ListItemTitleBinding
import com.ergonotes.fragments.MainFragmentDirections


class TitlesAdapter : ListAdapter<NoteEntry, RecyclerView.ViewHolder>(TitlesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TitlesViewHolder(
            ListItemTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = getItem(position)
        (holder as TitlesViewHolder).bind(note)
    }
}

class TitlesViewHolder(private val binding: ListItemTitleBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener,
    View.OnCreateContextMenuListener {

    fun bind(item: NoteEntry) {
        binding.apply {
            binding.note = item
            executePendingBindings()
        }
    }

    init {
        itemView.setOnClickListener(this)
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onClick(v: View?) {
        binding.note?.let { noteEntry: NoteEntry ->
            navigateToNoteEntry(noteEntry, itemView)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        val edit = menu!!.add(Menu.NONE, 1, 1, "Edit")
        val delete = menu.add(Menu.NONE, 2, 2, "Delete")
        edit.setOnMenuItemClickListener(onEditMenu)
        delete.setOnMenuItemClickListener(onEditMenu)
    }

    private val onEditMenu: MenuItem.OnMenuItemClickListener =
        MenuItem.OnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> {
                    binding.note?.let { noteEntry: NoteEntry ->
                        navigateToEdit(noteEntry, itemView)
                    }
                }
                2 -> {
                    binding.note?.let { noteEntry: NoteEntry ->
                        navigateToDelete(noteEntry, itemView)
                    }
                }
            }
            true
        }
}

private fun navigateToNoteEntry(note: NoteEntry, view: View) {
    val direction = MainFragmentDirections.actionMainFragmentToNewFragment(note.noteId)
    view.findNavController().navigate(direction)
}

private fun navigateToDelete(note: NoteEntry, view: View) {
    val directionToDelete = MainFragmentDirections.actionMainFragmentToDialogFragment(note.noteId)
    view.findNavController().navigate(directionToDelete)
}

private fun navigateToEdit(note: NoteEntry, view: View) {
    val directionToEdit = MainFragmentDirections.actionMainFragmentToEditFragment(note.noteId)
    view.findNavController().navigate(directionToEdit)
}

private class TitlesDiffCallback : DiffUtil.ItemCallback<NoteEntry>() {

    override fun areItemsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem == newItem
    }
}


