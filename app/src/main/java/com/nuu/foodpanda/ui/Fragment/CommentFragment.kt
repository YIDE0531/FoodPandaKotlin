package com.nuu.foodpanda.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nuu.foodpanda.apibean.ItemComment
import com.nuu.foodpanda.databinding.FragmentCommentBinding
import com.nuu.foodpanda.ui.adapter.CommentAdapter

class CommentFragment : Fragment() {
    private lateinit var binding: FragmentCommentBinding

    companion object{
        private var commentFragment: CommentFragment? = null
        fun newInstance(): CommentFragment? {
            if(commentFragment == null){
                commentFragment = CommentFragment()
            }
            return commentFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBinding.inflate(layoutInflater, container, false)
        val manager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = manager

        var addData = arguments?.getSerializable("shopData") as List<ItemComment>
        addData = if(addData.isEmpty()){
            var params = ItemComment("", "", "")
            addData + params
        }else{
            addData + addData[0]
        }

        val commentAdapter = CommentAdapter(
            requireContext(),
            addData
        )
        binding.recyclerView.adapter = commentAdapter
        return binding.root
    }
}