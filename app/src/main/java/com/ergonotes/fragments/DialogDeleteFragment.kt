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
import com.ergonotes.viewmodelfactories.DialogDeleteFragmentViewModelFactory
import com.ergonotes.viewmodels.DialogDeleteFragmentViewModel

class DialogDeleteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentDialogDeleteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dialog_delete, container, false
        )
        val application: Application = requireNotNull(this.activity).application
        val arguments = DialogDeleteFragmentArgs.fromBundle(requireArguments())
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory =
            DialogDeleteFragmentViewModelFactory(arguments.noteEntryKey, dataSource)
        val dialogFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(DialogDeleteFragmentViewModel::class.java)
        binding.dialogFragmentViewModel = dialogFragmentViewModel
        binding.lifecycleOwner = this


        binding.buttonDeleteDialog.setOnClickListener {

            dialogFragmentViewModel.deleteTargetNote()

            findNavController().navigate(DialogDeleteFragmentDirections
                .actionDialogFragmentToMainFragment())
        }

        binding.buttonCancelDialog.setOnClickListener {

            findNavController().navigate(
                DialogDeleteFragmentDirections.actionDialogFragmentToMainFragment()
            )
        }
        return binding.root
    }
}
