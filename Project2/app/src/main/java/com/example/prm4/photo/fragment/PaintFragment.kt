package com.example.prm4.photo.fragment

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.prm4.adapers.KierowcaAdapter
import com.example.prm4.databinding.FragmentPaintBinding
import com.example.prm4.fragments.EditFragment
import com.example.prm4.photo.Model.Settings

class PaintFragment : Fragment() {

    private lateinit var binding: FragmentPaintBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null

    private val onTakePhoto: (Boolean) -> Unit = { photography: Boolean ->
        if(photography){
            loadBitmap()
        } else {
            imageUri?.let{
                requireContext().contentResolver.delete(it, null, null)
            }
        }
    }

    private fun loadBitmap(){
        val imageUri = imageUri ?: return
        requireContext().contentResolver
            .openInputStream(imageUri)
            ?.use {
                BitmapFactory.decodeStream(it)
            }?.let{
                binding.paintView.background = it
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            onTakePhoto
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPaintBinding.inflate(
            inflater, container, false
        ).also {
            binding = it
        }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createPicture()
    }


    private fun createPicture(){
        val imagesUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        imageUri = requireContext().contentResolver.insert(imagesUri, ct)
        cameraLauncher.launch(imageUri)

    }

    fun setSettings(settings: Settings) {
        binding.paintView.apply {
            color = settings.color
            size = settings.size
        }
    }

    fun save() {

        val imageUri = imageUri ?: return //TODO: createPicture()
        val bmp = binding.paintView.generateBitMap()
        imageUri?.let { KierowcaAdapter.add(it) }
        EditFragment.setPhoto(KierowcaAdapter.getMapUri()[KierowcaAdapter.getSize()])
        requireContext().contentResolver.openOutputStream(imageUri)?.use{
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, it)
        }

    }
}