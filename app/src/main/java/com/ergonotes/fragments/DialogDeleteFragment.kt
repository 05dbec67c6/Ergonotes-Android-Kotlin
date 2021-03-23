package com.ergonotes.fragments

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
import com.ergonotes.databinding.FragmentDialogDeleteBinding
import com.ergonotes.viewmodelfactories.DialogDeleteViewModelFactory
import com.ergonotes.viewmodels.DialogDeleteViewModel

class DialogDeleteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// -------------------------------------------------------------------------------------------------
// DataBinding--------------------------------------------------------------------------------------

        val binding: FragmentDialogDeleteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dialog_delete, container, false
        )

// -------------------------------------------------------------------------------------------------
// Application, arguments, binding and dataSource for viewModel/factory-----------------------------

        val application: Application = requireNotNull(this.activity).application

        val arguments = DialogDeleteFragmentArgs.fromBundle(requireArguments())

        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao

        val viewModelFactory =
            DialogDeleteViewModelFactory(arguments.noteEntryKey, dataSource)

        val dialogFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(DialogDeleteViewModel::class.java)

        binding.dialogDeleteViewModel = dialogFragmentViewModel

        binding.lifecycleOwner = this

// -------------------------------------------------------------------------------------------------
// Buttons------------------------------------------------------------------------------------------

        // Yes to deletion / and go to main
        binding.buttonDeleteDialog.setOnClickListener {
            dialogFragmentViewModel.deleteTargetNote()
            findNavController().navigate(
                DialogDeleteFragmentDirections
                    .actionDialogFragmentToMainFragment()
            )
        }

        // Cancel and go back
        binding.buttonCancelDialog.setOnClickListener {
            findNavController().navigate(
                DialogDeleteFragmentDirections.actionDialogFragmentToMainFragment()
            )
        }
        return binding.root
    }
}
