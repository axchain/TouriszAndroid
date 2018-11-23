package com.tourisz.admin.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.anychart.anychart.*
import com.google.gson.Gson
import com.tourisz.Application
import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.response.HotelListResponse
import com.tourisz.api.response.StatsData
import com.tourisz.api.response.StatsResponse
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.util.helper.AndroidHelper
import org.json.JSONObject
import java.util.*
import kotlin.math.absoluteValue


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminHomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mView: View
    private lateinit var progressBar: ProgressBar
    private lateinit var txtNoData: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_admin, container, false)
        progressBar = mView.findViewById(R.id.progressBar)
        txtNoData = mView.findViewById(R.id.txtNoData)

        if (AndroidHelper.isNetworkAvailable(activity)) {
            callGraphAPI()
        } else {
            AndroidHelper.showToast(activity, getString(R.string.no_internet))
        }
        return mView
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AdminHomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    fun callGraphAPI() {

        val jsonObj = JSONObject()

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_ADMIN_STATS(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    progressBar.visibility = View.GONE
                    Log.e("Response", response.toString())
                    var statsResponse: StatsResponse = Gson().fromJson(response.toString(), StatsResponse::class.java)

                    if (statsResponse.result) {
                        if (statsResponse.data.size > 0) {
                            setData(statsResponse.data)
                        } else {
                            txtNoData.visibility = View.VISIBLE
                        }
                    } else {
                        txtNoData.visibility = View.VISIBLE
                    }

                }, Response.ErrorListener { error ->
            txtNoData.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }

    fun setData(stats: ArrayList<StatsData>) {
        val anyChartView = mView.findViewById(R.id.any_chart_view) as AnyChartView

        val cartesian = AnyChart.cartesian()
        cartesian.xAxis.labels.setRotation(65)
        cartesian.xAxis.setStaggerLines(2)
        cartesian.xAxis.labels.setFontSize("10")

        cartesian.yScale.setMaximumGap(0)
        cartesian.yScale.setMinimumGap(0)

        cartesian.yScale.setStickToZero(true)
        cartesian.setYScale(ScaleTypes.LINEAR)
        cartesian.yScale.setMinimum(0)
        cartesian.yAxis.labels.setFormat("{%Value}{groupsSeparator: }")


        val data: ArrayList<DataEntry> = ArrayList()

        var isZero = false
        for (i in 0..stats.size - 1) {
            if (stats[i].value == null || stats[i].value <= 4) {
                isZero = true
            }
            data.add(ValueDataEntry(stats[i].title, if(stats[i].value == 0 ) null else stats[i].value))

        }

//        var isAllZero = true
//        for (i in 0..stats.size - 1) {
//            if (stats[i].value != 0 && stats[i].value != null){
//                isAllZero = false
//            }
//            //
//            data.add(ValueDataEntry(stats[i].title, if(stats[i].value == 0 ) null else stats[i].value))
//
//
//        }

        // stats.sortedWith(compareBy({it.value}))

        if (isZero /*|| stats[stats.size -1].value < 5*/){
            Log.e("all0", "true");
            cartesian.yScale.setMaximum(4)
        }

        anyChartView.setChart(cartesian)

        val column = cartesian.column(data)
        column.labels.setAdjustFontSize(true, true)
        column.tooltip.setTitleFormat("{%X}")
                .setPosition(Position.CENTER)
                .setAnchor(EnumsAnchor.CENTER_BOTTOM)
                .setOffsetX(0)
                .setOffsetY(5)
                .setFormat("{%Value}{groupsSeparator: }")

        cartesian.setXAxis("Users")
        cartesian.setYAxis("Numbers")

    }

}
