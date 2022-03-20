package com.notes.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.ui.list.NoteListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailsFragment : Fragment(
) {
    private lateinit var _binding : FragmentNoteDetailsBinding
    private val viewModel : NoteListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.toolbar.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        viewModel.finish.observe(viewLifecycleOwner){
            requireActivity().supportFragmentManager.popBackStack()
        }
        _binding.button.setOnClickListener {
            if (_binding.editTextTitle.text.isNotBlank()
                && _binding.editTextContent.text.isNotBlank()) {
                viewModel.insertNotetoDatabase(_binding.editTextTitle.text.toString(),
                    _binding.editTextContent.text.toString()
                )
            }else{
                Snackbar.make(view, "error", Snackbar.LENGTH_LONG)
            }
            }

    }
companion object{
    fun newInstance() : NoteDetailsFragment{
        return NoteDetailsFragment()
    }
}
}