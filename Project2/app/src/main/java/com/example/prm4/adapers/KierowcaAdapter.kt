package com.example.prm4.adapers

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.prm4.KierowcaCallback
import com.example.prm4.Navigable
import com.example.prm4.R
import com.example.prm4.data.KierowcaDatabase
import com.example.prm4.data.model.KierowcaEntity
import com.example.prm4.databinding.ListItenBinding
import com.example.prm4.fragments.EditFragment
import com.example.prm4.fragments.InfoFragment
import com.example.prm4.fragments.ListFragment
import kotlin.concurrent.thread

class KierowcaViewHolder(val binding: ListItenBinding)
    : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceType")
    fun bind(kierowca: KierowcaEntity) {
        binding.name.text = kierowca.imie
        binding.ingridients.text = kierowca.osiagniecia
        binding.imageView.setImageURI(KierowcaAdapter.getMapUri()[kierowca.icon])
    }


}

class KierowcaAdapter(private val navigable: Navigable) : RecyclerView.Adapter<KierowcaViewHolder>() {
    private val data = mutableListOf<KierowcaEntity>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    companion object {
        @JvmStatic
        val map: MutableMap<Int, Uri> = HashMap()

        @JvmStatic
        fun add(value: Uri) {
            val number: Int = (map.size + 1)
            map[number] = Uri.parse(value.toString())
        }

        @JvmStatic
        fun getSize(): Int = map.size
        @JvmStatic
        fun getMapUri():MutableMap<Int, Uri> {
            return map
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KierowcaViewHolder {
        val binding = ListItenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return KierowcaViewHolder(binding).also { vh ->
            binding.root.setOnClickListener{
                DishImagesAdapter.set(data[vh.absoluteAdapterPosition].icon)
                InfoFragment.setIcon(data[vh.absoluteAdapterPosition].icon)
                InfoFragment.set(data[vh.absoluteAdapterPosition].id)
                InfoFragment.setImie(data[vh.absoluteAdapterPosition].imie)
                InfoFragment.setOsiagniecia(data[vh.absoluteAdapterPosition].osiagniecia)
                navigable.navigate(Navigable.Destination.Info)

            }
        }.also { vh ->
            binding.root.setOnLongClickListener {
                AlertDialog.Builder(parent.context)
                    .setTitle("Czy na pewno chcesz usunąć ten element?")
                    .setMessage("Ta operacja jest nieodwracalna.")
                    .setPositiveButton("Usuń") { dialog, _ ->
                        thread {
                            KierowcaDatabase.open(parent.context).kierowcy.removeKierowca(data[vh.absoluteAdapterPosition])
                        }
                        navigable.navigate(Navigable.Destination.Add)
                        navigable.navigate(Navigable.Destination.List)

                        dialog.dismiss()
                    }
                    .setNegativeButton("Anuluj") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

                true
            }

        }
    }


    override fun onBindViewHolder(holder: KierowcaViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun replace(newData: List<KierowcaEntity>) {
        val callback = KierowcaCallback(data, newData)
        data.clear()
        data.addAll(newData)
        val result = DiffUtil.calculateDiff(callback)
        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }

}