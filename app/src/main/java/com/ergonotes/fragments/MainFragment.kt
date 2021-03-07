package com.ergonotes.fragments

import android.content.SharedPreferences
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
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentMainBinding
import com.ergonotes.viewmodelfactories.MainFragmentViewModelFactory
import com.ergonotes.viewmodels.MainFragmentViewModel
import com.ergonotes.views.NotesAdapter
import com.ergonotes.views.TitlesAdapter


class MainFragment : Fragment() {

    private var numberOfRows: Int = 1
    private var numberOfColumns: Int = 1

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
        loadSettings()


        val managerNotes = object : GridLayoutManager(
            activity, numberOfRows, HORIZONTAL, false
        ) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width / numberOfColumns
                return true
            }
        }

        binding.recyclerViewNotes.layoutManager = managerNotes

        val managerTitles = object : GridLayoutManager(
            activity, numberOfRows, HORIZONTAL, false
        ) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width / numberOfColumns
                return true
            }
        }

        binding.recyclerViewTitles.layoutManager = managerTitles

//        binding.recyclerViewTitles.layoutManager = object :
//            GridLayoutManager(activity, nurequireContext()) {
//            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
//                // force height of viewHolder here, this will override layout_height from xml
//                lp.height = height / 2
//                lp.width = 200
//                return true
//            }
//        }

// -------------------------------------------------------------------------------------------------
// Instance of adapter that handles click on listItem and submit recyclerviews list-----------------

        // RecyclerView of Notes
        val adapterNotes = NotesAdapter()

        binding.recyclerViewNotes.adapter = adapterNotes


        // Submitting the whole list of notes for the recyclerview
        mainFragmentViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterNotes.submitList(it)
            }
        })
        fun subscribeUi(adapter: TitlesAdapter) {
            mainFragmentViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
                adapter.submitList(notes)
            }
        }
        // RecyclerView of Titles
        val adapterTitles = TitlesAdapter()
        binding.recyclerViewTitles.adapter = adapterTitles
        subscribeUi(adapterTitles)

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


        binding.button.setOnLongClickListener {
            Toast.makeText(activity, "Toast", Toast.LENGTH_SHORT).show()
            true
        }

        binding.buttonSettings.setOnClickListener {
            view?.findNavController()?.navigate(
                MainFragmentDirections.actionMainFragmentToSettingsFragment()
            )

        }

        //val relativeLayout: RelativeLayout = findViewById(R.id.relativeLayout)
//        val recyclerViewItem = ConstraintLayout(requireContext())
//
//        val constraint: ConstraintLayout = constraint_layout_note
//
//        val params = ConstraintLayout.LayoutParams(
//            400,
//            ConstraintLayout.LayoutParams.MATCH_PARENT
//        )
//        constraint.layoutParams = params

//


//        val params = ConstraintLayout.LayoutParams(
//            ConstraintLayout.LayoutParams.MATCH_PARENT,
//            ConstraintLayout.LayoutParams.WRAP_CONTENT
//        )
//        params.setMargins(0, 20, 0, 40)
//        params.height = 1
//        params. width = 1
//        constraint_layout_note.layoutParams = params

        //apply the default width and height constraints in code
//        val constraints = ConstraintSet()
//        constraints.clone(parent)
//        constraints.constrainDefaultHeight(view.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
//        constraints.constrainDefaultWidth(view.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
//        constraints.applyTo(parent)


// -------------------------------------------------------------------------------------------------
        return binding.root
    }

    private fun loadSettings() {
        val settingsManager: SharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)

        val rows = settingsManager.getString("rows", "2")
        val columns = settingsManager.getString("columns", "2")

        numberOfRows = rows!!.toInt()
        numberOfColumns = columns!!.toInt()
    }
}



