package com.example.prm4.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm4.Navigable
import com.example.prm4.R
import com.example.prm4.adapers.DishImagesAdapter
import com.example.prm4.data.KierowcaDatabase
import com.example.prm4.databinding.InfoBinding
import kotlin.concurrent.thread


class InfoFragment : Fragment() {



    private lateinit var binding: InfoBinding
    private lateinit var adapter: DishImagesAdapter
    var i: Int = 0

    companion object {
        @JvmStatic
        var idaaa: Long = 0
            private set

        var imie: String = ""
            private set

        var osiagniecia: String = ""
            private set

        var icon: Int = 0
            private set

        @JvmStatic
        fun set(value: Long) {
            idaaa = value
        }
        @JvmStatic
        fun setImie(value: String) {
            imie = value
        }
        @JvmStatic
        fun setOsiagniecia(value: String) {
            osiagniecia = value
        }
        @JvmStatic
        fun setIcon(value: Int) {
            icon = value
        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return InfoBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        adapter = DishImagesAdapter()
        binding.photo.apply {
            adapter = this@InfoFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        val xx : String = osiagniecia.substring(1, osiagniecia.length -1)
        binding.editTextText.setText(imie)
        binding.editTextText2.setText(xx)

        binding.buttonEdit.setOnClickListener {
            EditFragment.set(idaaa)
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
        }




    }

}