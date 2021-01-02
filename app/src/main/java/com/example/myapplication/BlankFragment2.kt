package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.myapplication.databinding.FragmentBlank2Binding

class BlankFragment2 : Fragment() {

    private var _binding: FragmentBlank2Binding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlank2Binding.inflate(inflater, container, false)
        val view = binding.root

        binding.blank2FragmentText.setOnClickListener(::navigateBack)

        return view

    }

    private fun navigateBack(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_blankFragment2_to_blankFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}