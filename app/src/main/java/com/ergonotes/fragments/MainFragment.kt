package com.ergonotes.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentMainBinding
import com.ergonotes.mainfragment.recyclerview.NoteEntryAdapterNotes
import com.ergonotes.mainfragment.recyclerview.NoteEntryAdapterTitles
import com.ergonotes.mainfragment.recyclerview.NoteEntryNotesListener
import com.ergonotes.mainfragment.recyclerview.NoteEntryTitlesListener
import com.ergonotes.viewmodelfactories.MainFragmentViewModelFactory
import com.ergonotes.viewmodels.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// -------------------------------------------------------------------------------------------------
// Reference binding object and inflate fragment----------------------------------------------------

        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        val application = requireNotNull(this.activity).application

// -------------------------------------------------------------------------------------------------
// Setting up ViewModel and Factory-----------------------------------------------------------------

        // Create an instance of the ViewModel Factory
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory = MainFragmentViewModelFactory(dataSource)

        // Associate ViewModel with this Fragment
        val mainFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainFragmentViewModel::class.java)

        // Use View Model with data binding
        binding.mainFragmentViewModel = mainFragmentViewModel

// -------------------------------------------------------------------------------------------------
// Setting current activity as lifecycle owner of the binding for LiveData--------------------------

        binding.lifecycleOwner = this

// -------------------------------------------------------------------------------------------------
// Setting up Recyclerview in Gridlayout------------------------------------------------------------

        val managerNotes = GridLayoutManager(
            activity,
            mainFragmentViewModel.numberOfColumns,
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewNotes.layoutManager = managerNotes

        val managerTitles = GridLayoutManager(
            activity,
            mainFragmentViewModel.numberOfColumns,
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewTitles.layoutManager = managerTitles

// -------------------------------------------------------------------------------------------------
// Instance of adapter that handles click on listItem and submit recyclerviews list-----------------

        // RecyclerView of Notes
        val adapterNotes = NoteEntryAdapterNotes(NoteEntryNotesListener { noteId ->
            mainFragmentViewModel.onNoteClicked(noteId)
        })

        binding.recyclerViewNotes.adapter = adapterNotes

        // Submitting the whole list of notes for the recyclerview
        mainFragmentViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterNotes.submitList(it)
            }
        })

        // RecyclerView of Titles
        val adapterTitles = NoteEntryAdapterTitles(NoteEntryTitlesListener { noteId ->
            mainFragmentViewModel.onNoteClicked(noteId)
        })

        binding.recyclerViewTitles.adapter = adapterTitles

        // Observe and submit the whole list of titles for the recyclerview
        mainFragmentViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterTitles.submitList(it)
            }
        })

        // On item click
        mainFragmentViewModel.navigateToNewFragment.observe(viewLifecycleOwner, Observer { note ->
            note?.let {
                this.findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToNewFragment(note)
                )
            }
        })

        // Button - Exit app
        binding.buttonExit.setOnClickListener {
            activity?.finish()
        }

        // Button New
        binding.buttonAdd.setOnClickListener {

            mainFragmentViewModel.onPressNewNote()

        }

        val scrollListeners = arrayOfNulls<RecyclerView.OnScrollListener>(2)
        scrollListeners[0] = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollListeners[1]?.let { binding.recyclerViewNotes.removeOnScrollListener(it) }
                binding.recyclerViewNotes.scrollBy(dx, dy)
                scrollListeners[1]?.let { binding.recyclerViewNotes.addOnScrollListener(it) }
            }
        }
        scrollListeners[1] = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollListeners[0]?.let { binding.recyclerViewTitles.removeOnScrollListener(it) }
                binding.recyclerViewTitles.scrollBy(dx, dy)
                scrollListeners[0]?.let { binding.recyclerViewTitles.addOnScrollListener(it) }
            }
        }
        scrollListeners[0]?.let { binding.recyclerViewTitles.addOnScrollListener(it) }
        scrollListeners[1]?.let { binding.recyclerViewNotes.addOnScrollListener(it) }

// -------------------------------------------------------------------------------------------------
// New stuff here--------------------------------------------------------------------------------



        binding.recyclerViewTitles.setOnLongClickListener {
                val pop = context?.let { it1 -> PopupMenu(it1, it) }
            if (pop != null) {
                pop.inflate(R.menu.show_menu)
            }

            if (pop != null) {
                pop.setOnMenuItemClickListener { item ->

                    when (item.itemId) {
                        R.id.context_menu_editText -> {

                        }
                        R.id.context_menu_delete -> {

                        }
                    }
                    true
                }
            }
            if (pop != null) {
                pop.show()
            }
                true
            }



// -------------------------------------------------------------------------------------------------
        return binding.root
    }
}

