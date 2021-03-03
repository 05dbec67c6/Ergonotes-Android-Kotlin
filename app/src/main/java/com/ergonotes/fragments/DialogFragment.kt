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
import com.ergonotes.databinding.FragmentDialogBinding
import com.ergonotes.viewmodelfactories.DialogFragmentViewModelFactory
import com.ergonotes.viewmodels.DialogFragmentViewModel

class DialogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentDialogBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dialog, container, false
        )
        val application: Application = requireNotNull(this.activity).application
        val arguments = DialogFragmentArgs.fromBundle(requireArguments())
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory = DialogFragmentViewModelFactory(arguments.noteEntryKey, dataSource)
        val dialogFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(DialogFragmentViewModel::class.java)
        binding.dialogFragmentViewModel = dialogFragmentViewModel
        binding.lifecycleOwner = this


        binding.buttonDeleteDialog.setOnClickListener {
            dialogFragmentViewModel.deleteTargetNote()

            findNavController().navigate(DialogFragmentDirections.actionDialogFragmentToMainFragment())

        }
        binding.buttonCancelDialog.setOnClickListener {

            findNavController().navigate(
                DialogFragmentDirections.actionDialogFragmentToNewFragment(arguments.noteEntryKey)
            )
        }
        return binding.root
    }
}
