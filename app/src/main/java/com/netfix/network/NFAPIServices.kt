package com.netfix.network

import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.dataModel.ConnectionVO
import com.netfix.models.network.dataModel.LocationVO
import com.netfix.models.network.dataModel.ProfileVO
import com.netfix.models.network.request.UserReqModel
import com.netfix.models.network.request.alert.AlertVO
import com.netfix.models.network.request.complaint.AssignComplaintModel
import com.netfix.models.network.request.connection.ConnectionRequestModel
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.models.network.request.login.LoginRequestModel
import com.netfix.models.network.request.plan.PlanAllRequestModel
import com.netfix.models.network.request.plan.PlanUpdateRequestModel
import com.netfix.models.network.request.service.AddServiceRequestModel
import com.netfix.models.network.request.service.ServiceRequestModel
import com.netfix.models.network.request.service.UpdateServiceRequestModel
import com.netfix.models.network.request.signup.SignUpRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.alert.AlertMsgVO
import com.netfix.models.network.response.banners.BannerResponseModel
import com.netfix.models.network.response.complaint.ComplaintCancelResponseModel
import com.netfix.models.network.response.complaint.ComplaintResponseModel
import com.netfix.models.network.response.complaint.NewComplaintResponseModel
import com.netfix.models.network.response.connection.ConnectionResponseModel
import com.netfix.models.network.response.login.LoginResponseModel
import com.netfix.models.network.response.plan.PlanAllResponseModel
import com.netfix.models.network.response.plan.PlanResponseModel
import com.netfix.models.network.response.service.AddServiceResponseModel
import com.netfix.models.network.response.service.ServiceAllResponseModel
import com.netfix.ui.complaint.ComplaintDetailRepository
import com.netfix.ui.dashboard.DashboardRepository
import com.netfix.ui.notification.AlertCancelReq
import retrofit2.Call
import retrofit2.http.*

interface NFAPIServices {

    //feedback
    @Headers("ContentType: application/json")
    @GET("User/GetFeedback")
    fun getFeedbacks(): Call<ResponseModel<List<FeedbackRequestModel>>>


    @Headers("ContentType: application/json")
    @POST("Complaint/ComplaintClose")
    fun cancelTicket(@Body ticket: ComplaintDetailRepository.Ticket): Call<ResponseModel<String>>

    @Headers("ContentType: application/json")
    @POST("Complaint/ComplaintAssignTo")
    fun assignComplaint(@Body loginModel: AssignComplaintModel): Call<ResponseModel<Any>>

    //Agents
    @Headers("ContentType: application/json")
    @GET("User/GetAgent")
    fun getAgents(): Call<ResponseModel<List<UserReqModel>>>

    @Headers("ContentType: application/json")
    @POST("User/SignIn")
    fun validateUser(@Body loginModel: LoginRequestModel): Call<LoginResponseModel>

    //SERVICE

    @Headers("ContentType: application/json")
    @POST("Services/GetServices")
    fun getAllServices(@Body reqModel: ServiceRequestModel): Call<ServiceAllResponseModel>

    @Headers("ContentType: application/json")
    @POST("Services/AddServices")
    fun addService(@Body addServiceRequestModel: AddServiceRequestModel): Call<AddServiceResponseModel>

    @Headers("ContentType: application/json")
    @POST("Services/UpdateServices")
    fun updateService(@Body updateServiceRequestModel: UpdateServiceRequestModel): Call<AddServiceResponseModel>

    //PLAN
    @Headers("ContentType: application/json")
    @POST("Plan/GetPlanDetail")
    fun getAllPlans(@Body planAllRequestModel: PlanAllRequestModel): Call<PlanAllResponseModel>

    @Headers("ContentType: application/json")
    @POST("Plan/UpdatePlanDetail")
    fun updatePlan(@Body planUpdateRequestModel: PlanUpdateRequestModel): Call<PlanResponseModel>

    @Headers("ContentType: application/json")
    @POST("Plan/AddPlanDetail")
    fun addPlan(@Body planUpdateRequestModel: PlanUpdateRequestModel): Call<PlanResponseModel>

    //BANNERS
    @Headers("ContentType: application/json")
    @GET("StaticFile/GetBanner")
    fun getBanners(): Call<BannerResponseModel>


    //NEW CONNECTION
    @Headers("ContentType: application/json")
    @POST("NewConnection/AddNewConnection")
    fun requestNewConnection(@Body connectionRequestModel: ConnectionRequestModel): Call<ConnectionResponseModel>

    //All CONNECTION
    @Headers("ContentType: application/json")
    @GET("NewConnection/GetNewConnection")
    fun getAllConnections(): Call<ResponseModel<List<ConnectionVO>>>

    //COMPLAINTS
    @Headers("ContentType: application/json")
    @GET("Complaint/GetComplaint")
    fun getAllComplaints(@Query("userId") userId: Int): Call<ComplaintResponseModel>

    @Headers("ContentType: application/json")
    @POST("Complaint/AddComplaint")
    fun raiseComplaint(@Body complaintVO: ComplaintVO): Call<NewComplaintResponseModel>

    @Headers("ContentType: application/json")
    @POST("Complaint/SearchComplaint")
    fun getTicketDetail(@Body ticket: ComplaintDetailRepository.Ticket): Call<ComplaintResponseModel>

    @Headers("ContentType: application/json")
    @POST("Complaint/SearchComplaint")
    fun getComplaintForUser(@Body compReq: DashboardRepository.ComplaintReq): Call<ComplaintResponseModel>

    //SignUp
    @Headers("ContentType: application/json")
    @POST("User/SignUp")
    fun signUp(@Body signUpRequestModel: SignUpRequestModel): Call<ResponseModel<SignUpRequestModel>>


    @Headers("ContentType: application/json")
    @GET("User/GetAddressArea")
    fun getLocations(): Call<ResponseModel<List<LocationVO>>>


    @Headers("ContentType: application/json")
    @POST("/User/AddFeedback")
    fun submitFeedback(@Body feedbackRequestModel: FeedbackRequestModel): Call<ResponseModel<FeedbackRequestModel>>


    @Headers("ContentType: application/json")
    @GET("User/GetProfile")
    fun getProfile(@Query("userID") userId: Int): Call<ResponseModel<ProfileVO>>

    @Headers("ContentType: application/json")
    @POST("User/UpdateProfile")
    fun updateProfile(@Body model:ProfileVO): Call<ResponseModel<ProfileVO>>


    @Headers("ContentType: application/json")
    @POST("User/SearchUserDetail")
    fun getAllUsers(@Body body: Any = Object()): Call<ResponseModel<List<UserReqModel>>>

    @Headers("ContentType: application/json")
    @POST("MessageAlert/AddMessage")
    fun submitAlert(@Body alertVO: AlertVO): Call<ResponseModel<AlertVO>>

    @Headers("ContentType: application/json")
    @POST("MessageAlert/UpdateMessage")
    fun cancelAlert(@Body alertCancelReq: AlertCancelReq): Call<ResponseModel<AlertVO>>



    @Headers("ContentType: application/json")
    @GET("User/GetUserAlertMessage")
    fun getUserAlert(@Query("userID") userId: Int): Call<ResponseModel<List<AlertMsgVO>>>



}