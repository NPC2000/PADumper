package com.dumper.android.ui.console

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dumper.android.databinding.FragmentConsoleBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConsoleFragment : Fragment() {

    private lateinit var consoleBind: FragmentConsoleBinding
    private val vm: ConsoleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        consoleBind = FragmentConsoleBinding.inflate(layoutInflater, container, false)

        vm.console.observe(viewLifecycleOwner) {
            consoleBind.console.text = "$it\n"
            lifecycleScope.launch {
                delay(10)
                consoleBind.scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }

        vm.finishCode.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                if (it == 0) {
                    Toast.makeText(requireContext(), "Dump success!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Dump error!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        consoleBind.copyConsole.setOnClickListener {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("PADumper-Log", consoleBind.console.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "Log Copied!", Toast.LENGTH_SHORT).show()
        }

        return consoleBind.root
    }
}
