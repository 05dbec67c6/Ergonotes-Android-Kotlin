package com.ergonotes

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import kotlinx.android.synthetic.main.custom_setting_background_color.view.*
import kotlinx.android.synthetic.main.custom_setting_default_background_color.view.*
import kotlinx.android.synthetic.main.custom_setting_default_text_color.view.*

class DefaultColorBackgroundPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        widgetLayoutResource = R.layout.custom_setting_default_background_color
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {

        super.onBindViewHolder(holder)

        with(holder.itemView) {

            val defaultBackgroundColor: Int = sharedPreferences
                .getInt("defaultBackgroundColor", Color.parseColor("#B4B4B4"))

            color_preview_default_background_color.setBackgroundColor(defaultBackgroundColor)

            default_background_color_textview.setOnClickListener {

                val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(context)

                colorPickerDialog.setLastColor(defaultBackgroundColor)

                colorPickerDialog.show()

                colorPickerDialog.setOnColorPickedListener { color, _ ->

                    //  defaultBackgroundColor = color

                    color_preview_default_background_color.setBackgroundColor(color)

                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putInt("defaultBackgroundColor", color)
                    editor.apply()

                }
            }
        }
    }
}

class DefaultColorTextPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        widgetLayoutResource = R.layout.custom_setting_default_text_color
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {

        super.onBindViewHolder(holder)

        with(holder.itemView) {

            val defaultTextColor: Int = sharedPreferences
                .getInt("defaultTextColor", Color.parseColor("#000000"))

            color_preview_text_color.setBackgroundColor(defaultTextColor)

            default_text_color_textview.setOnClickListener {

                val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(context)

                colorPickerDialog.setLastColor(defaultTextColor)

                colorPickerDialog.show()

                colorPickerDialog.setOnColorPickedListener { color, _ ->

                    //  defaultBackgroundColor = color

                    color_preview_text_color.setBackgroundColor(color)

                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putInt("defaultTextColor", color)
                    editor.apply()

                }
            }
        }
    }
}


class BackgroundColorPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        widgetLayoutResource = R.layout.custom_setting_background_color
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {

        super.onBindViewHolder(holder)

        with(holder.itemView) {

            val backgroundColor: Int = sharedPreferences
                .getInt("backgroundColor", Color.parseColor("#DFDFDF"))

            color_preview_background_color.setBackgroundColor(backgroundColor)

            background_color_textview.setOnClickListener {

                val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(context)

                colorPickerDialog.setLastColor(backgroundColor)

                colorPickerDialog.show()

                colorPickerDialog.setOnColorPickedListener { color, _ ->

                    //  defaultBackgroundColor = color

                    color_preview_background_color.setBackgroundColor(color)

                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putInt("backgroundColor", color)
                    editor.apply()

                }
            }
        }
    }
}