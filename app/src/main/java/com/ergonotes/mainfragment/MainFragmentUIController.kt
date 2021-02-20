package com.ergonotes.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentMainBinding

class MainFragmentUIController : Fragment() {

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
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao
        val viewModelFactory = MainFragmentViewModelFactory(dataSource, application)

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

        val manager = GridLayoutManager(activity, 3)
        binding.recyclerViewItems.layoutManager = manager

// -------------------------------------------------------------------------------------------------
// Instance of adapter that handles click on listitem and submit recyclerviews list-----------------

        val adapter = NoteEntryAdapter(NoteEntryListener { noteId ->
            mainFragmentViewModel.onNoteEntryClicked(noteId)
        })

        binding.recyclerViewItems.adapter = adapter

        // Submitting the whole list for the recyclerview
        mainFragmentViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

//--------------------------------------------------------------------------------------------------
// Experimental here later for XML------------------------------------------------------------------

        binding.buttonAdd.setOnClickListener { mainFragmentViewModel.onAdd() }
        binding.buttonClear.setOnClickListener { mainFragmentViewModel.onClear() }

        mainFragmentViewModel.navigateToNewFragment.observe(viewLifecycleOwner, Observer { note ->
            note?.let {
                this.findNavController().navigate(
                    MainFragmentUIControllerDirections
                        .actionMainFragmentUIControllerToNewFragmentUIController(note)


                )
            }
        })

// -------------------------------------------------------------------------------------------------
// Experimental here, later XML---------------------------------------------------------------------





// -------------------------------------------------------------------------------------------------

        return binding.root
    }
}

