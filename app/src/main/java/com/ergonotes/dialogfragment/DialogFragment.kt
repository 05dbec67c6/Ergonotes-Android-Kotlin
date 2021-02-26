package com.ergonotes.dialogfragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ergonotes.R


class FireMissilesDialogFragment : DialogFragment() {



}


//    /** The system calls this to get the DialogFragment's layout, regardless
//    of whether it's being displayed as a dialog or an embedded fragment. */
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout to use as dialog or embedded fragment
//        return inflater.inflate(R.layout.fragment_dialog, container, false)
//    }
//
//
//    /** The system calls this only when creating the layout in a dialog. */
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            // Use the Builder class for convenient dialog construction
//            val builder = AlertDialog.Builder(it)
//
//            val inflater = requireActivity().layoutInflater;
//            builder.setView(inflater.inflate(R.layout.fragment_dialog, null))
//
//
//
//            builder.setMessage(R.string.dialog_fire_missiles)
//                .setPositiveButton(R.string.fire,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // FIRE ZE MISSILES!
//                    })
//                .setNegativeButton(R.string.cancel,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // User cancelled the dialog
//                    })
//            // Create the AlertDialog object and return it
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }




