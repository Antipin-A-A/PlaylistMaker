package com.example.playlistmaker.settings.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetting()
    }

    private fun initSetting() = with(binding) {

        viewModel.state.observe(viewLifecycleOwner) {
            switchButton.isChecked = it
        }

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
        buttonSupport.setOnClickListener {
            viewModel.sendSupport()
        }

        buttonArrow.setOnClickListener {
            viewModel.openTerms()
        }

        buttonShare.setOnClickListener {
            viewModel.shareAppLink()
        }
    }

}
