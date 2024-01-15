package com.example.prm4

import androidx.recyclerview.widget.DiffUtil
import com.example.prm4.data.model.KierowcaEntity

class KierowcaCallback(val notSorted: List<KierowcaEntity>, val sorted: List<KierowcaEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = notSorted.size

    override fun getNewListSize(): Int = sorted.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] === sorted[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] == sorted[newItemPosition]
}