package com.example.healthcare.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.ModelClas.ApiModel.ActiveandDiscontinue.HealthCehck.HealthCheck
import com.example.healthcare.R
import com.example.healthcare.ViewHolder.AllHealthChecksViewHolder
import com.example.timeless.SharedPrefrences.Singleton
import kotlinx.android.synthetic.main.item_health_check_viewall.view.*

class AllHealthChecksAdapter(val list:ArrayList<HealthCheck>):RecyclerView.Adapter<AllHealthChecksViewHolder>() {


    var lastpostion =-1

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllHealthChecksViewHolder {

        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_health_check_viewall,parent,false)

        return AllHealthChecksViewHolder(v)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllHealthChecksViewHolder, position: Int) {

        if (list[position].description.isEmpty()){
            holder.itemView.active_discription.visibility = View.GONE
        }
        else{
            holder.itemView.active_discription.text = list[position].description
        }

        holder.itemView.active_heading.text = list[position].checkUpName

        holder.itemView.setOnClickListener {

            Singleton.ActiveCheckupName = list[position].checkUpName

            Singleton.Activeconition = list[position].conditionName

            if (list[position].frequency==null||list[position].frequency.isEmpty()){
                Singleton.ActiveFrequency = "0"
                Singleton.ActiveFrequencyType = "0"
            }
            else{
                Singleton.ActiveFrequency = list[position].frequency
                Singleton.ActiveFrequencyType = list[position].frequencyType
            }

            if (list[position].pastChecks.isNullOrEmpty()){
                Singleton.ActiveStartDate = ""

            }
            else{
                Singleton.ActiveStartDate = list[position].pastChecks[0].date

                //Range Reading
                Singleton.recommendedUpperReading = list[position].pastChecks[0].recommendedUpperReading
                Singleton.recommendedLowerReading = list[position].pastChecks[0].recommendedLowerReading

                //Normal Reading
                Singleton.recommendedReading = list[position].pastChecks[0].recommendedReading

                //Bloog pressure Reading
                Singleton.DiastolicCurrentUpperReading = list[position].pastChecks[0].diastolicBP_RecommendedUpperReading
                Singleton.DiastolicCurrentLowerReading = list[position].pastChecks[0].diastolicBP_RecommendedLowerReading
                Singleton.SystolicCurrentUpperReading = list[position].pastChecks[0].systolicBP_RecommendedUpperReading
                Singleton.SystolicCurrentLowerReading = list[position].pastChecks[0].systolicBP_RecommendedLowerReading

                //BMI Range
                Singleton.recommendedHeight = list[position].pastChecks[0].recommendedHeight
                Singleton.recommendedWeight = list[position].pastChecks[0].recommendedWeight
            }


            if (list[position].discontinueDate==null){
                Singleton.ActiveDiscontinueDate = ""

            }
            else{
                Singleton.ActiveDiscontinueDate = list[position].discontinueDate.toString()

            }


            Singleton.ActivecMedicineNAme = list[position].medicationName

            Singleton.ActiveDiscription = list[position].description

            Singleton.ActiveParametr = list[position].parameterType


            Singleton.Active_id = list[position]._id


            Navigation.findNavController(it).navigate(R.id.action_allHealthChecksFragment_to_allHealthCheckFragment)

        }

        setAnimation(holder.itemView, position)

    }

    private fun setAnimation(itemView: View, position: Int) {

        if (position > lastpostion)  {

            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.zoom)

            itemView.animation = animation

            lastpostion = position

        }
    }
}