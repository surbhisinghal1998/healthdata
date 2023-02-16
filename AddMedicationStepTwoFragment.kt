package com.example.healthcare.Fragments.PitientFragment.MyMedications

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.example.healthcare.Adapter.DiagonsedByAdapter
import com.example.healthcare.Adapter.SelectConditionAdapter
import com.example.healthcare.ModelClas.ApiModel.Conditions.AddConditions.AddConditionsModel
import com.example.healthcare.ModelClas.ApiModel.Conditions.GetConditionName.ConditionsModel
import com.example.healthcare.ModelClas.ApiModel.Conditions.GetConditionName.Conditionss
import com.example.healthcare.ModelClas.ApiModel.Login.LoginModel
import com.example.healthcare.ModelClas.ApiModel.Recipient.GetRecipients.GetRecipientModel
import com.example.healthcare.ModelClas.ApiModel.Recipient.GetRecipients.Recipient
import com.example.healthcare.Networking.Retrofit.Api.ApiClient
import com.example.healthcare.Networking.Retrofit.Api.ApiInterface
import com.example.healthcare.R
import com.example.timeless.SharedPrefrences.App
import com.example.timeless.SharedPrefrences.CommonUtils
import com.example.timeless.SharedPrefrences.Singleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_medication.view.*
import kotlinx.android.synthetic.main.fragment_add_medication_two.*
import kotlinx.android.synthetic.main.fragment_add_medication_two.view.*
import kotlinx.android.synthetic.main.fragment_create_account.view.*
import kotlinx.android.synthetic.main.fragment_recipients.view.*
import kotlinx.android.synthetic.main.recipients_items.*
import kotlinx.android.synthetic.main.recipients_items.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class AddMedicationStepTwoFragment : Fragment(), SelectConditionAdapter.SelectConditions,
    DiagonsedByAdapter.CondtionDiagonedBy {

    private lateinit var selectConditionAdapter: SelectConditionAdapter

    private lateinit var view1: View

    var cal = Calendar.getInstance()

    var meddicien_name_layout = false

    private var condition_name = ""

    private var reformattedStr = ""

    private var diagnosisDate = ""

    private lateinit var diagnosedBy: String

    private lateinit var hospitalInformation: String

    var contionlist: ArrayList<Conditionss> = ArrayList()

    private lateinit var conditionsModel: ConditionsModel

    private var conditionName: ArrayList<Conditionss> = ArrayList()

    var progressDialog: ProgressDialog? = null

    private lateinit var addConditionsModel: AddConditionsModel

    private lateinit var diagonsedByAdapter: DiagonsedByAdapter

    private lateinit var getRecipientModel: GetRecipientModel

    private var recipientsList: ArrayList<Recipient> = ArrayList()

    private var serchrecipientsList: ArrayList<Recipient> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDateTime: LocalDateTime = LocalDateTime.now()

    var clickdiagoned = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_add_medication_two, container, false)

        DateFormat_()

        checkScreen()

        view1.addCondition_two_Btn.isEnabled = false

        SetRecyclerView()

        Clcik()

        conditionApi()

        SearchConditionName()

        diagnosedByApi()

        return view1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun DateFormat_() {

        val fromUser = SimpleDateFormat("yyyy-MM-dd")
        val myFormat = SimpleDateFormat("yyyy-MM-dd")

        try {

            reformattedStr = myFormat.format(fromUser.parse(currentDateTime.toString()))
//            Toast.makeText(context, reformattedStr, Toast.LENGTH_SHORT).show()

        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun checkScreen() {

        if (Singleton.check_screens == "AddCondtion") {

            view1.med_dis.visibility = View.GONE

            view1.MedNAme_Title.visibility = View.GONE

            view1.contion_title.text = "Add Condition"

            diagnosisDate = reformattedStr

            view1.addCondition_two_Btn.text = "Submit"

        } else if (Singleton.check_screens == "addMec_step_two") {

            if (Singleton.medicineDis.isNullOrEmpty()){
                view1.med_dis.visibility = View.GONE

            }
            else{
                view1.med_dis.text = Singleton.medicineDis

            }

            diagnosisDate = reformattedStr

            view1.MedNAme_Title.text = Singleton.medname+" "+"is used to treat & manage several conditions. Select the Condition being treated below to get a list of recommended Health Checks"
            view1.contion_title.text = "Step Two: Add Condition"

        } else if (Singleton.check_screens == "editCondition") {

            view1.MedNAme_Title.visibility = View.GONE
            
            view1.med_dis.visibility = View.GONE

            condition_name = Singleton.editConditionName.toString()

            diagnosisDate = Singleton.editConditionDate.toString()

            view1.contion_title.text = "Edit Condition"

            view1.addCondition_two_Btn.text = "Continue"

            view1.addCondition_two_Btn.text = "Save Condition"

            view1.conditions_name.setText(Singleton.editConditionName)
            view1.selectDisg.setText(Singleton.editConditionDiagnosedBy)

//            view1.diagnosis_date_.text = Singleton.editConditionDate

            if (Singleton.editConditionHospitalInfo.isNullOrEmpty()) {
                view1.CondtionHospitalInfo.setText("")
            } else {
                view1.CondtionHospitalInfo.setText(Singleton.editConditionHospitalInfo)
            }
        }

    }

    private fun diagnosedByApi() {

        val token = App.appPreference1.getUserDetails("SaveDetails", LoginModel::class.java).token

        val accessToken = "Bearer $token"

        val apiClient = ApiClient.getRetrofitInstance().create(ApiInterface::class.java)

        apiClient.getRecipient(
            accessToken
        ).enqueue(object : Callback<GetRecipientModel> {
            override fun onResponse(
                call: Call<GetRecipientModel>,
                response: Response<GetRecipientModel>
            ) {


                getRecipientModel = response.body()!!

                if (getRecipientModel.status == 1) {

                    recipientsList = getRecipientModel.recipients as ArrayList<Recipient>

                    recipientsList.reverse()

                    if (recipientsList.size > 7) {
                        view1.DiagonsedBy_name_layout.layoutParams.height = 700
                    } else {
                        view1.DiagonsedBy_name_layout.layoutParams.height =
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    }

                    diagonsedByAdapter =
                        DiagonsedByAdapter(recipientsList, this@AddMedicationStepTwoFragment)

                    view1.DiagonsedBy_name_recycler.adapter = diagonsedByAdapter

//                    Toast.makeText(context,getRecipientModel.message,Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, getRecipientModel.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<GetRecipientModel>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun SearchConditionName() {

        view1.conditions_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                contionlist.clear()

                for (i in 0 until conditionName.size) {
                    if (conditionName[i].Condition_Treated.startsWith(
                            s.toString(),
                            ignoreCase = true
                        )
                    ) {

                        contionlist.add(conditionName[i])

//                       view1.select_condition_recycler.visibility = View.VISIBLE

                    } else {
//                        view1.select_condition_recycler.visibility = View.GONE
                    }
                }
                view1.conditions_name_layout.visibility = View.VISIBLE

                if (contionlist.size > 7) {
                    view1.conditions_name_layout.layoutParams.height = 700
                } else {
                    view1.conditions_name_layout.layoutParams.height =
                        ViewGroup.LayoutParams.WRAP_CONTENT
                }

                selectConditionAdapter =
                    SelectConditionAdapter(contionlist, this@AddMedicationStepTwoFragment)
                view1.select_condition_recycler.adapter = selectConditionAdapter

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


    }


    private fun conditionApi() {

        val token = App.appPreference1.getUserDetails("SaveDetails", LoginModel::class.java).token

        val accessToken = "Bearer $token"

        val apiClient = ApiClient.getRetrofitInstance().create(ApiInterface::class.java)

        val content_type = "application/json"

        val mediaType = MediaType.parse("application/json; charset=utf-8")

        val mySelection = JSONObject()
        try {

            mySelection.put("_id", Singleton.stepOneMed_ID)

            Log.e("myselection", mySelection.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(mediaType, mySelection.toString())

        apiClient.allConditions(
            accessToken, body
        ).enqueue(object : Callback<ConditionsModel> {
            override fun onResponse(
                call: Call<ConditionsModel>,
                response: Response<ConditionsModel>
            ) {


//                progressDialog!!.dismiss()

                conditionsModel = response.body()!!

                if (response.isSuccessful) {
                    if (conditionsModel.status == 1) {

                        conditionName = conditionsModel.conditionssList as ArrayList<Conditionss>

                        contionlist.addAll(conditionName)

                        SetRecyclerView()

                        view1.addCondition_two_Btn.isEnabled = true


                    } else {
                        Toast.makeText(context, conditionsModel.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context, conditionsModel.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ConditionsModel>, t: Throwable) {
//                progressDialog!!.dismiss()
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()

            }

        })


    }


    private fun Clcik() {

        view1.meddicien_name_layout.setOnClickListener {
            if (meddicien_name_layout) {
                view1.conditions_name_layout.visibility = View.VISIBLE
                meddicien_name_layout = false
                view1.conditions_name.isCursorVisible = true
            } else if (!meddicien_name_layout) {
                view1.conditions_name_layout.visibility = View.GONE
                meddicien_name_layout = true
                view1.conditions_name.isCursorVisible = false
            }
        }


        view1.conditionsearchbtn.setOnClickListener {
            if (meddicien_name_layout) {
                view1.conditions_name_layout.visibility = View.VISIBLE
                meddicien_name_layout = false
                view1.conditions_name.isCursorVisible = true
            } else if (!meddicien_name_layout) {
                view1.conditions_name_layout.visibility = View.GONE
                meddicien_name_layout = true
                view1.conditions_name.isCursorVisible = false
            }
        }

        view1.diagnosis_date_.setOnClickListener {
            SetDiagnosis_date()
        }

        view1.addCondition_two_Btn.setOnClickListener {

            Singleton.screens = "AddConditionTwo"

            validations()
//            Navigation.findNavController(view1).navigate(R.id.action_addMedicationFragmentTwoFragment_to_addMedicationStepThreeFragment)
        }

        view1.add_condition_two_back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        view1.deropImagew.setOnClickListener {

            if (!clickdiagoned) {
                view1.DiagonsedBy_name_layout.visibility = View.VISIBLE
                clickdiagoned = true


            } else if (clickdiagoned) {
                view1.DiagonsedBy_name_layout.visibility = View.GONE
                clickdiagoned = false

            }
        }

    }

    private fun validations() {

        if (view1.conditions_name.text.toString().isEmpty()) {
            Toast.makeText(context, "Please Select Condition Name", Toast.LENGTH_SHORT).show()
        } else if (condition_name.isEmpty()) {
            Toast.makeText(context, "Please Select Condition from List", Toast.LENGTH_SHORT).show()
        } else if (view1.conditions_name.text.toString() != condition_name) {
            Toast.makeText(context, "Please Select Condition from List", Toast.LENGTH_SHORT).show()
        }
//        else if (view1.diagnosis_date_.text.toString().isEmpty()) {
//            Toast.makeText(context, "Please Select Diagnosis Date", Toast.LENGTH_SHORT).show()
//        }
//        else if (view1.selectDisg.text.toString().isEmpty()) {
//            CommonUtils.toast(context, "Please Select Diagnosed by")
//        }
        else {
            if (Singleton.check_screens == "AddCondtion" || Singleton.check_screens == "addMec_step_two") {
                if (CommonUtils.isNetworkConnected(requireContext())) {
                    AddConditionApi()
                } else {
                    Snackbar.make(view1, "Check internet Connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
            }
            else {
                if (CommonUtils.isNetworkConnected(requireContext())) {
                    editConditionApi()
                } else {
                    Snackbar.make(view1, "Check internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }
            }

        }
    }

    private fun editConditionApi() {

        ProgresLoader()

        val token = App.appPreference1.getUserDetails("SaveDetails", LoginModel::class.java).token

        val accessToken = "Bearer $token"

        val apiClient = ApiClient.getRetrofitInstance().create(ApiInterface::class.java)

        val mediaType = MediaType.parse("application/json; charset=utf-8")

        val mySelection = JSONObject()
        try {

            mySelection.put("condition", condition_name)
            mySelection.put("_id", Singleton.editConditionId)
            mySelection.put("diagnosisDate", diagnosisDate)
            mySelection.put("DiagnosedBy", view1.selectDisg.text.toString())
            mySelection.put("hospitalInfo", view1.CondtionHospitalInfo.text.toString().trim())

            Log.e("myselection", mySelection.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(mediaType, mySelection.toString())

        apiClient.editCondition(
            accessToken,
            body
        ).enqueue(object : Callback<AddConditionsModel> {
            override fun onResponse(
                call: Call<AddConditionsModel>,
                response: Response<AddConditionsModel>
            ) {
                progressDialog!!.dismiss()

                addConditionsModel = response.body()!!

                if (addConditionsModel.status == 1) {

                    requireActivity().onBackPressed()
                    Toast.makeText(context, addConditionsModel.message, Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(context, addConditionsModel.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddConditionsModel>, t: Throwable) {
                progressDialog!!.dismiss()
                Snackbar.make(view1, t.message.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

        })

    }

    private fun AddConditionApi() {

        hospitalInformation = view1.CondtionHospitalInfo.text.toString()

        ProgresLoader()

        val token = App.appPreference1.getUserDetails("SaveDetails", LoginModel::class.java).token

        val accessToken = "Bearer $token"

        val apiClient = ApiClient.getRetrofitInstance().create(ApiInterface::class.java)

        val content_type = "application/json"

        val mediaType = MediaType.parse("application/json; charset=utf-8")

        val mySelection = JSONObject()
        try {

            mySelection.put("condition", condition_name)
            mySelection.put("diagnosisDate", diagnosisDate)
            mySelection.put("DiagnosedBy", view1.selectDisg.text.toString())
            mySelection.put("hospitalInfo", hospitalInformation)

            if (Singleton.check_screens == "addMec_step_two") {
                mySelection.put("medicationId", Singleton.stepOneMed_ID)
            }
            else{
                mySelection.put("addedDirect", true)
            }

            Log.e("myselection", mySelection.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(mediaType, mySelection.toString())

        apiClient.addUsersConditions(
            accessToken,
            body
        ).enqueue(object : Callback<AddConditionsModel> {
            override fun onResponse(
                call: Call<AddConditionsModel>,
                response: Response<AddConditionsModel>
            ) {

                progressDialog!!.dismiss()

                addConditionsModel = response.body()!!

                if (addConditionsModel.status == 1) {

                    Singleton.stepTwoId = addConditionsModel.conditionDetails._id

                    if (Singleton.check_screens == "AddCondtion") {
                        requireActivity().onBackPressed()
                    } else if (Singleton.check_screens == "addMec_step_two") {
                        Navigation.findNavController(view1).navigate(R.id.action_addMedicationFragmentTwoFragment_to_addMedicationStepThreeFragment)
                    }

                    Toast.makeText(context, addConditionsModel.message, Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(context, addConditionsModel.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddConditionsModel>, t: Throwable) {
                progressDialog!!.dismiss()
                Snackbar.make(view1, t.message.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            }

        })
    }

    private fun SetDiagnosis_date() {

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateDateInView()
            }

            private fun updateDateInView() {

                val myFormat = "yyyy-MM-dd" // mention the format you need
                val Format = "dd/MMM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(Format, Locale.US)

                view1.diagnosis_date_!!.text = sdf.format(cal.time)

                val myFormat_ = "yyyy-MM-dd" // mention the format you need
                val sdf_ = SimpleDateFormat(myFormat_, Locale.US)

                diagnosisDate = sdf_.format(cal.time)

                Singleton.setlectConditionDate = diagnosisDate
            }

        }
        val datePickerDialog = DatePickerDialog(
            requireContext(), dateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)

        )

//        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis();

        datePickerDialog.show()
    }

    private fun SetRecyclerView() {

        if (conditionName.size > 7) {
            view1.conditions_name_layout.layoutParams.height = 700
        } else {
            view1.conditions_name_layout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        selectConditionAdapter = SelectConditionAdapter(contionlist, this)
        view1.select_condition_recycler.adapter = selectConditionAdapter
    }

    override fun onClickConditions(position: Int) {

        condition_name = contionlist[position].Condition_Treated

        view1.conditions_name.setText(condition_name)

        meddicien_name_layout = false

        view1.conditions_name.isCursorVisible = false

        Singleton.conditionsmane = condition_name

        view1.conditions_name_layout.visibility = View.GONE

        hideKeyboard()

    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity?.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun ProgresLoader() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog = ProgressDialog(context, R.style.MyAlertDialogStyle)
        progressDialog!!.show()
        progressDialog!!.setCancelable(false)
        Thread {
            try {
                Thread.sleep(10000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            progressDialog!!.dismiss()
        }.start()
    }

    override fun onclciitems(position: Int) {

        view1.selectDisg.setText(recipientsList[position].fullName)

        view1.DiagonsedBy_name_layout.visibility = View.GONE

        clickdiagoned = false

    }

    override fun onResume() {

        super.onResume()

        if (view1.conditions_name.text.isNotEmpty()) {
            view1.conditions_name_layout.visibility = View.GONE
        }

        val myFormat = "yyyy-MM-dd" // mention the format you need
        val Format = "dd/MMM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(Format, Locale.US)

        if (Singleton.check_screens == "AddCondtion" || Singleton.check_screens == "addMec_step_two") {
//            view1.diagnosis_date_!!.text = sdf.format(cal.time)
        } else {
            if (Singleton.editConditionDate.isNullOrEmpty()) {
                view1.diagnosis_date_.text = "Select Date"
            } else {

                val fromUser = SimpleDateFormat("yyyy-MM-dd")
                val myFormat = SimpleDateFormat("dd/MMM/yyyy")


                try {
                    diagnosisDate = Singleton.editConditionDate.toString()
                    view1.diagnosis_date_.text = myFormat.format(fromUser.parse(diagnosisDate))

                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }

        if (view1.selectDisg.text.isNotEmpty()) {
            view1.DiagonsedBy_name_layout.visibility = View.GONE
        }

    }


}