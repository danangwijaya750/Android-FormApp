package com.dngwjy.formapp.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class RvAdapter<T> (private val data:List<T>,private val listener:(T)->Unit)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewHolder(
            LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false), viewType
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BinderData<T>).bindData(data[position],listener,position)
    }

    override fun getItemViewType(position: Int): Int =layoutId(position,data[position])

    protected abstract fun layoutId(position: Int, obj:T):Int

    abstract fun viewHolder(view: View, viewType: Int):RecyclerView.ViewHolder

    internal interface BinderData<T> {
        fun bindData(data:(T),listen:(T)->Unit,position: Int)
    }
}