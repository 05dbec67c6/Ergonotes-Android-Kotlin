package com.ergonotes.newfragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentNewBinding

class NewFragmentUIController : Fragment() {

    private lateinit var newFragmentViewModel: NewFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentNewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new, container, false
        )

        val application = requireNotNull(this.activity).application

        // Arguments from MainFragment
        val arguments = NewFragmentUIControllerArgs.fromBundle(requireArguments())

// -------------------------------------------------------------------------------------------------
// Setting up ViewModel and Factory-----------------------------------------------------------------

        // Create an instance of the ViewModel Factory
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory = NewFragmentViewModelFactory(arguments.noteEntryKey, dataSource)

        // Associate ViewModel with this Fragment
        val newFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(NewFragmentViewModel::class.java)

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
                NewFragmentUIControllerDirections
                    .actionNewFragmentUIControllerToMainFragmentUIController()
            )
        }

        // Button - Delete editText and/or whole note
        binding.buttonDelete.setOnClickListener {
            when {
                binding.editTextTitle.hasFocus() && binding.editTextTitle.text.toString() != ""
                -> binding.editTextTitle.setText("")

                binding.editTextNote.hasFocus() && binding.editTextNote.text.toString() != ""
                -> binding.editTextNote.setText("")

                binding.editTextTitle.hasFocus() && binding.editTextTitle.text.toString() == ""
                -> {
                    findNavController().navigate(
                        NewFragmentUIControllerDirections
                            .actionNewFragmentUIControllerToFireMissilesDialogFragment(arguments.noteEntryKey)
                    )
                }

                binding.editTextNote.hasFocus() && binding.editTextNote.text.toString() == ""
                -> {
                    findNavController().navigate(
                        NewFragmentUIControllerDirections
                            .actionNewFragmentUIControllerToFireMissilesDialogFragment(arguments.noteEntryKey)

                    )
                }
            }
        }

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

        // Button - Apply, toast and go back
        binding.buttonApplyAndGoToMain.setOnClickListener {

            newFragmentViewModel.onSetNote(
                titleString = binding.editTextTitle.text.toString(),
                noteString = binding.editTextNote.text.toString()
            )

            view?.findNavController()?.navigate(
                NewFragmentUIControllerDirections
                    .actionNewFragmentUIControllerToMainFragmentUIController()
            )
        }

// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// Experimental here, later XML---------------------------------------------------------------------



        //automatically show inputmethod
//        val inputMethodManager = requireActivity()
//            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.showSoftInput(view.findFocus(), InputMethodManager.SHOW_IMPLICIT)
//
//        super.onViewCreated(view, savedInstanceState)


        return binding.root
    }


    // -------------------------------------------------------------------------------------------------
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    // -------------------------------------------------------------------------------------------------
    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
    }
}


