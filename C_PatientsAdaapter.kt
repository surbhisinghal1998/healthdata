package com.example.healthcare.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.ModelClas.ApiModel.subscriptionPitientsDetal.Data
import com.example.healthcare.R
import com.example.healthcare.ViewHolder.C_PatientsViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.no_of_patinets_items.view.*

class C_PatientsAdaapter(val list : ArrayList<Data>,val clickOnPatient: ClickOnPatient):RecyclerView.Adapter<C_PatientsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): C_PatientsViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.no_of_patinets_items,parent,false)

        return C_PatientsViewHolder(v)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: C_PatientsViewHolder, position: Int) {

        if (list[position].image.isNullOrEmpty()){
            Picasso.get()
                .load(R.drawable.defauld_user_image)
                .into( holder.itemView.active_user_home_dp)
        }
        else{
            Picasso.get()
                .load(list[position].image)
                .resize(0, 300 )
                .into( holder.itemView.active_user_home_dp)

        }

        holder.itemView.active_P_user_name.text = list[position].firstName+" "+list[position].lastName
        holder.itemView.PatienytsLocationTv.text = "Location"+" "+list[position].country

        holder.itemView.setOnClickListener {
            clickOnPatient.onclick(position)
        }

    }

    interface ClickOnPatient{
        fun onclick(position: Int)
    }

}