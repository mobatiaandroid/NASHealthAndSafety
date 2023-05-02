package com.nas.healthandsafety.constants


import com.nas.healthandsafety.activity.fire_marshall.model.EvacuationEndResponseModel
import com.nas.healthandsafety.activity.fire_marshall.model.EvacuationStartResponseModel
import com.nas.healthandsafety.activity.home.model.EvacuationStatusResponseModel
import com.nas.healthandsafety.activity.home.model.StudentsResponseModel
import com.nas.healthandsafety.activity.login.model.LoginResponseModel
import com.nas.healthandsafety.activity.register.model.RegisterAccountModel
import com.nas.healthandsafety.activity.session_select.model.SubjectsResponseModel
import com.nas.healthandsafety.activity.session_select.model.YearGroupsResponseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    /***Access Token Generation***/
    @FormUrlEncoded
    @POST("oauth/access_token")
    fun accessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientID: String,
        @Field("client_secret") clientSecret: String,
        @Field("username") userName: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    /***Sign Up***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/signup_V1")
//    fun signUpAPICall(
//        @Field("access_token") accessToken: String,
//        @Field("email") emailID: String,
//        @Field("deviceid") deviceID: String,
//        @Field("devicetype") deviceType: String
//    ): Call<CreateAccountModel>

    @FormUrlEncoded
    @POST("api/v1/auth/register")
    fun register(
        @Field("email") email: String,
    ): Call<RegisterAccountModel>

    @FormUrlEncoded
    @POST("api/v1/auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponseModel>

    @GET("api/v1/students/get-yeargroups")
    fun getYearGroups(
        @Header("Authorization") token: String,
    ): Call<YearGroupsResponseModel>
    /***Log In***/

    @GET("api/v1/students/get-class-subjects")
    fun getSubjects(
        @Header("Authorization") token: String,
        @Query("class_id") classID: String,
    ): Call<SubjectsResponseModel>

    @GET("api/v1/students/get-class-students")
    fun getStudents(
        @Header("Authorization") token: String,
        @Query("class_id") classID: String,
    ): Call<StudentsResponseModel>

    @GET("api/v1/firebase/evacuation-status")
    fun getEvacuationStatus(
        @Header("Authorization") token: String
        ): Call<EvacuationStatusResponseModel>

//    @POST("api/v1/staffs/session-out")
//    fun postStaffNotify(
//        @Header("Authorization") token: String
//    ): Call<>

    @POST("api/v1/firebase/evacuation-start")
    fun postEvacuationStart(
        @Header("Authorization") token: String
    ): Call<EvacuationStartResponseModel>

    @POST("api/v1/firebase/evacuation-close")
    fun postEvacuationEnd(
        @Header("Authorization") token: String
    ): Call<EvacuationEndResponseModel>
//    @FormUrlEncoded
//    @POST("api/StaffApp/login_V1")
//    fun loginAPICall(
//        @Field("access_token") accessToken: String,
//        @Field("email") emailID: String,
//        @Field("password") password: String,
//        @Field("deviceid") deviceID: String,
//        @Field("devicetype") deviceType: String
//    ): Call<SignInModel>

    /***Year Groups***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/year_groups")
//    fun yearGroupsAPICall(
//        @Field("access_token") accessToken: String
//    ): Call<YearGroups>


    /***Students***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/students_V1")
//    fun  studentsAPICall(
//        @Field("access_token") accessToken: String,
//        @Field("class_id") classID: String
//    ): Call<StudentModel>


    /***Assembly Points***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/assembly_points_V1")
//    fun assemblyPoints(
//        @Field("access_token") accessToken: String
//    ): Call<AssemblyPointsModel>

    /***Evacuation Start***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/evacuation_start_V1")
//    fun evacuationStart(
//        @Field("access_token") accessToken: String,
//        @Field("staff_id") staffID: String,
//        @Field("class_id") classID: String,
//        @Field("assembly_point_id") assemblyID: String
//    ): Call<EvacuationModel>

    /***Log Out***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/logout_V1")
//    fun logOut(
//        @Field("access_token") accessToken: String,
//        @Field("staff_id") staffID: String,
//        @Field("deviceid") deviceID: String,
//        @Field("devicetype") deviceType: String
//    ): Call<LogoutModel>

    /***Change Password***/
    @FormUrlEncoded
    @POST("api/StaffApp/change_password_V1")
    fun changePassword(
        @Field("access_token") accessToken: String,
        @Field("staff_id") staffID: String,
        @Field("current_password") currentPassword: String,
        @Field("new_password") newPassword: String
    ): Call<ResponseBody>

    /***Recover Account***/
    @FormUrlEncoded
    @POST("api/StaffApp/forgot_password_V1")
    fun forgotPassword(
        @Field("access_token") accessToken:String,
        @Field("email") emailID: String
    ): Call<ResponseBody>

    /***Mark Attendance***/
    @FormUrlEncoded
    @POST("api/StaffApp/mark_attendance")
    fun markAttendance(
        @Field("access_token") accessToken: String,
        @Field("student_id") studentID: String,
        @Field("present") present: String
    ): Call<ResponseBody>

    /***Subjects***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/subjects")
//    fun subjectsAPICall(
//        @Field("access_token") accessToken: String
//    ): Call<SubjectsModel>

    /***Albums***/
    @FormUrlEncoded
    @POST("api/StaffApp/albums")
    fun albumsAPICall(
        @Field("access_token") accessToken: String,
        @Field("page_number") pageNumber: String
    ): Call<ResponseBody>

    /***Photos***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/photos")
//    fun photosAPICall(
//        @Field("access_token") accessToken: String,
//        @Field("album_id") albumID: String,
//        @Field("page_number") pageNumber: String
//    ): Call<PhotosModel>

    /***Videos***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/videos")
//    fun videosAPICall(
//        @Field("access_token") accessToken: String,
//        @Field("page_number") pageNumber: String
//    ): Call<VideosModel>

    /***Update Attendance***/
//    @FormUrlEncoded
//    @POST("api/StaffApp/mark_attendance")
//    fun attendanceUpdate(
//        @Field("access_token") accessToken: String,
//        @Field("student_id") studentID: String,
//        @Field("present") present: String
//    ): Call<StudentAttendanceModel>
}