package com.ergonotes.mainfragment.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.R
import com.ergonotes.database.NoteEntry
import com.ergonotes.databinding.ListItemTitleBinding
import com.ergonotes.fragments.MainFragment
import com.ergonotes.fragments.MainFragmentDirections


class NoteEntryAdapterTitles(private val clickListener: NoteEntryTitlesListener) :
    ListAdapter<NoteEntry, NoteEntryAdapterTitles.ViewHolder>(NoteEntryTitlesDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteEntry, clickListener: NoteEntryTitlesListener) {
            binding.title = item
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

//        init {
//            binding.titleString.setOnLongClickListener {
//                val pop = PopupMenu(binding.titleString.context, it)
//                pop.inflate(R.menu.show_menu)
//
//                pop.setOnMenuItemClickListener { item ->
//
//                    when (item.itemId) {
//                        R.id.context_menu_editText -> {
//
//                        }
//                        R.id.context_menu_delete -> {
//                            val action = MainFragmentDirections
//                                .actionMainFragmentToDialogFragment((binding.title!!.noteId))
//                            itemView.findNavController().navigate(action)
//                        }
//                    }
//                    true
//                }
//                pop.show()
//                true
//            }
//        }
    }
}


class NoteEntryTitlesDiffCallback : DiffUtil.ItemCallback<NoteEntry>() {
    override fun areItemsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: NoteEntry, newItem: NoteEntry): Boolean {
        return oldItem == newItem
    }
}

class NoteEntryTitlesListener(val clickListener: (noteId: Long) -> Unit) {
    fun onClick(note: NoteEntry) = clickListener(note.noteId)
}