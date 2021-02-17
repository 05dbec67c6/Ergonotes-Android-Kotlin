package com.ergonotes.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentMainUIControllerBinding

class MainFragmentUIController : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentMainUIControllerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_main_u_i_controller, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = MainFragmentViewModelFactory(dataSource, application)

        val mainFragmentViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(MainFragmentViewModel::class.java)

        //binding.mainFragmentViewModel = mainFragmentViewModel

        binding.lifecycleOwner = this

//--------------------------------------------------------------------------------------------------
        return binding.root
    }
}

