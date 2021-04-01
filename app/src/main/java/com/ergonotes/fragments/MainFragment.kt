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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.ergonotes.R
import com.ergonotes.adapter.NotesAdapter
import com.ergonotes.adapter.TitlesAdapter
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentMainBinding
import com.ergonotes.viewmodelfactories.MainViewModelFactory
import com.ergonotes.viewmodels.MainViewModel
import timber.log.Timber
import java.util.*


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
// Application, arguments, databinding and dataSource for viewModel/Factory-------------------------

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

        var defaultNoteTextSizeSettings = settingsManager
            .getString("defaultNoteTextSize", "45")

        var defaultTitleTextSizeSettings = settingsManager
            .getString("defaultTitleTextSize", "45")

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
            .getInt("defaultBackgroundColor", Color.parseColor("#B4B4B4"))

        val defaultTextColor: Int = sharedPreferences
            .getInt("defaultTextColor", Color.parseColor("#000000"))

        val backgroundColor: Int = sharedPreferences
            .getInt("backgroundColor", Color.parseColor("#DFDFDF"))

        binding.recyclerViewTitles.setBackgroundColor(backgroundColor)
        binding.recyclerViewNotes.setBackgroundColor(backgroundColor)
        binding.mainConstraintLayout.setBackgroundColor(backgroundColor)

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
// new
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

        val adapterNotes = NotesAdapter()
        binding.recyclerViewNotes.adapter = adapterNotes


        val adapterTitles = TitlesAdapter()
        binding.recyclerViewTitles.adapter = adapterTitles

        mainViewModel.allNotesByPosition.observe(viewLifecycleOwner, { note ->
            note?.let {

                adapterNotes.submitList(it)
                adapterTitles.submitList(it)
            }
        })

// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------


        mainViewModel.allNotesByPosition.observe(viewLifecycleOwner, { note ->
            note?.let {

                val simpleItemTouchCallback =
                    object : ItemTouchHelper.SimpleCallback(
                        UP or DOWN or START or END,
                        0
                    ) {
                        private var fromPosition: Int? = null
                        private var toPosition: Int? = null

                        override fun onSwiped(
                            viewHolder: RecyclerView.ViewHolder,
                            direction: Int
                        ) {
                        }

                        override fun onSelectedChanged(
                            viewHolder: RecyclerView.ViewHolder?,
                            actionState: Int
                        ) {
                            super.onSelectedChanged(viewHolder, actionState)

                            if (actionState == ACTION_STATE_DRAG) {
                                viewHolder?.itemView?.alpha = 0.5f
                            }
                        }

                        override fun onMove(
                            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {

                            val from = viewHolder.adapterPosition
                            val to = target.adapterPosition

                            fromPosition = from
                            toPosition = to

                            Collections.swap(it, from, to)

                            Timber.i("onMove")
                            Timber.i("Viewholderpositions: fromPosition: ${fromPosition}  toPosition: $toPosition")

                            Timber.i("NoteEntryPosition: fromPosition: ${it[fromPosition!!].notePosition}")
                            Timber.i("NoteEntryPosition: toPosition: ${it[toPosition!!].notePosition}")

                            if (fromPosition != null && toPosition != null) {
                                mainViewModel.updateNotes(
                                    it[fromPosition!!],
                                    it[toPosition!!],
                                    toPosition!!,
                                    fromPosition!!
                                )
                            }
                            adapterNotes.submitList(it)
                            adapterTitles.submitList(it)
                            return true
                        }

                        override fun clearView(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder
                        ) {
                            super.clearView(recyclerView, viewHolder)
                            viewHolder.itemView.alpha = 1.0f

                            fromPosition = null
                            toPosition = null
                        }
                    }
                ItemTouchHelper(simpleItemTouchCallback as SimpleCallback).attachToRecyclerView(
                    binding.recyclerViewNotes
                )
            }

        })


// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------

// Navigate to newFragment on item click
        mainViewModel.navigateToNewFragment.observe(viewLifecycleOwner,
            { note ->
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
                    scrollListeners[1]?.let {
                        binding.recyclerViewNotes.removeOnScrollListener(
                            it
                        )
                    }
                    binding.recyclerViewNotes.scrollBy(dx, dy)
                    scrollListeners[1]?.let {
                        binding.recyclerViewNotes.addOnScrollListener(
                            it
                        )
                    }
                }
            }
        scrollListeners[1] =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    scrollListeners[0]?.let {
                        binding.recyclerViewTitles.removeOnScrollListener(
                            it
                        )
                    }
                    binding.recyclerViewTitles.scrollBy(dx, dy)
                    scrollListeners[0]?.let {
                        binding.recyclerViewTitles.addOnScrollListener(
                            it
                        )
                    }
                }
            }

        scrollListeners[0]?.let { binding.recyclerViewTitles.addOnScrollListener(it) }
        scrollListeners[1]?.let { binding.recyclerViewNotes.addOnScrollListener(it) }

// -------------------------------------------------------------------------------------------------
// Buttons------------------------------------------------------------------------------------------

// Button - exit app
        binding.buttonExit.setOnClickListener { activity?.finish() }

// Button - new note with default values
        binding.buttonAdd.setOnClickListener {

            mainViewModel.onAddNote(
                noteEntryBackgroundColor = defaultBackgroundColor,
                noteEntryTextColor = defaultTextColor,
                noteEntryNoteSize = defaultNoteTextSize,
                noteEntryTitleSize = defaultTitleTextSize,
            )

            mainViewModel.navigateToNewNote.observe(viewLifecycleOwner, { note ->
                note?.let {
                    this.findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToNewFragment(
                            note.noteId
                        )
                    )
                }
            })
        }

// Button - settings
        binding.buttonSettings.setOnClickListener {
            view?.findNavController()?.navigate(
                MainFragmentDirections.actionMainFragmentToSettingsFragment()
            )
        }
// -------------------------------------------------------------------------------------------------
        return binding.root
    }
}
