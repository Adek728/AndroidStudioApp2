package com.example.prm4.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm4.adapers.KierowcaAdapter
import com.example.prm4.Navigable
import com.example.prm4.adapers.DishImagesAdapter
import com.example.prm4.data.KierowcaDatabase
import com.example.prm4.data.model.KierowcaEntity
import com.example.prm4.databinding.FragmentListBinding
import kotlin.concurrent.thread


class ListFragment : Fragment() {



    private lateinit var binding: FragmentListBinding
    private var adapter: KierowcaAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = (activity as? Navigable)?.let { KierowcaAdapter(it) }
        loadData()

        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }


        binding.btAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
            InfoFragment.setImie("")
            InfoFragment.setOsiagniecia("")
            DishImagesAdapter.set(0)
        }

        binding.btSort.setOnClickListener {

            (activity as? Navigable)?.navigate(Navigable.Destination.Photo)
        }




    }


    fun loadData() =  thread {
        val kierowcas = KierowcaDatabase.open(requireContext()).kierowcy.getAll().map { entity ->
            KierowcaEntity(
                entity.id,
                entity.imie,
                entity.osiagniecia.split("\n").toString(),
                entity.icon
            )
        }
        requireActivity().runOnUiThread {
            binding.numberOfElements.setText(kierowcas.size.toString())
        }

        adapter?.replace(kierowcas)

    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

}