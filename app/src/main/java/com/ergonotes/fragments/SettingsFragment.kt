package com.ergonotes.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import com.ergonotes.R
import com.ergonotes.databinding.FragmentSettingsBinding

class SettingsFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// -------------------------------------------------------------------------------------------------
// Databinding, application and args----------------------------------------------------------------

        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )

        val settingsManager: SharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val defaultBackgroundColor: Int =
            sharedPreferences.getInt("defaultBackgroundColor", Color.parseColor("#B4B4B4"))

        binding.colorPreviewDefaultBackgroundColor.setBackgroundColor(defaultBackgroundColor)

        binding.defaultBackgroundColorTextview.setOnClickListener {

            val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(context)

            colorPickerDialog.setLastColor(defaultBackgroundColor)

            colorPickerDialog.show()

            colorPickerDialog.setOnColorPickedListener { color, _ ->

                //  defaultBackgroundColor = color

                binding.colorPreviewDefaultBackgroundColor.setBackgroundColor(color)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putInt("defaultBackgroundColor", color)
                editor.apply()

            }
        }


        val defaultTextColor: Int = sharedPreferences
            .getInt("defaultTextColor", Color.parseColor("#000000"))

        binding.colorPreviewTextColor.setBackgroundColor(defaultTextColor)

        binding.defaultTextColorTextview.setOnClickListener {

            val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(context)

            colorPickerDialog.setLastColor(defaultTextColor)

            colorPickerDialog.show()

            colorPickerDialog.setOnColorPickedListener { color, _ ->

                //  defaultBackgroundColor = color

                binding.colorPreviewTextColor.setBackgroundColor(color)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putInt("defaultTextColor", color)
                editor.apply()

            }
        }


        val backgroundColor: Int = sharedPreferences
            .getInt("backgroundColor", Color.parseColor("#DFDFDF"))

        binding.colorPreviewBackgroundColor.setBackgroundColor(backgroundColor)

        binding.backgroundColorTextview.setOnClickListener {

            val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(context)

            colorPickerDialog.setLastColor(backgroundColor)

            colorPickerDialog.show()

            colorPickerDialog.setOnColorPickedListener { color, _ ->

                //  defaultBackgroundColor = color

                binding.colorPreviewBackgroundColor.setBackgroundColor(color)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putInt("backgroundColor", color)
                editor.apply()

            }
        }

        return binding.root
    }
}


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settings, rootKey)


// -------------------------------------------------------------------------------------------------
// only allow numbers in settings title text sizes--------------------------------------------------

        val numberNoteTextPreference: EditTextPreference? = findPreference("defaultNoteTextSize")

        numberNoteTextPreference?.setOnBindEditTextListener { editText ->

            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setSelection(editText.length())

        }

// -------------------------------------------------------------------------------------------------
// only allow numbers in settings note text sizes---------------------------------------------------

        val numberTitleTextPreference: EditTextPreference? = findPreference("defaultTitleTextSize")

        numberTitleTextPreference?.setOnBindEditTextListener { editText ->

            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setSelection(editText.length())

        }
    }
}


