package com.ergonotes.newfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentNewBinding

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
// Focus an editText and show softinput ------------------------------------------------------------

        //Todo: 1. If title is empty, focus it, else, focus note
        //Todo: 2. If editText is focused, put cursor at end of line
        //Todo: 3. Open softInput

/*        if (binding.editTextNote.requestFocus()) {
            (requireActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
        binding.editTextNote.requestFocus()*/
        // hideKeyboard(activity as Activity)

// -------------------------------------------------------------------------------------------------
// Buttons here, later XML----------------------------------------------------------------------

        //Todo: Bind Buttons to XML
        binding.buttonToMain.setOnClickListener {
            val action = NewFragmentUIControllerDirections
                .actionNewFragmentUIControllerToMainFragmentUIController()
            view?.findNavController()?.navigate(action)
        }

        binding.buttonApply.setOnClickListener {
            newFragmentViewModel.onSetNote(
                binding.editTextTitle.text.toString(),
                binding.editTextNote.text.toString()
            )
        }

// -------------------------------------------------------------------------------------------------
// Experimental here, later XML---------------------------------------------------------------------





// -------------------------------------------------------------------------------------------------
        return binding.root
    }
}

