package com.example.healthcare.Fragments.PitientFragment.Recipients

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.healthcare.Adapter.ParametersAdapter
import com.example.healthcare.ModelClas.ApiModel.Recipient.GetRecipients.AddRecipients.AddRecipientsModel
import com.example.healthcare.ModelClas.ApiModel.Login.LoginModel
import com.example.healthcare.ModelClas.ApiModel.Recipient.GetRecipients.EditRecipient.EditRecipientsModel
import com.example.healthcare.Networking.Retrofit.Api.ApiClient
import com.example.healthcare.Networking.Retrofit.Api.ApiInterface
import com.example.healthcare.R
import com.example.timeless.SharedPrefrences.App
import com.example.timeless.SharedPrefrences.CommonUtils
import com.example.timeless.SharedPrefrences.Singleton
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_health_check.view.*
import kotlinx.android.synthetic.main.fragment_add_recipients.*
import kotlinx.android.synthetic.main.fragment_add_recipients.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern


@Suppress("DEPRECATION")
class AddRecipientsFragment : Fragment() ,  ParametersAdapter.ParameterInterface {

    private lateinit var view1: View

    private var recipientsImage = ""

    var recipientsNAme = ""

    var recipientsRelationShip = ""

    var recipientsType = ""

    var relationship_name = ""

    var recipientsPhoneNumber = ""

    var recipientsEmail = ""

    var relationShip = false

    var recipientsReEmail = ""

    var recipientsAddress = ""

    var recipientsCountry = ""

    var recipientsHospitalInfo = ""

    var  Relationship = ""

    private val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    private var pic: Bitmap? = null

    private var imageUri: Uri? = null

    private lateinit var recImage: MultipartBody.Part

    private lateinit var addRecipientsModel: AddRecipientsModel

    private lateinit var editRecipientsModel: EditRecipientsModel

    private lateinit var parametersAdapter: ParametersAdapter

    private lateinit var pattern: Pattern
    private lateinit var matcher: Matcher

    private  var relationship : ArrayList<String> = ArrayList()

    var progressDialog: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_add_recipients, container, false)

        CheckScrren()

        setAdapter()

//        SpinnerSet()

        TypeSpiner()

        Click()

        return view1
    }

    private fun setAdapter() {

        relationship.add("Clinician")
        relationship.add("Brother")
        relationship.add("Sister")
        relationship.add("Friend")
        relationship.add("Other")


        parametersAdapter =ParametersAdapter(relationship,this)
        view1.rec_reatyionship_recycler.adapter = parametersAdapter

    }


    private fun TypeSpiner() {
        val languages = resources.getStringArray(R.array.R_Type)

        if (view1.type_recipients_spinner != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item, languages
            )
            view1.type_recipients_spinner.adapter = adapter

            view1.type_recipients_spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
//
                    recipientsType = languages[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }
    }

    private fun Click() {

        view1.reci_realtion_dropDown.setOnClickListener {
            if (!relationShip) {
                view1.relationship_medicines.visibility = View.VISIBLE
                relationShip = true
            } else if (relationShip) {
                view1.relationship_medicines.visibility = View.GONE
                relationShip = false
            }
        }

        view1.Add_Recipients_back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        view1.recipients_image.setOnClickListener {
            SelectImage()
        }

        view1.Add_Recipients_btn.setOnClickListener {

            if (CommonUtils.isNetworkConnected(requireContext())){
                Validations()
            }
            else{
                CommonUtils.toast(context,"Check internet Connection")
            }
        }

        recipientsCountry = view1.recipients_cpp.defaultCountryName

        view1.recipients_cpp.setOnCountryChangeListener(CountryCodePicker.OnCountryChangeListener { selectedCountry ->
            recipientsCountry = selectedCountry.name
        })


    }

    private fun SelectImage() {
        val options = arrayOf<CharSequence>("Camera", "Photo Library", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Please select")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Camera") {
                dialog.dismiss()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivity(intent)
                try {
                    startActivityForResult(intent, 1)

                }catch (e:Exception){
                    CommonUtils.toast(context,"To add an Image, kindly allow Camera Permission")
                }
            } else if (options[item] == "Photo Library") {
                val gallery = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                )
                startActivityForResult(gallery, 2)
            }
        }
        builder.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                pic = data?.getParcelableExtra<Bitmap>("data")
                Log.e("Check", pic.toString())


                val path: String = MediaStore.Images.Media.insertImage(
                    requireContext().contentResolver, pic, "IMG_" + System.currentTimeMillis(), null
                )

                imageUri = Uri.parse(path)

                recipientsImage = getRealPathFromURI(imageUri)

                view1.recipients_image.setImageBitmap(pic)


//                uploadToCloudinary(getRealPathFromUri(requireContext(), imageUri))

            } else if (requestCode == 2) {

                imageUri = data?.data

                recipientsImage = getRealPathFromURI(imageUri)

                view1.recipients_image.setImageURI(imageUri)


//                uploadToCloudinary(getRealPathFromUri(requireContext(), imageUri))
            }
        }
    }

    fun getRealPathFromURI(uri: Uri?): String {
        var path = ""
        if (requireActivity().contentResolver != null) {
            val cursor: Cursor? =
                uri?.let { requireActivity().contentResolver.query(it, null, null, null, null) }
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun Validations() {

//        if (recipientsImage.isEmpty()){
//            Toast.makeText(context,"Please Select Recipients Image",Toast.LENGTH_SHORT).show()
//        }
        if (recipients_name.text.toString().isEmpty()){
            view1.recipients_name.error = "This field is Required"
        }
        else if (recipients_phoneNumber.text.toString().isEmpty()){
            view1.recipients_phoneNumber.error = "This field is Required"
        }
        else if (recipients_phoneNumber.length()<4){
            view1.recipients_phoneNumber.error = "Enter valid phone number"
        }

        else if (recipients_email.text.toString().trim().isEmpty()){
            view1.recipients_email.error = "This field is Required"
        }
        else if (!isValidEmail(recipients_email.text.toString().trim())) {
            view1.recipients_email.error = "Invalid email format"
        }
        else if (recipients_email.text.toString().trim()!=recipients_remail.text.toString().trim()){
            view1.recipients_remail.error = "Email do not match. Try Again"
        }
        else{
            if ( Singleton.screens == "steps_four_addRecipients" || Singleton.screens == "home_addRecipients"){
                AddRecipientsApi()
            }
            else if (Singleton.screens == "EditRecipient"){
//                Toast.makeText(context,"dhksbdh",Toast.LENGTH_SHORT).show()
                EditRecipientApi()
            }
        }
    }

    private fun CheckScrren() {
        if ( Singleton.screens == "steps_four_addRecipients" || Singleton.screens == "home_addRecipients"){

        }

        else if (Singleton.screens == "EditRecipient"){

            recipientsImage = ""

//            recipientsImage = Singleton.recipien_image.toString()

            view1.recipients_toolbar_title.text = "Edit Recipient Profile"

            if(Singleton.recipien_image.isNullOrEmpty()){
                Picasso.get()
                    .load(R.drawable.defauld_user_image)
                    .resize(200,200)
                    .centerCrop()
                    .into(view1.recipients_image)
            }
            else{
                Picasso.get()
                    .load(Singleton.recipien_image)
                    .resize(200,200)
                    .centerCrop()
                    .into(view1.recipients_image)
            }

            view1.recipients_name.setText(Singleton.recipien_name)
            view1.recipients_phoneNumber.setText(Singleton.recipien_phone)
            view1.recipients_email.setText(Singleton.recipien_email)
            view1.recipients_remail.setText(Singleton.recipien_email)
            view1.recipients_address.setText(Singleton.recipien_address)
            view1.relation.setText(Singleton.recipien_relation)
            view1.recipients_hospitalInfo.setText(Singleton.resOtherInfo)


        }

    }


    private fun EditRecipientApi() {

        ProgresLoader()

        val token = App.appPreference1.getUserDetails("SaveDetails", LoginModel::class.java).token

        val accessToken = "Bearer $token"

        recipientsNAme = view1.recipients_name.text.toString()
        recipientsPhoneNumber = view1.recipients_phoneNumber.text.toString()
        recipientsEmail = view1.recipients_email.text.toString()
        recipientsReEmail = view1.recipients_remail.text.toString()
        recipientsAddress = view1.recipients_address.text.toString()
        recipientsHospitalInfo = view1.recipients_hospitalInfo.text.toString()
        Relationship = view1.relation.text.toString()


        val name = RequestBody.create(MediaType.parse("text/plain"), view1.recipients_name.text.toString())
        val phone = RequestBody.create(MediaType.parse("text/plain"), view1.recipients_phoneNumber.text.toString())
        val email = RequestBody.create(MediaType.parse("text/plain"), recipientsEmail)
        val address = RequestBody.create(MediaType.parse("text/plain"), recipientsAddress)
        val hospitalInfo = RequestBody.create(MediaType.parse("text/plain"), view1.recipients_hospitalInfo.text.toString())
        val type = RequestBody.create(MediaType.parse("text/plain"), recipientsType)
        val relation = RequestBody.create(MediaType.parse("text/plain"), Relationship)
        val country = RequestBody.create(MediaType.parse("text/plain"), recipientsCountry)
        val _id = RequestBody.create(MediaType.parse("text/plain"), Singleton.recipientsID)



        if (!recipientsImage.isEmpty()) {

            val file = File(recipientsImage)

            val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)

            recImage = MultipartBody.Part.createFormData("image", file.name, requestFile)

        }
        else if (recipientsImage.isEmpty()) {
            val file = File("")
            val requestFile = RequestBody.create(MediaType.parse("image/*"), "")
            recImage = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        val apiClient = ApiClient.getRetrofitInstance().create(ApiInterface::class.java)

        apiClient.editRecipients(
            _id,
            name,
            relation,
            type,
            phone,
            email,
            address,
            country,
            hospitalInfo,
            recImage
        ).enqueue(object : retrofit2.Callback<EditRecipientsModel>{
            override fun onResponse(
                call: Call<EditRecipientsModel>,
                response: Response<EditRecipientsModel>
            ) {

                progressDialog!!.dismiss()

                editRecipientsModel = response.body()!!

                if (editRecipientsModel.status==1){
                    requireActivity().onBackPressed()
                    Toast.makeText(context,editRecipientsModel.message,Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,editRecipientsModel.message,Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<EditRecipientsModel>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun AddRecipientsApi() {

        ProgresLoader()

        val token = App.appPreference1.getUserDetails("SaveDetails", LoginModel::class.java).token

        val accessToken = "Bearer $token"

        recipientsNAme = view1.recipients_name.text.toString()
        recipientsPhoneNumber = view1.recipients_phoneNumber.text.toString()
        recipientsEmail = view1.recipients_email.text.toString()
        recipientsReEmail = view1.recipients_remail.text.toString()
        recipientsAddress = view1.recipients_address.text.toString()
        recipientsHospitalInfo = view1.recipients_hospitalInfo.text.toString()
        Relationship = view1.relation.text.toString()

        val name = RequestBody.create(MediaType.parse("text/plain"), recipientsNAme)
         val phone = RequestBody.create(MediaType.parse("text/plain"), recipientsPhoneNumber)
         val email = RequestBody.create(MediaType.parse("text/plain"), recipientsEmail)
         val address = RequestBody.create(MediaType.parse("text/plain"), recipientsAddress)
         val otherInfo = RequestBody.create(MediaType.parse("text/plain"), recipientsHospitalInfo)
         val type = RequestBody.create(MediaType.parse("text/plain"), recipientsType)
         val relation = RequestBody.create(MediaType.parse("text/plain"), Relationship)
         val country = RequestBody.create(MediaType.parse("text/plain"), recipientsCountry)


        if (!recipientsImage.isEmpty()) {

            val file = File(recipientsImage)
            val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            recImage = MultipartBody.Part.createFormData("image", file.name, requestFile)


        } else if (recipientsImage.isEmpty()) {
            val file = File("")
            val requestFile = RequestBody.create(MediaType.parse("image/*"), "")
            recImage = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        val apiClient = ApiClient.getRetrofitInstance().create(ApiInterface::class.java)

        apiClient.addRecipients(
            accessToken,
            name,
            relation,
            type,
            phone,
            email,
            address,
            country,
            otherInfo,
            recImage
        ).enqueue(object : retrofit2.Callback<AddRecipientsModel>{
            override fun onResponse(
                call: Call<AddRecipientsModel>,
                response: Response<AddRecipientsModel>
            ) {

                progressDialog!!.dismiss()

                addRecipientsModel = response.body()!!

                if (addRecipientsModel.status==1){
                    requireActivity().onBackPressed()
                    Toast.makeText(context,addRecipientsModel.message,Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,addRecipientsModel.message,Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<AddRecipientsModel>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onResume() {
        super.onResume()

//        SpinnerSet()

        TypeSpiner()

    }

    fun isValidEmail(email: String): Boolean {

        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()

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

    override fun clickonparametr(position: Int) {

        relationShip = false

        relationship_name = relationship[position]

        view1.relation.setText(relationship_name)

        view1.relationship_medicines.visibility = View.GONE

    }


}