package tonnysunm.com.acornote.ui.colortag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import tonnysunm.com.acornote.databinding.FragmentColortagsBinding

private val TAG = "ColorTagListFragment"

open class ColorTagListFragment(private val vertical: Boolean = true) : Fragment() {

    val viewModel: ColorTagViewModel by viewModels {
        ColorTagViewModelFactory(
            requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentColortagsBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ColorTagListAdapter()
        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.recyclerview.adapter = adapter

//        binding.editText.setOnEditorActionListener() { v, keyCode, _ ->
//            if (keyCode == EditorInfo.IME_ACTION_DONE) {
//                val textView = v as TextView
////                viewModel.createLabel(textView.text.toString())
//
//                textView.text = null
//                return@setOnEditorActionListener false
//            }
//            false
//        }

        return binding.root

    }

}
