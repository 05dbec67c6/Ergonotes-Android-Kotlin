package com.ergonotes.fragments

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.ergonotes.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settings, rootKey)

// -------------------------------------------------------------------------------------------------

        val numberNoteTextPreference: EditTextPreference? = findPreference("defaultNoteTextSize")

        numberNoteTextPreference?.setOnBindEditTextListener { editText ->

            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setSelection(editText.length())

        }

// -------------------------------------------------------------------------------------------------

        val numberTitleTextPreference: EditTextPreference? = findPreference("defaultTitleTextSize")

        numberTitleTextPreference?.setOnBindEditTextListener { editText ->

            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setSelection(editText.length())

        }
    }
}


