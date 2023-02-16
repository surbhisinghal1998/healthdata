package com.example.healthcare.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.ModelClas.ApiModel.ActiveandDiscontinue.Medication.Medication
import com.example.healthcare.R
import com.example.healthcare.ViewHolder.AllMedicationViewHolder
import com.example.timeless.SharedPrefrences.Singleton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_all_medication_items.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class AllMedicationAdapter(
    val activeList: ArrayList<Medication>,
    val editActiveMedication: EditActiveMedication
) : RecyclerView.Adapter<AllMedicationViewHolder>() {

    private lateinit var context: Context

    var lastpostion = -1

    var reformattedStr = ""

    var discontinueDate = ""

    var discontinueDateFor = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMedicationViewHolder {

        context = parent.context

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_all_medication_items, parent, false)

        return AllMedicationViewHolder(v)

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: AllMedicationViewHolder, position: Int) {

        if (Singleton.check_screens == "active") {
            holder.itemView.discontinndate.visibility = View.GONE
            holder.itemView.editActiveMedication.visibility = View.VISIBLE
        } else {
            holder.itemView.discontinndate.visibility = View.VISIBLE
            holder.itemView.editActiveMedication.visibility = View.GONE

        }

        if (activeList[position].image.isNullOrEmpty()) {

            if (activeList[position].medicationType == null || activeList[position].medicationType.isEmpty()) {
                Picasso.get()
                    .load(R.drawable.medicine)
                    .into(holder.itemView.active_image)
            } else {
                if (activeList[position].medicationType.equals("Tablet", ignoreCase = true)) {
                    Picasso.get()
                        .load(R.drawable.pills)
                        .into(holder.itemView.active_image)
                } else if (activeList[position].medicationType.equals(
                        "Sublingual Tablet",
                        ignoreCase = true
                    )
                ) {
                    Picasso.get()
                        .load(R.drawable.pills)
                        .into(holder.itemView.active_image)
                } else if (activeList[position].medicationType.equals(
                        "Sublingual Tablet & injection",
                        ignoreCase = true
                    )
                ) {
                    Picasso.get()
                        .load(R.drawable.inj)
                        .into(holder.itemView.active_image)
                } else if (activeList[position].medicationType.equals(
                        "Oral Solution",
                        ignoreCase = true
                    )
                ) {
                    Picasso.get()
                        .load(R.drawable.syrup)
                        .into(holder.itemView.active_image)
                } else if (activeList[position].medicationType.equals(
                        "Injection",
                        ignoreCase = true
                    )
                ) {
                    Picasso.get()
                        .load(R.drawable.inj)
                        .into(holder.itemView.active_image)
                } else if (activeList[position].medicationType.equals("Patch", ignoreCase = true)) {
                    Picasso.get()
                        .load(R.drawable.bandages)
                        .into(holder.itemView.active_image)
                }
            }

        } else {
            Picasso.get()
                .load(activeList[position].image)
                .into(holder.itemView.active_image)
        }

        var nooftablets = ""

        val medNAme = activeList[position].medication

        val strgnehth = activeList[position].dosageSchedule[0].strength

        val unit = activeList[position].dosageSchedule[0].unit

        val tolataltablets = activeList[position].totalTablets

        if (activeList[position].changedSummary.isNullOrEmpty()) {
            nooftablets =
                activeList[position].dosageSchedule[activeList[position].dosageSchedule.size - 1].noOfTablets.toString()
        } else {
            nooftablets =
                activeList[position].changedSummary[activeList[position].changedSummary.size - 1].newDose.toString()
        }

        val medicationType = activeList[position].medicationType

        if (activeList[position].noOfCheck == null) {
            holder.itemView.healthCheck_number.text = "0"
        } else {
            holder.itemView.healthCheck_number.text = activeList[position].noOfCheck.toString()
        }

        if (activeList[position].conditionName.isNullOrEmpty()) {
            holder.itemView.condtions_Tv.text = "No Condition"
        } else {
            holder.itemView.condtions_Tv.text = activeList[position].conditionName
        }


        val duration = activeList[position].duration

        holder.itemView.active_Name.text = "$medNAme  $strgnehth $unit"

        holder.itemView.tablet.text = "$nooftablets $medicationType"

        holder.itemView.active_tablet.text = "Current Balance $tolataltablets"

        holder.itemView.duration_tv.text = duration.toString()

        val startdate = activeList[position].createdAt.substring(0, 10)

        val fromUser = SimpleDateFormat("yyyy-MM-dd")
        val myFormat = SimpleDateFormat("dd/MMM/yyyy")

        try {
            reformattedStr = myFormat.format(fromUser.parse(startdate))

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        holder.itemView.active_date.text = reformattedStr


        if (activeList[position].discontinueDate.isNullOrEmpty()) {
            discontinueDate = "Date"
        } else {
            discontinueDate = activeList[position].discontinueDate
        }

        try {
            discontinueDateFor = myFormat.format(fromUser.parse(discontinueDate))

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        holder.itemView.Discontinue_tv_Date.text = discontinueDateFor

        setAnimation(holder.itemView, position)

        if (position % 2 == 0) {
            holder.itemView.background =
                ContextCompat.getDrawable(context, R.drawable.pink_condtion_item_bg)
        } else {
            holder.itemView.background =
                ContextCompat.getDrawable(context, R.drawable.cream_condition_items_bg)
        }

        holder.itemView.setOnClickListener {

            if (Singleton.check_screens == "active") {

                Singleton._idMed = activeList[position]._id

                Navigation.findNavController(it)
                    .navigate(R.id.action_myAllmedicationsFragment2_to_medicationFragment)
            } else {

                Singleton._idMed = activeList[position]._id

                Navigation.findNavController(it)
                    .navigate(R.id.action_myAllmedicationsFragment2_to_medicationFragment)

            }
        }

        holder.itemView.editActiveMedication.setOnClickListener {
            editActiveMedication.clickEdit(position)
        }

    }

    override fun getItemCount(): Int {
        return activeList.size
    }

    private fun setAnimation(itemView: View, position: Int) {

        if (position > lastpostion) run {

            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.zoom)

            itemView.animation = animation

            lastpostion = position

        }
    }

    interface EditActiveMedication {
        fun clickEdit(position: Int)
    }

}