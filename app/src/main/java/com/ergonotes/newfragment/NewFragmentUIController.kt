package com.ergonotes.newfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentNewBinding

//Todo: SaveState on landscape mode
//Todo: If title is empty, focus it, else, focus note
//Todo: If editText is focused, put cursor at end of line

class NewFragmentUIController : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// -------------------------------------------------------------------------------------------------
// Reference binding object and inflate fragment with arguments-------------------------------------

        val binding: FragmentNewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new, container, false
        )

        val application = requireNotNull(this.activity).application

        // Arguments from MainFragment
        val arguments = NewFragmentUIControllerArgs.fromBundle(requireArguments())

// -------------------------------------------------------------------------------------------------
// Setting up ViewModel and Factory-----------------------------------------------------------------

        // Create an instance of the ViewModel Factory
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao
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

            Toast.makeText(
                context, getString(R.string.toast_message_on_delete),
                Toast.LENGTH_SHORT
            ).show()
        }

        // Button - Delete Text
        binding.buttonDelete.setOnClickListener {
            when {
                (binding.editTextTitle.hasFocus() && binding.editTextTitle.text.toString() != "")
                -> binding.editTextTitle.setText("")

                binding.editTextNote.hasFocus() &&
                        binding.editTextNote.text.toString() != "" ->
                    binding.editTextNote.setText("")
            }
        }

        // Button - Toggle focus between title and note
        binding.buttonToggleFocus.setOnClickListener {
            when {
                binding.editTextTitle.hasFocus() -> binding.editTextNote.requestFocus()
                binding.editTextNote.hasFocus() -> binding.editTextTitle.requestFocus()
                else -> binding.editTextNote.requestFocus()
            }
        }

        // Button - Apply, toast and go back
        binding.buttonApplyAndGoToMain.setOnClickListener {

            newFragmentViewModel.onSetNote(
                binding.editTextTitle.text.toString(),
                binding.editTextNote.text.toString()
            )

            Toast.makeText(
                context,
                getString(R.string.toast_message_on_save),
                Toast.LENGTH_SHORT
            )
                .show()

            view?.findNavController()?.navigate(
                NewFragmentUIControllerDirections
                    .actionNewFragmentUIControllerToMainFragmentUIController()
            )
        }

// -------------------------------------------------------------------------------------------------
// Experimental here, later XML---------------------------------------------------------------------


// -------------------------------------------------------------------------------------------------
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val inputMethodManager = requireActivity()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view.findFocus(), InputMethodManager.SHOW_IMPLICIT)

        super.onViewCreated(view, savedInstanceState)
    }
}

