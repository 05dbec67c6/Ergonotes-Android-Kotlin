package com.ergonotes.fragments

import android.content.Context
import android.graphics.Color
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
import androidx.preference.PreferenceManager
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentNewBinding
import com.ergonotes.viewmodelfactories.NewViewModelFactory
import com.ergonotes.viewmodels.NewViewModel

class NewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// -------------------------------------------------------------------------------------------------
// Databinding, application and args----------------------------------------------------------------

        val binding: FragmentNewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new, container, false
        )

// -------------------------------------------------------------------------------------------------
// Application, arguments, databinding and dataSource for viewModel/Factory-------------------------

        val application = requireNotNull(this.activity).application

        val arguments = NewFragmentArgs.fromBundle(requireArguments())

        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao

        val viewModelFactory =
            NewViewModelFactory(arguments.noteEntryKey, dataSource, application)

        val newViewModel = ViewModelProvider(this, viewModelFactory)
            .get(NewViewModel::class.java)

        binding.newFragmentViewModel = newViewModel

        binding.lifecycleOwner = this

// -------------------------------------------------------------------------------------------------
// Buttons------------------------------------------------------------------------------------------

        // Button - Go to main without saving
        binding.buttonBack.setOnClickListener {
            view?.findNavController()?.navigate(
                NewFragmentDirections.actionNewFragmentToMainFragment()
            )
        }

        // Button - Delete editText and/or whole notes text
        binding.buttonDelete.setOnClickListener {
            when {
                binding.editTextTitle.hasFocus() && binding.editTextTitle.text.toString() != ""
                -> binding.editTextTitle.setText("")

                binding.editTextNote.hasFocus() && binding.editTextNote.text.toString() != ""
                -> binding.editTextNote.setText("")
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

        //Button - Apply and go back
        binding.buttonApplyAndGoToMain.setOnClickListener {

            newViewModel.onSetNote(
                titleString = binding.editTextTitle.text.toString(),
                noteString = binding.editTextNote.text.toString(),

                )
            Toast.makeText(activity, "Note saved", Toast.LENGTH_SHORT).show()

            view?.findNavController()?.navigate(
                NewFragmentDirections.actionNewFragmentToMainFragment()
            )
        }

// -------------------------------------------------------------------------------------------------
// Retrieve background color from Settings----------------------------------------------------------

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val backgroundColor: Int = sharedPreferences
            .getInt("backgroundColor", Color.parseColor("#FFFFC8"))

        binding.constraintLayoutNewFragment.setBackgroundColor(backgroundColor)

// -------------------------------------------------------------------------------------------------
// Set focus, set cursor to end of line and show keyboard-------------------------------------------

        newViewModel.getNote().observe(viewLifecycleOwner, {
            it?.let {

                binding.editTextNote.setText(it.noteEntryNote)
                binding.editTextTitle.setText(it.noteEntryTitle)

                if (binding.editTextTitle.text.toString().isEmpty()) {
                    binding.editTextTitle.requestFocus()
                } else {
                    binding.editTextNote.requestFocus()
                    binding.editTextNote.setSelection(binding.editTextNote.length())
                }
                binding.editTextNote.showKeyboard()
            }
        })
// -------------------------------------------------------------------------------------------------
        return binding.root
    }
}

private fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}