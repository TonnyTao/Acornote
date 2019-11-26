package tonnysunm.com.acornote.ui.folder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.databinding.EditFolderFragmentBinding
import tonnysunm.com.acornote.model.EmptyId


class EditFolderFragment : Fragment() {

    private val viewModel: EditFolderViewModel by viewModels {
        val id = arguments?.getLong("folderId")

        EditFolderViewModelFactory(
            requireActivity().application,
            if (id != EmptyId) id else null
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = EditFolderFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.setOnCancel {
            it.findNavController().popBackStack()
        }

        binding.setOnSure { view ->
            view.isEnabled = false

            val title = binding.textView.text.toString()

            binding.progressbar.visibility = View.VISIBLE

            lifecycleScope.launch {
                viewModel.updateOrInsertFolder(title)

                view.findNavController().popBackStack()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}