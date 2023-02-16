package com.example.healthcare.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.ModelClas.ApiModel.HealthCheck.HealthCheckList.Checks
import com.example.healthcare.R
import com.example.healthcare.ViewHolder.HealthCheckViewHolder
import kotlinx.android.synthetic.main.add_healthcheck_name_list_items.view.*

class AddHealthCheckAdapterList(val listName:ArrayList<Checks>, val healCheckNameInterface: HealCheckNameInterface):RecyclerView.Adapter<HealthCheckViewHolder>() {


    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthCheckViewHolder {

        context = parent.context

        val v = LayoutInflater.from(parent.context).inflate(R.layout.add_healthcheck_name_list_items,parent,false)
        return HealthCheckViewHolder(v)
    }

    override fun onBindViewHolder(holder: HealthCheckViewHolder, position: Int) {

        holder.itemView.healthCheckList_name.text = listName[position].Health_Check

        holder.itemView.setOnClickListener {
            healCheckNameInterface.onclick(position)
        }

    }

    override fun getItemCount(): Int {
        return listName.size
    }

    interface HealCheckNameInterface{

        fun onclick(position: Int)
    }

}