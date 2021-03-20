package com.ergonotes.fragments

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentNewBinding
import com.ergonotes.viewmodelfactories.NewViewModelFactory
import com.ergonotes.viewmodels.NewViewModel
import kotlinx.android.synthetic.main.fragment_new.*

class NewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentNewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new, container, false
        )

        val application = requireNotNull(this.activity).application

        // Arguments from MainFragment
        val arguments = NewFragmentArgs.fromBundle(requireArguments())

// -------------------------------------------------------------------------------------------------
// Setting up ViewModel and Factory-----------------------------------------------------------------

        // Create an instance of the ViewModel Factory
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory =
            NewViewModelFactory(arguments.noteEntryKey, dataSource, application)

        // Associate ViewModel with this Fragment
        val newFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(NewViewModel::class.java)

        // Use View Model with data binding
        binding.newFragmentViewModel = newFragmentViewModel

// -------------------------------------------------------------------------------------------------
// Setting current activity as lifecycle owner of the binding for LiveData--------------------------

        binding.lifecycleOwner = this

// -------------------------------------------------------------------------------------------------
// Buttons------------------------------------------------------------------------------------------

        // Button - Go to main without saving
        binding.buttonBack.setOnClickListener {
            view?.findNavController()?.navigate(
                NewFragmentDirections.actionNewFragmentToMainFragment()
            )
        }

        // Button - Delete editText and/or whole note
        binding.buttonDelete.setOnClickListener {
            when {
                binding.editTextTitle.hasFocus() && binding.editTextTitle.text.toString() != ""
                -> binding.editTextTitle.setText("")

                binding.editTextNote.hasFocus() && binding.editTextNote.text.toString() != ""
                -> binding.editTextNote.setText("")
            }
        }

        newFragmentViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    NewFragmentDirections.actionNewFragmentToMainFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
            }
        })

        // Button - Toggle focus between title and note
        binding.buttonToggleFocus.setOnClickListener {
            when {
                binding.editTextTitle.hasFocus() -> {
                    binding.editTextNote.requestFocus()
                    binding.editTextNote.setSelection(binding.editTextNote.length())
                }
                binding.editTextNote.hasFocus() -> {
                    binding.editTextTitle.requestFocus()
                    binding.editTextTitle.setSelection(binding.editTextTitle.length())
                }
                else -> binding.editTextNote.requestFocus()
            }
        }

        //Button - Apply, toast and go back
        binding.buttonApplyAndGoToMain.setOnClickListener {

            newFragmentViewModel.onSetNote(
                titleString = binding.editTextTitle.text.toString(),
                noteString = binding.editTextNote.text.toString(),
            )

            view?.findNavController()?.navigate(
                NewFragmentDirections.actionNewFragmentToMainFragment()
            )
        }
//        newFragmentViewModel.navigateToSleepTracker.observe(
//            viewLifecycleOwner, Observer {
//                if (it == true) { // Observed state is true.
//                    this.findNavController().navigate(
//                        NewFragmentDirections.actionNewFragmentToMainFragment()
//                    )
//                    // Reset state to make sure we only navigate once, even if the device
//                    // has a configuration change.
//                }
//            })

// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// Experimental here, later XML---------------------------------------------------------------------


//        val noteTextSizeListener: LiveData<NoteEntry> = newFragmentViewModel.getNote()
//        noteTextSizeListener.observe(
//            viewLifecycleOwner,
//            Observer<NoteEntry> { note: NoteEntry? ->
//
//                if (note != null) {
//                    binding.editTextNote.setText(note.noteEntryNote)
//                }
//            })


        //automatically show inputmethod
//        val inputMethodManager = requireActivity()
//            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.showSoftInput(view.findFocus(), InputMethodManager.SHOW_IMPLICIT)
//
//        super.onViewCreated(view, savedInstanceState)

        //  val text = newFragmentViewModel.getNote()

//        val noteTextListener: LiveData<NoteEntry> = newFragmentViewModel.getNote()
//        noteTextListener.observe(
//            viewLifecycleOwner,
//            Observer<NoteEntry> { note: NoteEntry? ->
//
//                if (note != null) {
//                    binding.editTextNote.setText(note.noteEntryNote)
//                }
//            })

        binding.editTextTitle.requestFocus()
        binding.editTextTitle.setSelection(binding.editTextTitle.length())


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val backgroundColor: Int = sharedPreferences
            .getInt("backgroundColor", Color.parseColor("#FFFFC8"))

        binding.constraintLayoutNewFragment.setBackgroundColor(backgroundColor)

binding.editTextTitle.isFocusable=true
        binding.editTextTitle.isFocusableInTouchMode=true
        binding.editTextTitle.requestFocus()





        return binding.root
    }
}




//editText_note.setSelection(editText_note.length())

//binding
//        if (binding != null) {
//            binding.editTextNote.requestFocus()
//        }
//        if (binding != null) {
//            binding.
//        }
//    }




