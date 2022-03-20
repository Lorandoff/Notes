package com.notes.ui.list

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.notes.R
import com.notes.databinding.FragmentNoteListBinding
import com.notes.databinding.ListItemNoteBinding
import com.notes.ui.details.NoteDetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment(
) : Fragment() {
    private var _binding : FragmentNoteListBinding? = null

    private var clicked = false
    private val viewModel : NoteListViewModel by viewModels()

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),
        R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),
        R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),
        R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),
        R.anim.to_bottom_anim) }


    private val recyclerViewAdapter = RecyclerViewAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("fs", "onViewCreated")
        _binding!!.list.adapter = recyclerViewAdapter

        viewModel.getList()
        _binding!!.createNoteButton.setOnClickListener {
            viewModel.onCreateNoteClick()
            launchNoteDetailsFragment()
        }
        recyclerViewAdapter.onItemClickListener = {
            viewModel.deledeNoteFromDatabase(it)
        }
        _binding!!.floatingActionButtonSort.setOnClickListener {
            onSortButtonClicked()
        }
        _binding!!.floatingActionButtonSortAsc?.setOnClickListener {
            viewModel.sortList(1)
        }
        _binding!!.floatingActionButtonSortDesc?.setOnClickListener {
            viewModel.sortList(2)
        }
        viewModel.notes.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    recyclerViewAdapter.setItems(it)
                }
            }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun launchNoteDetailsFragment(){
        requireActivity().supportFragmentManager.beginTransaction(

        )
            .replace(R.id.container, NoteDetailsFragment.newInstance())
            .addToBackStack(null).commit()
    }
    companion object{

    }
    private fun onSortButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O){
            setClickable(clicked)
            }
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked){
            _binding?.floatingActionButtonSortAsc?.startAnimation(fromBottom)
            _binding?.floatingActionButtonSortDesc?.startAnimation(fromBottom)
            _binding?.floatingActionButtonSort?.startAnimation(rotateOpen)
        }else{
            _binding?.floatingActionButtonSortAsc?.startAnimation(toBottom)
            _binding?.floatingActionButtonSortAsc?.startAnimation(toBottom)
            _binding?.floatingActionButtonSort?.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked){
            _binding?.floatingActionButtonSortAsc?.visibility  = View.VISIBLE
            _binding?.floatingActionButtonSortDesc?.visibility = View.VISIBLE
        }else{
            _binding?.floatingActionButtonSortAsc?.visibility = View.INVISIBLE
            _binding?.floatingActionButtonSortDesc?.visibility = View.INVISIBLE
        }
    }
    private fun setClickable(clicked : Boolean){
        if (!clicked){
            _binding?.floatingActionButtonSortAsc?.isClickable= false
            _binding?.floatingActionButtonSortDesc?.isClickable= false
        }else{
            _binding?.floatingActionButtonSortAsc?.isClickable = true
            _binding?.floatingActionButtonSortDesc?.isClickable = true
        }
    }
}
    private class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val items = mutableListOf<NoteListItem>()
        public var onItemClickListener : ((NoteListItem) -> Unit)? = null

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.binding.floatingActionButton.setOnClickListener {
                onItemClickListener?.invoke(items[position])
            }
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size

        fun setItems(
            items: List<NoteListItem>
        ) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

        private class ViewHolder(
            val binding: ListItemNoteBinding
        ) : RecyclerView.ViewHolder(
            binding.root
        ) {

            fun bind(
                note: NoteListItem
            ) = with(binding){
                titleLabel.text = note.title
                contentLabel.text = note.content
            }

        }

    }

