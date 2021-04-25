package com.ergonotes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.ergonotes.R
import com.ergonotes.databinding.FragmentDialogDeleteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

class DialogDeleteFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_delete, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupView(view)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    companion object {
        const val TAG = "SimpleDialog"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(noteEntryKey: Long): DialogDeleteFragment {
            val args = Bundle()
            args.putLong("NoteEntryKey", noteEntryKey)
            val fragment = DialogDeleteFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private fun setupClickListeners(view: View) {

        val binding = FragmentDialogDeleteBinding.bind(view)

        binding.buttonDeleteDialog.setOnClickListener {

            val args = arguments?.get("NoteEntryKey")

            Timber.i("Timber: !!!!!!!!!!arguments: $arguments")

            dismiss()
        }


        binding.buttonCancelDialog.setOnClickListener {
            dismiss()
        }

    }

//    fun setupView(view: View) {
//        val binding = FragmentDialogDeleteBinding.bind(view)
//        binding.XX.text = arguments?.getString(KEY_TITLE)
//        binding.XX.text = arguments?.getString(KEY_SUBTITLE)
//    }

}

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
//
//
//        return super.onCreateDialog(savedInstanceState)
//    =
//        AlertDialog.Builder(requireContext())
//            .setMessage("smoke dope")
//            .setPositiveButton("doper popoe") { _,_ ->
//
//                Toast.makeText(
//                    activity,
//                    getString(R.string.edit_fragment_toast_enter_value_note_text_size_on_preview),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//            .create()
//
//    companion object {
//        const val TAG = "PurchaseConfirmationDialog"
//    }
//
//
//
//
//
//

//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//// -------------------------------------------------------------------------------------------------
//// DataBinding--------------------------------------------------------------------------------------
//
//        val binding: FragmentDialogDeleteBinding = DataBindingUtil.inflate(
//            inflater, R.layout.fragment_dialog_delete, container, false
//        )
//
//// -------------------------------------------------------------------------------------------------
//// Application, arguments, binding and dataSource for viewModel/factory-----------------------------
//
//        val application: Application = requireNotNull(this.activity).application
//
//        val arguments = DialogDeleteFragmentArgs.fromBundle(requireArguments())
//
//        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
//
//        val viewModelFactory =
//            DialogDeleteViewModelFactory(arguments.noteEntryKey, dataSource)
//
//        val dialogFragmentViewModel = ViewModelProvider(this, viewModelFactory)
//            .get(DialogDeleteViewModel::class.java)
//
//        binding.dialogDeleteViewModel = dialogFragmentViewModel
//
//        binding.lifecycleOwner = this
//
//// -------------------------------------------------------------------------------------------------
//// Buttons------------------------------------------------------------------------------------------
//
//        // Confirm deletion and go to main
//        binding.buttonDeleteDialog.setOnClickListener {
//
//           dialogFragmentViewModel.deleteNote()
//
//            //getActivity()?.onBackPressed();
//
//
//
//        }
//
//        // Cancel and go back
//        binding.buttonCancelDialog.setOnClickListener {
//            this.findNavController().navigate(
//                DialogDeleteFragmentDirections.actionDialogFragmentToMainFragment()
//            )
//        }
//
//// -------------------------------------------------------------------------------------------------
//        return binding.root
//    }
//}
