package com.ergonotes.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.R
import com.ergonotes.adapter.NotesAdapter
import com.ergonotes.adapter.TitlesAdapter
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentMainBinding
import com.ergonotes.viewmodelfactories.MainViewModelFactory
import com.ergonotes.viewmodels.MainViewModel

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// -------------------------------------------------------------------------------------------------
// Databinding--------------------------------------------------------------------------------------

        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

// -------------------------------------------------------------------------------------------------
// Application, arguments, and dataSource for viewModel/Factory-------------------------------------

        val application = requireNotNull(this.activity).application

        val dataBase = NoteDatabase.getDatabase(application).noteDatabaseDao

        val viewModelFactory = MainViewModelFactory(dataBase)

        val mainViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)

        binding.mainFragmentViewModel = mainViewModel

        binding.lifecycleOwner = this

// -------------------------------------------------------------------------------------------------
// Load settings------------------------------------------------------------------------------------


        val settingsManager: SharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)

        val rows = settingsManager.getString("rows", "2")
        val columns = settingsManager.getString("columns", "2")
        var defaultNoteTextSizeSettings = settingsManager.getString("defaultNoteTextSize", "45")
        var defaultTitleTextSizeSettings = settingsManager.getString("defaultTitleTextSize", "45")

        if (defaultNoteTextSizeSettings == "") {
            defaultNoteTextSizeSettings = "45"

            Toast.makeText(
                activity,
                "The default text size value  shouldn't be empty." +
                        "Standard size is used until you change it to a real value",
                Toast.LENGTH_LONG
            ).show()
        }

        if (defaultTitleTextSizeSettings == "") {
            defaultTitleTextSizeSettings = "45"

            Toast.makeText(
                activity,
                "Default text sizes shouldn't be empty." +
                        "Standard size is used until you change it to a real value",
                Toast.LENGTH_LONG
            ).show()
        }
        val numberOfRows = rows!!.toInt()
        val numberOfColumns = columns!!.toInt()
        val defaultNoteTextSize = defaultNoteTextSizeSettings.toString().toFloat()
        val defaultTitleTextSize = defaultTitleTextSizeSettings.toString().toFloat()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val defaultBackgroundColor: Int = sharedPreferences
            .getInt("defaultBackgroundColor", Color.parseColor("#FFFFFF"))
        val defaultTextColor: Int = sharedPreferences
            .getInt("defaultTextColor", Color.parseColor("#FFFFFF"))

// -------------------------------------------------------------------------------------------------
// Setting up Recyclerview in Gridlayout with LayoutManager-----------------------------------------

        // For notes
        val layoutManagerNotes = object : GridLayoutManager(
            activity, numberOfRows, HORIZONTAL, false
        ) {
            override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams): Boolean {
                layoutParams.width = width / numberOfColumns
                return true
            }
        }
        binding.recyclerViewNotes.layoutManager = layoutManagerNotes

        // For titles
        val layoutManagerTitles = object : GridLayoutManager(
            activity, numberOfRows, HORIZONTAL, false
        ) {
            override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams): Boolean {
                layoutParams.width = width / numberOfColumns
                return true
            }
        }
        binding.recyclerViewTitles.layoutManager = layoutManagerTitles

// -------------------------------------------------------------------------------------------------
// Instance of adapter that handles click on listItem and submit recyclerviews list-----------------

        // Setting adapter for notes
        val adapterNotes = NotesAdapter()
        binding.recyclerViewNotes.adapter = adapterNotes

        // Submitting the whole list of notes for the recyclerview
        mainViewModel.allNotes.observe(viewLifecycleOwner, {
            it?.let {
                adapterNotes.submitList(it)
            }
        })

        // Setting adapter for titles
        val adapterTitles = TitlesAdapter()
        binding.recyclerViewTitles.adapter = adapterTitles

        // Submitting the whole list of titles for the recyclerview
        mainViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterTitles.submitList(it)
            }
        })

        // Navigate to newFragment on item click
        mainViewModel.navigateToNewFragment.observe(viewLifecycleOwner, { note ->
            note?.let {
                this.findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToNewFragment(note.noteId)
                )
            }
        })

// -------------------------------------------------------------------------------------------------
// Synchronize scrolling----------------------------------------------------------------------------

        val scrollListeners = arrayOfNulls<RecyclerView.OnScrollListener>(2)
        scrollListeners[0] =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    scrollListeners[1]?.let { binding.recyclerViewNotes.removeOnScrollListener(it) }
                    binding.recyclerViewNotes.scrollBy(dx, dy)
                    scrollListeners[1]?.let { binding.recyclerViewNotes.addOnScrollListener(it) }
                }
            }
        scrollListeners[1] =
            object : RecyclerView.OnScrollListener() {
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
// Buttons------------------------------------------------------------------------------------------

        // Button - exit app
        binding.buttonExit.setOnClickListener { activity?.finish() }

        // Button - settings
        binding.buttonSettings.setOnClickListener {
            view?.findNavController()?.navigate(
                MainFragmentDirections.actionMainFragmentToSettingsFragment()
            )
        }

        // Button - new note
        binding.buttonAdd.setOnClickListener {

            mainViewModel.onAddNote(
                noteEntryBackgroundColor = defaultBackgroundColor,
                noteEntryTextColor = defaultTextColor,
                noteEntryNoteSize = defaultNoteTextSize,
                noteEntryTitleSize = defaultTitleTextSize
            )

            mainViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, { note ->
                note?.let {
                    this.findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToNewFragment(
                            note.noteId
                        )
                    )
                }
            })

            mainViewModel.newestNote.value = null
        }

// -------------------------------------------------------------------------------------------------

// -------------------------------------------------------------------------------------------------
        return binding.root
    }
}



