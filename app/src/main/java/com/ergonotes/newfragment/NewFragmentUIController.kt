package com.ergonotes.newfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentNewBinding
import com.ergonotes.mainfragment.MainFragmentViewModel
import com.ergonotes.mainfragment.MainFragmentViewModelFactory

class NewFragmentUIController : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentNewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new, container, false
        )

        val application = requireNotNull(this.activity).application

        // Instance of ViewModelFactory
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao
       //val viewModelFactory = NewFragmentViewModelFactory()

        //dataSource, application

       /* val newFragmentViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(NewFragmentViewModel::class.java)*/

        // Use View Model with data binding
        //binding.newFragmentViewModel = newFragmentViewModel

        binding.buttonToMain.setOnClickListener {
            val action = NewFragmentUIControllerDirections
                .actionNewFragmentUIControllerToMainFragmentUIController()
            view?.findNavController()?.navigate(action)
        }

        binding.lifecycleOwner = this

        return binding.root
    }
}