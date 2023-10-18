package com.practicum.playlistmaker.ui.media.fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.ui.media.model.ViewModelNewPlaylistState
import com.practicum.playlistmaker.ui.media.view_model.NewPlaylistViewModel
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewPlaylistViewModel by viewModel()

    private var coverUri: Uri? = null
    private var titleTextWatcher: TextWatcher? = null
    private var descriptionTextWatcher: TextWatcher? = null
    private var titleInputText = ""
    private var descriptionInputText = ""
    private var imagePrivateStorageUri = ""
    private lateinit var confirmDialog: MaterialAlertDialogBuilder



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TITLE_TEXT, titleInputText)
        outState.putString(DESCRIPTION_TEXT, descriptionInputText)
        outState.putString(IMAGE_COVER, coverUri?.let { coverUri.toString() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        titleTextWatcher?.let { binding.name.removeTextChangedListener(it) }
        descriptionTextWatcher?.let { binding.description.removeTextChangedListener(it) }
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            titleInputText = savedInstanceState.getString(TITLE_TEXT, "")
            descriptionInputText = savedInstanceState.getString(DESCRIPTION_TEXT, "")
            val uriStr = savedInstanceState.getString(IMAGE_COVER, "")
            if (uriStr != "") {
                coverUri = Uri.parse(uriStr)
            }
        }

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        viewModel.observeState().observe(this.viewLifecycleOwner) { newPlaylistState ->
            when(newPlaylistState){
                ViewModelNewPlaylistState.SaveError -> {
                    Toast.makeText(requireContext(), R.string.retry, Toast.LENGTH_LONG).show()
                }
                ViewModelNewPlaylistState.SaveSuccess -> {
                    Toast.makeText(requireContext(), getString(R.string.playlist_saved_success, titleInputText), Toast.LENGTH_LONG).show()
                    navigateBack()
                }
                is ViewModelNewPlaylistState.ImageSaved -> {
                    imagePrivateStorageUri = newPlaylistState.uri
                    viewModel.savePlaylist(titleInputText, descriptionInputText, imagePrivateStorageUri)
                }
            }
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                coverUri = uri
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.playlist_holder)
                    .transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelSize(R.dimen.roundCornerPlayerArtwork)))
                    .into(binding.imageViewCover)
            }
        }

        binding.imageViewCover.setOnClickListener {

            val requester = PermissionRequester.instance()


            lifecycleScope.launch {
                if (Build.VERSION.SDK_INT >= 33) {
                    requester.request(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    requester.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                        is PermissionResult.Denied.NeedsRationale -> {
                            Toast.makeText(requireContext(), R.string.rationale_permission, Toast.LENGTH_LONG).show()
                        }
                        is PermissionResult.Denied.DeniedPermanently -> {
                            Toast.makeText(requireContext(), R.string.denied_permanently_permission, Toast.LENGTH_LONG).show()
                        }
                        is PermissionResult.Cancelled -> {
                            return@collect
                        }
                    }
                }
            }


        }

        setDescriptionTextWatcher()
        setTitleTextWatcher()
        setBackNavigation()
        setCreateBtn()

    }

    private fun setCreateBtn(){
        binding.btnCreatePlaylist.setOnClickListener {
            if(titleInputText.isNotEmpty()){
                if(coverUri != null){
                    viewModel.saveImageToPrivateStorage(coverUri!!, getString(R.string.playlists_folder_name), getString(R.string.playlist_cover_filename_part))
                } else {
                    viewModel.savePlaylist(titleInputText, descriptionInputText, imagePrivateStorageUri)
                }
            }
        }
    }

    private fun setCreateBtnEditState(){
        binding.btnCreatePlaylist.isEnabled = titleInputText.isNotEmpty()
    }

    private fun buildDialog() =
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.playlist_creat_confirm))
            .setMessage(getString(R.string.loss_data))
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
            }.setPositiveButton(getString(R.string.finish)) { dialog, which ->
                navigateBack()
            }

    private fun setBackNavigation() {

        confirmDialog = buildDialog()

        binding.toolbar.setOnClickListener {
            onNavigateBack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onNavigateBack()
            }
        })
    }

    private fun onNavigateBack(){
        if (titleInputText.isEmpty() && descriptionInputText.isEmpty() && coverUri == null) {
            navigateBack()
        } else confirmDialog.show()
    }

    private fun navigateBack(){
        val currentTrackId = arguments?.getString(TRACK_ID)
        if (currentTrackId.isNullOrEmpty()) {
            findNavController().navigateUp()
        } else {
            val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
            startActivity(playerIntent)
            activity?.finish()
        }
    }

    private fun setDescriptionTextWatcher() {
        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                descriptionInputText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                setEditTextColors(binding.txtInpLayoutDesc, descriptionInputText)
            }
        }
        descriptionTextWatcher?.let { binding.description.addTextChangedListener(it) }
    }

    private fun setTitleTextWatcher() {
        titleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                titleInputText = s.toString()
                setCreateBtnEditState()
            }

            override fun afterTextChanged(s: Editable?) {
                setEditTextColors(binding.txtInpLayoutName, titleInputText)
            }
        }
        titleTextWatcher?.let { binding.name.addTextChangedListener(it) }
    }

    private fun setEditTextColors(textInputLayout: TextInputLayout, text: CharSequence?) {
        val textColor = if (text.toString().isEmpty()) {
            ResourcesCompat.getColorStateList(resources, R.color.input_text_selector_color_init, requireContext().theme)
        } else {
            ResourcesCompat.getColorStateList(resources, R.color.input_text_selector_color, requireContext().theme)
        }

        if (textColor != null) {
            textInputLayout.setBoxStrokeColorStateList(textColor)
            textInputLayout.hintTextColor = textColor
            textInputLayout.defaultHintTextColor = textColor
        }
    }

    companion object {
        fun newInstance() = NewPlaylistFragment()
        const val TRACK_ID = "TRACK_ID"
        private const val TITLE_TEXT = "TITLE_TEXT"
        private const val DESCRIPTION_TEXT = "DESCRIPTION_TEXT"
        private const val IMAGE_COVER = "IMAGE_COVER"
    }

}