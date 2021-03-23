package com.ergonotes.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.database.NoteEntry
import com.ergonotes.databinding.FragmentEditBinding
import com.ergonotes.viewmodelfactories.EditViewModelFactory
import com.ergonotes.viewmodels.EditViewModel

class EditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// -------------------------------------------------------------------------------------------------
// Databinding--------------------------------------------------------------------------------------

        val binding: FragmentEditBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit, container, false
        )

// -------------------------------------------------------------------------------------------------
// Application, arguments, databinding and dataSource for viewModel/Factory-------------------------

        val application = requireNotNull(this.activity).application

        val arguments = EditFragmentArgs.fromBundle(requireArguments())

        val dataBase = NoteDatabase.getDatabase(application).noteDatabaseDao

        val viewModelFactory = EditViewModelFactory(arguments.noteEntryKey, dataBase)

        val editFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditViewModel::class.java)

        binding.editFragmentViewModel = editFragmentViewModel

        binding.lifecycleOwner = this

// -------------------------------------------------------------------------------------------------
// Listener-----------------------------------------------------------------------------------------

        // TextSize of the notes
        val noteTextSizeListener: LiveData<NoteEntry> = editFragmentViewModel.getNote()
        noteTextSizeListener.observe(viewLifecycleOwner, { note: NoteEntry? ->
            if (note != null) {
                binding.setNoteTextSize.setText(note.noteEntryNoteTextSize.toString())
            }
        })

        // TextSize of the titles
        val titleTextSizeListener: LiveData<NoteEntry> = editFragmentViewModel.getNote()
        titleTextSizeListener.observe(viewLifecycleOwner, { note: NoteEntry? ->
            if (note != null) {
                binding.setTitleTextSize.setText(note.noteEntryTitleTextSize.toString())
            }
        })

        // Convert textSize in SP
        val textSize: LiveData<NoteEntry> = editFragmentViewModel.getNote()
        textSize.observe(viewLifecycleOwner, { note: NoteEntry? ->

            if (note != null) {
                binding.textViewPreviewNote.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    note.noteEntryNoteTextSize
                )

            }
        })

// -------------------------------------------------------------------------------------------------
// Buttons------------------------------------------------------------------------------------------

        // Button - PreviewButton for textSize of note
        binding.buttonChangeNoteTextSize.setOnClickListener {
            val px: Float = binding.setNoteTextSize.text.toString().toFloat()
            val sp = px / resources.displayMetrics.scaledDensity
            binding.textViewPreviewNote.textSize = sp
        }

        // Button - PreviewButton for textSize of note
        binding.buttonChangeTitleTextSize.setOnClickListener {
            val px: Float = binding.setTitleTextSize.text.toString().toFloat()
            val sp = px / resources.displayMetrics.scaledDensity
            binding.textViewPreviewTitle.textSize = sp
        }

        // Button - Change backgroundColor
        binding.buttonChangeBackgroundColor.setOnClickListener {

            // Use colorPickerDialog
            val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(context)

            if (binding.textViewPreviewNote.background is ColorDrawable) {
                val cd = binding.textViewPreviewNote.background as ColorDrawable
                val colorCode = cd.color

                colorPickerDialog.setLastColor(colorCode)
            }
            colorPickerDialog.show()

            colorPickerDialog.setOnColorPickedListener { color, _ ->

                binding.textViewPreviewNote.setBackgroundColor(color)
                binding.textViewPreviewTitle.setBackgroundColor(color)

            }
        }

        // Button - Change textColor
        binding.buttonChangeTextColor.setOnClickListener {

            val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(context)

            colorPickerDialog.setLastColor(binding.textViewPreviewNote.currentTextColor)

            colorPickerDialog.show()

            colorPickerDialog.setOnColorPickedListener { color, _ ->

                binding.textViewPreviewNote.setTextColor(color)

            }
        }

        // Button - Cancel and go back
        binding.buttonCancel.setOnClickListener {
            findNavController().navigate(
                EditFragmentDirections.actionEditFragmentToMainFragment()
            )
        }

        // Button - Apply, toast and go back
        binding.buttonSaveAndGoBack.setOnClickListener {
            if (binding.setNoteTextSize.text.toString() == "" ||
                binding.setTitleTextSize.text.toString() == ""
            ) {

                // Toast message, TextSizeField can't be empty
                Toast.makeText(activity, getString(R.string.enter_value), Toast.LENGTH_SHORT)
                    .show()

            } else {

                // Save changes of backgroundColor
                var backgroundColor = Color.TRANSPARENT
                val background = binding.textViewPreviewNote.background
                if (background is ColorDrawable) backgroundColor = background.color

                // Save changes of textColor
                val text = binding.textViewPreviewNote.currentTextColor
                val textColor: Int = text

                // Write changes to dataBase
                editFragmentViewModel.onSetNote(

                    backgroundColor = backgroundColor,
                    textColor = textColor,
                    noteTextSize = binding.setNoteTextSize.text.toString().toFloat(),
                    titleTextSize = binding.setTitleTextSize.text.toString().toFloat()

                )

                // Navigate to main
                findNavController().navigate(
                    EditFragmentDirections.actionEditFragmentToMainFragment()
                )

                // Toast for succesfully save of changes
                Toast.makeText(activity, getString(R.string.changes_saved), Toast.LENGTH_SHORT)
                    .show()

            }
        }
        return binding.root
    }
}