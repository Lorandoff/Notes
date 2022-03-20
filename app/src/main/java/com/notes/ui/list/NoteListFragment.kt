package com.notes.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    //@Inject
    //lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel : NoteListViewModel by viewModels()
    //private lateinit var  viewModel : NoteListViewModel
        //Log.d(
      //      "fdsf", "viewModel"
      //  )
        //DependencyManager.noteListViewModel()


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
    //viewModel = ViewModelProvider(this, viewModelFactory)[NoteListViewModel::class.java]

        Log.d("fs", "onViewCreated")
        _binding!!.list.adapter = recyclerViewAdapter
      //  _binding.list.addItemDecoration(
       //     DividerItemDecoration(
      //          requireContext(),
       //         LinearLayout.VERTICAL
       //     )
       // )
        viewModel.getList()
        _binding!!.createNoteButton.setOnClickListener {
            viewModel.onCreateNoteClick()
            launchNoteDetailsFragment()
        }
        recyclerViewAdapter.onItemClickListener = {
            viewModel.deledeNoteFromDatabase(it)
        }
        _binding!!.floatingActionButton2.setOnClickListener {
            viewModel.sortList()
        }
        viewModel.notes.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    recyclerViewAdapter.setItems(it)
                }
            }
        )
       // viewModel.navigateToNoteCreation.observe(
        //    viewLifecycleOwner,
       //     {
                //findImplementationOrThrow<FragmentNavigator>()
                //    .navigateTo(
                     //   NoteDetailsFragment()
                 //   )
          //  launchNoteDetailsFragment()
       //     }
      //  )
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

