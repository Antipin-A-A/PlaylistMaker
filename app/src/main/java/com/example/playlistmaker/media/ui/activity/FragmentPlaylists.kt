package com.example.playlistmaker.media.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.ui.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FragmentPlaylists : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    val viewModel by activityViewModel<PlaylistsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    companion object {
        fun newInstance() = FragmentPlaylists()
    }
}
