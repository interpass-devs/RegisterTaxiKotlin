package com.thisisnotyours.registertaxikotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.databinding.FragmentCarSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarSearchFragment : Fragment() {
    private lateinit var binding: FragmentCarSearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarSearchBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}