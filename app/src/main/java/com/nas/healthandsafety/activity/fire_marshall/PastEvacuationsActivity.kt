package com.nas.healthandsafety.activity.fire_marshall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.fire_marshall.adapter.PastEvacautionAdapter
import com.nas.healthandsafety.activity.home.model.EvacuationStatusResponseModel
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PastEvacuationsActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var pastEvacuationsRecycler: RecyclerView
    lateinit var backButton: ImageView
    lateinit var progressBarDialog: ProgressBarDialog
    var evacuationsList: ArrayList<EvacuationStatusResponseModel.Data> = ArrayList()
    lateinit var evacuationAdapter: PastEvacautionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_evacuations)
        initialiseUI()
        callEvacuationStatusAPI()

    }

    private fun initialiseUI() {
        context = this
        backButton = findViewById(R.id.back_button)
        pastEvacuationsRecycler = findViewById(R.id.pastEvacuationsRecycler)
        progressBarDialog = ProgressBarDialog(context)
        backButton.setOnClickListener {
            val intent = Intent(context, MarshallEvacuationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

    }

    private fun callEvacuationStatusAPI() {
        var evacuationStatusResponse: EvacuationStatusResponseModel
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<EvacuationStatusResponseModel> = ApiClient.getClient.getEvacuationStatus(
                "Bearer " + PreferenceManager.getAccessToken(context)
            )
            progressBarDialog.show()
            call.enqueue(object : Callback<EvacuationStatusResponseModel> {
                override fun onResponse(
                    call: Call<EvacuationStatusResponseModel>,
                    response: Response<EvacuationStatusResponseModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                    } else {
                        evacuationStatusResponse = response.body()!!
                        if (evacuationStatusResponse.status == 200) {
                            if (evacuationStatusResponse.data!!.isNotEmpty()) {
                                for (i in evacuationStatusResponse.data!!.indices) {


                                    evacuationsList.add(evacuationStatusResponse.data!![i]!!)


                                }
                                evacuationAdapter = PastEvacautionAdapter(context, evacuationsList)
                                pastEvacuationsRecycler.layoutManager =
                                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                                pastEvacuationsRecycler.adapter = evacuationAdapter
                            } else {
                                AppUtils.showMessagePopUp(context, "No evacuations are in progress")
                            }
                        } else if (evacuationStatusResponse.status == 401) {
                            AppUtils.showMessagePopUp(
                                context,
                                "Unauthenticated or Token Expired, Please Login"
                            )
                        } else {
                            AppUtils.showMessagePopUp(
                                context,
                                getString(R.string.text_unknown_error)
                            )
                        }
                    }

                }

                override fun onFailure(call: Call<EvacuationStatusResponseModel>, t: Throwable) {
                    progressBarDialog.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })

        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }

}