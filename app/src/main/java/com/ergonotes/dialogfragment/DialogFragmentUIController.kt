package com.ergonotes.dialogfragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentDialogBinding

class DialogFragmentUIController : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDialogBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dialog, container, false
        )
        val application: Application = requireNotNull(this.activity).application
        val arguments = DialogFragmentUIControllerArgs.fromBundle(requireArguments())
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory = DialogFragmentViewModelFactory(arguments.noteEntryKey, dataSource)
        val dialogFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(DialogFragmentViewModel::class.java)

        binding.dialogFragmentViewModel = dialogFragmentViewModel

// -------------------------------------------------------------------------------------------------
// Setting current activity as lifecycle owner of the binding for LiveData--------------------------

        binding.lifecycleOwner = this


        binding.button.setOnClickListener {
            dialogFragmentViewModel.deleteTargetNote()

            findNavController().navigate(DialogFragmentUIControllerDirections
                .actionFireMissilesDialogFragmentToMainFragmentUIController())

        }
        binding.button2.setOnClickListener {

            findNavController().navigate(DialogFragmentUIControllerDirections
                    .actionFireMissilesDialogFragmentToNewFragmentUIController(arguments.noteEntryKey)
            )
        }
        // Use View Model with data binding

// -------------------------------------------------------------------------------------------------
// Setting current activity as lifecycle owner of the binding for LiveData--------------------------


        return binding.root
    }
}
