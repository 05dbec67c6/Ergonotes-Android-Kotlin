package com.ergonotes.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentEditBinding
import com.ergonotes.viewmodelfactories.EditFragmentViewModelFactory
import com.ergonotes.viewmodels.EditFragmentViewModel
import kotlinx.android.synthetic.main.colorpicker.*
import kotlinx.android.synthetic.main.fragment_edit.*


class EditFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEditBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit, container, false
        )

        val application = requireNotNull(this.activity).application

        // Arguments from MainFragment
        val arguments = EditFragmentArgs.fromBundle(requireArguments())

// -------------------------------------------------------------------------------------------------
// Setting up ViewModel and Factory-----------------------------------------------------------------

        // Create an instance of the ViewModel Factory
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory = EditFragmentViewModelFactory(arguments.noteEntryKey, dataSource)

        // Associate ViewModel with this Fragment
        val editFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditFragmentViewModel::class.java)

        // Use View Model with data binding
        binding.editFragmentViewModel = editFragmentViewModel

// -------------------------------------------------------------------------------------------------
// Setting current activity as lifecycle owner of the binding for LiveData--------------------------

        binding.lifecycleOwner = this

// -------------------------------------------------------------------------------------------------
// Buttons------------------------------------------------------------------------------------------

        // Button - Go to main without saving


        // Button - Delete editText and/or whole note


        // Button - Toggle focus between title and note


        // Button - Apply, toast and go back
        binding.buttonSave.setOnClickListener {
            var backgroundColor = Color.TRANSPARENT
            val background = binding.textViewPreviewNote.background
            if (background is ColorDrawable) backgroundColor = background.color

            val text = binding.textViewPreviewNote.currentTextColor
            val textColor: Int = text


            editFragmentViewModel.onSetNote(

                backgroundColor = backgroundColor,
                fontColor = textColor
            )

            findNavController().navigate(
                EditFragmentDirections.actionEditFragmentToMainFragment()
            )
            Toast.makeText(activity, "saved changes", Toast.LENGTH_SHORT).show()
        }

// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// Experimental here, later XML---------------------------------------------------------------------


        //automatically show inputmethod
//        val inputMethodManager = requireActivity()
//            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.showSoftInput(view.findFocus(), InputMethodManager.SHOW_IMPLICIT)
//
//        super.onViewCreated(view, savedInstanceState)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





// TutorialKart ColorPickr
        button_change_background_color.setOnClickListener {
            colorSelector.visibility = View.VISIBLE

            strColor.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
                    if (s.length == 6) {
                        colorA.progress = 255
                        colorR.progress = Integer.parseInt(s.substring(0..1), 16)
                        colorG.progress = Integer.parseInt(s.substring(2..3), 16)
                        colorB.progress = Integer.parseInt(s.substring(4..5), 16)
                    } else if (s.length == 8) {
                        colorA.progress = Integer.parseInt(s.substring(0..1), 16)
                        colorR.progress = Integer.parseInt(s.substring(2..3), 16)
                        colorG.progress = Integer.parseInt(s.substring(4..5), 16)
                        colorB.progress = Integer.parseInt(s.substring(6..7), 16)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {

                }
            })

            colorA.max = 255
            colorA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    val colorStr = getColorString()
                    strColor.setText(colorStr.replace("#", "").toUpperCase())
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                }
            })

            colorR.max = 255
            colorR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    val colorStr = getColorString()
                    strColor.setText(colorStr.replace("#", "").toUpperCase())
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                }
            })

            colorG.max = 255
            colorG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    val colorStr = getColorString()
                    strColor.setText(colorStr.replace("#", "").toUpperCase())
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                }
            })

            colorB.max = 255
            colorB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    val colorStr = getColorString()
                    strColor.setText(colorStr.replace("#", "").toUpperCase())
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                }
            })

            colorCancelBtn.setOnClickListener {
                colorSelector.visibility = View.GONE
            }

            colorOkBtn.setOnClickListener {
                val color: String = getColorString()
                textView_preview_note.setBackgroundColor(Color.parseColor(color))
                textView_preview_title.setBackgroundColor(Color.parseColor(color))
                colorSelector.visibility = View.GONE
            }
        }
//--------------------------------

        button_change_note_text_size.setOnClickListener {
            colorSelector.visibility = View.VISIBLE


//        textView_preview_note.setOnClickListener {
//            colorSelector.visibility = View.VISIBLE
//        }

            strColor.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
                    if (s.length == 6) {
                        colorA.progress = 255
                        colorR.progress = Integer.parseInt(s.substring(0..1), 16)
                        colorG.progress = Integer.parseInt(s.substring(2..3), 16)
                        colorB.progress = Integer.parseInt(s.substring(4..5), 16)
                    } else if (s.length == 8) {
                        colorA.progress = Integer.parseInt(s.substring(0..1), 16)
                        colorR.progress = Integer.parseInt(s.substring(2..3), 16)
                        colorG.progress = Integer.parseInt(s.substring(4..5), 16)
                        colorB.progress = Integer.parseInt(s.substring(6..7), 16)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {

                }
            })

            colorA.max = 255
            colorA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    val colorStr = getColorString()
                    strColor.setText(colorStr.replace("#", "").toUpperCase())
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                }
            })

            colorR.max = 255
            colorR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    val colorStr = getColorString()
                    strColor.setText(colorStr.replace("#", "").toUpperCase())
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                }
            })

            colorG.max = 255
            colorG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    val colorStr = getColorString()
                    strColor.setText(colorStr.replace("#", "").toUpperCase())
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                }
            })

            colorB.max = 255
            colorB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    val colorStr = getColorString()
                    strColor.setText(colorStr.replace("#", "").toUpperCase())
                    btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
                }
            })

            colorCancelBtn.setOnClickListener {
                colorSelector.visibility = View.GONE
            }

            colorOkBtn.setOnClickListener {
                val color: String = getColorString()
                textView_preview_note.setTextColor(Color.parseColor(color))
                textView_preview_title.setTextColor(Color.parseColor(color))
                colorSelector.visibility = View.GONE
            }
        }
    }


    fun getColorString(): String {
        var a = Integer.toHexString(((255 * colorA.progress) / colorA.max))
        if (a.length == 1) a = "0" + a
        var r = Integer.toHexString(((255 * colorR.progress) / colorR.max))
        if (r.length == 1) r = "0" + r
        var g = Integer.toHexString(((255 * colorG.progress) / colorG.max))
        if (g.length == 1) g = "0" + g
        var b = Integer.toHexString(((255 * colorB.progress) / colorB.max))
        if (b.length == 1) b = "0" + b
        return "#" + a + r + g + b
    }

}



