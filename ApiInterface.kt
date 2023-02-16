package com.example.healthcare.Networking.Retrofit.Api


import com.example.healthcare.ModelClas.ApiModel.ActiveandDiscontinue.HealthCehck.ActiveDiscontinueHealthModel
import com.example.healthcare.ModelClas.ApiModel.ActiveandDiscontinue.Medication.ActivenadDiscontinueModel
import com.example.healthcare.ModelClas.ApiModel.AddSubscribedUsers.SubscribedUsersModel
import com.example.healthcare.ModelClas.ApiModel.Conditions.AddConditions.AddConditionsModel
import com.example.healthcare.ModelClas.ApiModel.Recipient.GetRecipients.AddRecipients.AddRecipientsModel
import com.example.healthcare.ModelClas.ApiModel.Medication.AllMedicines.AllMedicinesModel
import com.example.healthcare.ModelClas.ApiModel.Conditions.GetConditionName.ConditionsModel
import com.example.healthcare.ModelClas.ApiModel.Conditions.GetUsersConditions.GetConditionsModel
import com.example.healthcare.ModelClas.ApiModel.ConditonMedicationMoidel.ConditionMedModel
import com.example.healthcare.ModelClas.ApiModel.ConnectedClinician.ConnectedClinicianModel
import com.example.healthcare.ModelClas.ApiModel.Delete.deleteRecipients.DeleteRecipinetsModel
import com.example.healthcare.ModelClas.ApiModel.Discontinue.DiscontinuedMedicationModel
import com.example.healthcare.ModelClas.ApiModel.ForgotPassWord.ForgotPAssModel
import com.example.healthcare.ModelClas.ApiModel.HealrhCehckReminderModel.HealthcheckremonderModel
import com.example.healthcare.ModelClas.ApiModel.HealthCheck.AddHealthCheck.AddHealthCheckModel
import com.example.healthcare.ModelClas.ApiModel.Recipient.GetRecipients.GetRecipientModel
import com.example.healthcare.ModelClas.ApiModel.Login.LoginModel
import com.example.healthcare.ModelClas.ApiModel.Otp.OtpModel
import com.example.healthcare.ModelClas.ApiModel.Race.RaceModel
import com.example.healthcare.ModelClas.ApiModel.Recipient.GetRecipients.EditRecipient.EditRecipientsModel
import com.example.healthcare.ModelClas.ApiModel.ResetPassword.ResetPassModel
import com.example.healthcare.ModelClas.ApiModel.Signupmodel.SignupModel
import com.example.healthcare.ModelClas.ApiModel.HealthCheck.HealthCheckList.GetHealthCheckNameMocel
import com.example.healthcare.ModelClas.ApiModel.HealthCheck.OverDue.OverDueModel
import com.example.healthcare.ModelClas.ApiModel.Invoice.InvoiceDetail.InvoiceDetailsModel
import com.example.healthcare.ModelClas.ApiModel.Invoice.PendingInvoiceModel
import com.example.healthcare.ModelClas.ApiModel.Invoice.RequestInvoiceModel
import com.example.healthcare.ModelClas.ApiModel.Medication.AddImage.AddMedImageModel
import com.example.healthcare.ModelClas.ApiModel.Medication.AddUserMedication.AddUserMedicationModel
import com.example.healthcare.ModelClas.ApiModel.Medication.MyMedication.MyMedicationModel
import com.example.healthcare.ModelClas.ApiModel.Notes.addNotes.AddNotesModel
import com.example.healthcare.ModelClas.ApiModel.Notes.getNoets.GetUserNotesModel
import com.example.healthcare.ModelClas.ApiModel.Notification.CountNot.NotificationcountModel
import com.example.healthcare.ModelClas.ApiModel.Notification.Notification.NotificationModel
import com.example.healthcare.ModelClas.ApiModel.PastHealthCehckSummary.PastcheckModel
import com.example.healthcare.ModelClas.ApiModel.PendningPayment.PendingPaymentModel
import com.example.healthcare.ModelClas.ApiModel.Profile.getProfile.PProfileModel
import com.example.healthcare.ModelClas.ApiModel.Summery.AllSummary.AllMedicationSummaryModel
import com.example.healthcare.ModelClas.ApiModel.Summery.DoseChange.DoseChnageSummaryModel
import com.example.healthcare.ModelClas.ApiModel.Summery.PatientConditionSummary.ConditionSummaryModel
import com.example.healthcare.ModelClas.ApiModel.Summery.PatientMedicationsummary.MedicationsumaryModel
import com.example.healthcare.ModelClas.ApiModel.Summery.PatinetSummary.PatientSummaryModel
import com.example.healthcare.ModelClas.ApiModel.Summery.Refill.RefillSummaryMoldel
import com.example.healthcare.ModelClas.ApiModel.Summery.Usages.UsagessummaryModel
import com.example.healthcare.ModelClas.ApiModel.TotalRevenue.TotalRevenueModel
import com.example.healthcare.ModelClas.ApiModel.subPlans.SubPlansModel
import com.example.healthcare.ModelClas.ApiModel.subscriptionPitientsDetal.SubPitientsDetailModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @POST("registeration")
    fun signUp(@Body body: RequestBody?): Call<SignupModel>?

    @POST("login")
    fun login(@Body body: RequestBody): Call<LoginModel>

    @PUT("forgot-Password")
    fun forgotPassword(@Body body: RequestBody): Call<ForgotPAssModel>

    @PUT("reset-password")
    fun resetPassword(@Body body: RequestBody): Call<ResetPassModel>

    @POST("manage-user-otp")
    fun otp(@Body body: RequestBody): Call<OtpModel>

    @GET("get-races-list")
    fun raceList(): Call<RaceModel>

    @GET("get-all-medicines")
    fun allMedicinesName(): Call<AllMedicinesModel>

    @POST("get-conditions")
    fun allConditions(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<ConditionsModel>

    @POST("add-users-medication")
    fun addMedicines(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<AddUserMedicationModel>

    @Multipart
    @POST("upload-image")
    fun addMedicinesImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<AddMedImageModel>

    @POST("add-users-conditions")
    fun addUsersConditions(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<AddConditionsModel>

    @Multipart
    @POST("add-users-recipients")
    fun addRecipients(
        @Header("Authorization") token: String,
        @Part("fullName") fullName: RequestBody,
        @Part("relationship") relationship: RequestBody,
        @Part("type") type: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("country") country: RequestBody,
        @Part("otherInfo") otherInfo: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<AddRecipientsModel>

    @GET("get-recipient-users")
    fun getRecipient(
        @Header("Authorization") token: String
    ): Call<GetRecipientModel>

    @POST("showSubscriptionPlans")
    fun getsubscriptionplandetails(
        @Body body: RequestBody
    ): Call<SubPlansModel>


    @GET("get-users-conditions")
    fun getUserConditions(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") token: String
    ): Call<GetConditionsModel>


    @HTTP(method = "DELETE", path = "recipients-profile-delete", hasBody = true)
    fun deleteRecipients(@Body body: RequestBody): Call<DeleteRecipinetsModel>

    @Multipart
    @PUT("recipients-profile-edit")
    fun editRecipients(
        @Part("_id") id: RequestBody,
        @Part("fullName") fullName: RequestBody,
        @Part("relationship") relationship: RequestBody,
        @Part("type") type: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("country") country: RequestBody,
        @Part("otherInfo") hospitalInfo: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<EditRecipientsModel>

    @POST("get-health-checks")
    fun getHealthCheckName(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<GetHealthCheckNameMocel>

    @POST("get-users-medication")
    fun getUsersMedication(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<MyMedicationModel>

    @POST("add-health-checkups")
    fun addHealthCheck(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<AddHealthCheckModel>


    @PUT("discontinue-medicine")
    fun discontinuedMedication(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DiscontinuedMedicationModel>


    @PUT("update-refill-status")
    fun addRefillMedication(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DiscontinuedMedicationModel>


    @POST("medication-active-status")
    fun activeDiscontinueMedication(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<ActivenadDiscontinueModel>


    @HTTP(method = "DELETE", path = "delete-users-conditions", hasBody = true)
    fun deleteConditions(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @POST("past-health-checkups")
    fun pastHealcheck(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<OverDueModel>


    @POST("overdue-health-checks")
    fun overDue(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<OverDueModel>

    @POST("overComing-health-checks")
    fun comingUp(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<OverDueModel>

    @PUT("update-status-of-medication")
    fun updateMedicationStatus(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @PUT("edit-report")
    fun editHealthCheckReport(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @POST("get-active/Inactive-healthchecks")
    fun activeDiscontinueHealth(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<ActiveDiscontinueHealthModel>


    @PUT("users-steps-completed")
    fun submitAllsateps(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @PUT("update-status-healthcheck")
    fun discontinueHealthCheck(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @PUT("edit-medications")
    fun editActiveMedication(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @POST("get-all-summary")
    fun allSummaryMedication(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<AllMedicationSummaryModel>

    @POST("get-complete-refill-summary")
    fun getRefillSummary(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<RefillSummaryMoldel>

    @POST("get-medication-changed-summary")
    fun getDosageChangeSummary(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DoseChnageSummaryModel>

    @POST("past-check-summary")
    fun pastCheck(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<PastcheckModel>


    @POST("add-notes")
    fun addNotes(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<AddNotesModel>

    @PUT("edit-notes")
    fun editNotes(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<AddNotesModel>



    @GET("get-notes-users")
    fun getUserNotes(
        @Header("Authorization") token: String
    ): Call<GetUserNotesModel>


    @GET("get-user-profile")
    fun patientProfile(
        @Header("Authorization") token: String
    ): Call<PProfileModel>

    @Multipart
    @PUT("edit-user-profile")
    fun editPatientProfile(
        @Header("Authorization") token: String,
        @Part("firstName") fullName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("country") country: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<PProfileModel>

    @PUT("edit-condition")
    fun editCondition(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<AddConditionsModel>

    @POST("get-condition-with-allMedications")
    fun getconditionwithallMedications(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<ConditionMedModel>

    @POST("past-undo-checkups")
    fun pastChedckUndo(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @POST("delete-notes")
    fun deleteNotes(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @POST("logout")
    fun logOut(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>


    @GET("get-user-notification")
    fun notification(
        @Header("Authorization") token: String,
    ): Call<NotificationModel>

    @GET("unseen-notifications-count")
    fun notificationCount(
        @Header("Authorization") token: String,
    ): Call<NotificationcountModel>

    @PUT("updating-notifications-to-seen")
    fun notificationseen(
        @Header("Authorization") token: String,
    ): Call<DeleteRecipinetsModel>

    @PUT("notification-enable")
    fun rec_notification(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @PUT("update-medication-reminder")
    fun medicationReminder(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @PUT("update-medication-refill-reminder")
    fun addRefillReminder(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @PUT("update-health-check-reminder")
    fun healthCheckReminder(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>

    @GET("time-period-options")
    fun healthCheckReminderName(
        @Header("Authorization") token: String,
    ): Call<HealthcheckremonderModel>

    @GET("time-period-medication-reminder")
    fun medicationReminderName(
        @Header("Authorization") token: String,
    ): Call<HealthcheckremonderModel>

    @POST("medication-usage-data")
    fun userSummary(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<UsagessummaryModel>

    @POST("add-subscribed-users")
    fun subscriptionPlan(
        @Body body: RequestBody
    ): Call<SubscribedUsersModel>

    @POST("get-active-inActive-patients")
    fun subscriptionPitientDetail(
        @Body body: RequestBody
    ): Call<SubPitientsDetailModel>

    @POST("clinician/generate-users-invoices")
    fun generateInvoice(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<RequestInvoiceModel>

    @POST("patient/get-generated-invoices")
    fun generateInvoicePatient(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<PendingInvoiceModel>

    @POST("patient/accept-decline-invoices")
    fun acceptAndDeclineInvoice(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<DeleteRecipinetsModel>


    @POST("patient/get-invoice-details")
    fun InvoiceDetails(
        @Body body: RequestBody
    ): Call<InvoiceDetailsModel>

    @GET("patient/connected-clinicians")
    fun ConnectedClinician(@Header("Authorization") token: String, ): Call<ConnectedClinicianModel>

    @POST("patient/data")
    fun PatientSummary(
        @Body body: RequestBody
    ): Call<PatientSummaryModel>

    @POST("patient/conditions-summary")
    fun PatientConditionSummary(
        @Body body: RequestBody
    ): Call<ConditionSummaryModel>

    @POST("patient/medications-summary")
    fun PatientMedicationSummary(
        @Body body: RequestBody
    ): Call<MedicationsumaryModel>

    @GET("clinician/pending-payment")
    fun pendingPaymentApi(
        @Header("Authorization") token: String,
    ): Call<PendingPaymentModel>

    @POST("clinician-revenue")
    fun clinicianTotalRevenue(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<TotalRevenueModel>
}