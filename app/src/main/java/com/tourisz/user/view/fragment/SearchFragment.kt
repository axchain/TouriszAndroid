package com.tourisz.user.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import com.tourisz.Application
import com.tourisz.R
import com.tourisz.admin.adapter.UsersAdapter
import com.tourisz.api.URLS
import com.tourisz.api.request.Object
import com.tourisz.api.response.*
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.user.adapter.MultiViewTypeAdapter
import com.tourisz.util.SearchTypes
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.listener.SearchClickListener
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment(), SearchClickListener {
    // TODO: Rename and change types of parameters
    private var searchType: Int = SearchTypes.USER_HOTELS
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchType = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mView: View
    private lateinit var searchView: SearchView
    private lateinit var rvHotel: RecyclerView
    private var arrayList: ArrayList<Object> = ArrayList()
    private lateinit var txtNoResult: TextView
    private lateinit var imgBack: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_search, container, false)

        txtNoResult = mView.findViewById(R.id.txtNoResult)
        rvHotel = mView.findViewById(R.id.rvHotel)
        imgBack = mView.findViewById(R.id.imgBack)
        progressBar = mView.findViewById(R.id.progressBar)

        rvHotel.layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(rvHotel.getContext(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(activity?.baseContext!!, R.drawable.custom_divider)!!)
        rvHotel.addItemDecoration(divider)


        searchView = mView.findViewById(R.id.searchView)
        searchView.setFocusable(true)
        searchView.setIconifiedByDefault(true)
        searchView.setFocusable(true)
        searchView.setIconified(false)
        // searchView.clearFocus()
        searchView.requestFocusFromTouch()
        val searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(Color.WHITE)
        searchEditText.setHintTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {


                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (!TextUtils.isEmpty(s) && s.length > 2) {
                    if (AndroidHelper.isNetworkAvailable(activity)) {
                        callSearchApi(s)
                    } else {
                        AndroidHelper.showToast(activity, getString(R.string.no_internet))
                    }

                } else if(TextUtils.isEmpty(s)){
                    Application.getInstance().cancelPendingRequests()
                    arrayList.clear()
                    rvHotel.visibility = View.GONE
                    txtNoResult.visibility = View.VISIBLE
                }
                return false
            }
        })



        imgBack.setOnClickListener { activity?.onBackPressed() }

        //AndroidHelper.showToast(activity, "type"+searchType)
        return mView
    }


    override fun onItemClick(position: Int) {
        onSearchItemClicked(arrayList[position])
    }


    override fun onBookItemClick(position: Int) {
        onSearchBookClicked(arrayList[position])
    }


    fun onSearchItemClicked(obj: Object) {
        listener?.onSearchItemClicked(obj, searchType)
    }

    fun onSearchBookClicked(obj: Object) {
        listener?.onSearchBookClicked(obj, searchType)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onSearchItemClicked(obj: Object, searchType: Int)
        fun onSearchBookClicked(obj: Object, searchType: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(searchType: Int, param2: String) =
                SearchFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, searchType)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    fun callSearchApi(query: String) {
        Application.getInstance().cancelPendingRequests()

        when (searchType) {
            SearchTypes.USER_HOTELS -> searchHotel("userId", query)
            SearchTypes.USER_EVENTS -> searchEvent("userId", query)
            SearchTypes.USER_BOOKED_HOTELS -> searchHotelBooking("userId", query)
            SearchTypes.USER_BOOKED_EVENTS -> searchEventBooking("userId", query)
            SearchTypes.AGNET_HOTELS -> searchHotel("agentId", query)
            SearchTypes.AGNET_EVENTS -> searchEvent("agentId", query)
            SearchTypes.ADMIN_HOTELS -> searchHotel("agentId", query)
            SearchTypes.ADMIN_EVENTS -> searchEvent("agentId", query)
            SearchTypes.ADMIN_USERS -> callGetUsersListAPI( query)
            SearchTypes.ADMIN_AGNETS -> callGetAdminsListAPI( query)


        }
    }

    fun searchHotel(key: String, query: String) {
        progressBar.visibility = View.VISIBLE
        val jsonObj = JSONObject()
        if (SearchTypes.ADMIN_HOTELS == searchType) {
            jsonObj.put(key, 0)
        } else {
            jsonObj.put(key, AndroidHelper.getUser(activity).id)
        }
        jsonObj.put("title", query)

        Log.e("req", jsonObj.toString())
        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_HOTEL_LIST(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    progressBar.visibility = View.GONE
                    arrayList.clear()

                    Log.e("Response", response.toString())
                    var hotelListResponse: HotelListResponse = Gson().fromJson(response.toString(), HotelListResponse::class.java)

                    if (hotelListResponse.result) {
                        if (hotelListResponse.data.size > 0) {
                            rvHotel.visibility = View.VISIBLE
                            txtNoResult.visibility = View.GONE
                            arrayList.addAll(hotelListResponse.data)
                            rvHotel.adapter = MultiViewTypeAdapter(Application.getInstance(), arrayList, searchType, this)


                        } else {
                            rvHotel.visibility = View.GONE
                            txtNoResult.visibility = View.VISIBLE
                        }

                    } else {
                        rvHotel.visibility = View.GONE
                        txtNoResult.visibility = View.VISIBLE
                    }


                }, Response.ErrorListener { error ->
            progressBar.visibility = View.GONE
            rvHotel.visibility = View.GONE
            txtNoResult.visibility = View.VISIBLE
            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }


    fun searchHotelBooking(key: String, query: String) {
        progressBar.visibility = View.VISIBLE

        val jsonObj = JSONObject()
        jsonObj.put(key, AndroidHelper.getUser(activity).id)
        jsonObj.put("title", query)

        Log.e("req", jsonObj.toString())
        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_HOTEL_BOOKINGS(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    progressBar.visibility = View.GONE
                    arrayList.clear()

                    Log.e("Response", response.toString())
                    var hotelListResponse: HotelBookingsResponse = Gson().fromJson(response.toString(), HotelBookingsResponse::class.java)

                    if (hotelListResponse.result) {
                        if (hotelListResponse.data.size > 0) {
                            rvHotel.visibility = View.VISIBLE
                            txtNoResult.visibility = View.GONE
                            arrayList.addAll(hotelListResponse.data)
                            rvHotel.adapter = MultiViewTypeAdapter(Application.getInstance(), arrayList, searchType, this)


                        } else {
                            rvHotel.visibility = View.GONE
                            txtNoResult.visibility = View.VISIBLE
                        }

                    } else {
                        rvHotel.visibility = View.GONE
                        txtNoResult.visibility = View.VISIBLE
                    }


                }, Response.ErrorListener { error ->
            progressBar.visibility = View.GONE
            rvHotel.visibility = View.GONE
            txtNoResult.visibility = View.VISIBLE
            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }


    fun searchEvent(key: String, query: String) {
        progressBar.visibility = View.VISIBLE

        val jsonObj = JSONObject()
        if (SearchTypes.ADMIN_HOTELS == searchType||SearchTypes.ADMIN_EVENTS== searchType) {
            jsonObj.put(key, 0)
        } else {
            jsonObj.put(key, AndroidHelper.getUser(activity).id)
        }


        jsonObj.put("title", query)
        Log.e("Req", jsonObj.toString())

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_EVENT_LIST(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    progressBar.visibility = View.GONE
                    arrayList.clear()

                    Log.e("Response", response.toString())
                    var hotelListResponse: EventListResponse = Gson().fromJson(response.toString(), EventListResponse::class.java)

                    if (hotelListResponse.result) {
                        if (hotelListResponse.data.size > 0) {
                            rvHotel.visibility = View.VISIBLE
                            txtNoResult.visibility = View.GONE
                            arrayList.addAll(hotelListResponse.data)
                            rvHotel.adapter = MultiViewTypeAdapter(Application.getInstance(), arrayList, searchType, this)
                        } else {
                            rvHotel.visibility = View.GONE
                            txtNoResult.visibility = View.VISIBLE
                        }

                    } else {
                        rvHotel.visibility = View.GONE
                        txtNoResult.visibility = View.VISIBLE
                    }

                }, Response.ErrorListener { error ->
            progressBar.visibility = View.GONE
            rvHotel.visibility = View.GONE
            txtNoResult.visibility = View.VISIBLE
            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }


    fun searchEventBooking(key: String, query: String) {
        progressBar.visibility = View.VISIBLE

        val jsonObj = JSONObject()
        jsonObj.put(key, AndroidHelper.getUser(activity).id)
        jsonObj.put("title", query)

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_EVENT_BOOKINGS(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    progressBar.visibility = View.GONE
                    arrayList.clear()

                    Log.e("Response", response.toString())
                    var hotelListResponse: EventBookingsResponse = Gson().fromJson(response.toString(), EventBookingsResponse::class.java)

                    if (hotelListResponse.result) {
                        if (hotelListResponse.data.size > 0) {
                            rvHotel.visibility = View.VISIBLE
                            txtNoResult.visibility = View.GONE
                            arrayList.addAll(hotelListResponse.data)
                            rvHotel.adapter = MultiViewTypeAdapter(Application.getInstance(), arrayList, searchType, this)
                        } else {
                            rvHotel.visibility = View.GONE
                            txtNoResult.visibility = View.VISIBLE
                        }

                    } else {
                        rvHotel.visibility = View.GONE
                        txtNoResult.visibility = View.VISIBLE
                    }


                }, Response.ErrorListener { error ->
            progressBar.visibility = View.GONE
            rvHotel.visibility = View.GONE
            txtNoResult.visibility = View.VISIBLE
            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }


    fun callGetAdminsListAPI(query: String) {

        progressBar.visibility = View.VISIBLE

        val jsonObj = JSONObject()
        jsonObj.put("title", query)

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_AGENT_LIST(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    progressBar.visibility = View.GONE
                    arrayList.clear()

                    Log.e("Response", response.toString())
                    var agentsListResponse: AgentsListResponse = Gson().fromJson(response.toString(), AgentsListResponse::class.java)

                    if (agentsListResponse.result) {
                        if (agentsListResponse.data.size > 0) {
                            rvHotel.visibility = View.VISIBLE
                            txtNoResult.visibility = View.GONE
                            arrayList.addAll(agentsListResponse.data)
                            rvHotel.adapter = MultiViewTypeAdapter(Application.getInstance(), arrayList, searchType, this)

                        } else {
                            rvHotel.visibility = View.GONE
                            txtNoResult.visibility = View.VISIBLE
                        }

                    } else {
                        rvHotel.visibility = View.GONE
                        txtNoResult.visibility = View.VISIBLE
                    }

                }, Response.ErrorListener { error ->

            progressBar.visibility = View.GONE
            rvHotel.visibility = View.GONE
            txtNoResult.visibility = View.VISIBLE
            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }


    fun callGetUsersListAPI(query: String) {

        progressBar.visibility = View.VISIBLE

        val jsonObj = JSONObject()
        jsonObj.put("title", query)

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_USER_LIST(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    progressBar.visibility = View.GONE
                    arrayList.clear()

                    Log.e("Response", response.toString())
                    var usersListResponse: UsersListResponse = Gson().fromJson(response.toString(), UsersListResponse::class.java)

                    if (usersListResponse.result) {
                        if (usersListResponse.data.size > 0) {
                            rvHotel.visibility = View.VISIBLE
                            txtNoResult.visibility = View.GONE
                            arrayList.addAll(usersListResponse.data)
                            rvHotel.adapter = MultiViewTypeAdapter(Application.getInstance(), arrayList, searchType, this)

                        } else {
                            rvHotel.visibility = View.GONE
                            txtNoResult.visibility = View.VISIBLE
                        }

                    } else {
                        rvHotel.visibility = View.GONE
                        txtNoResult.visibility = View.VISIBLE
                    }

                }, Response.ErrorListener { error ->

            progressBar.visibility = View.GONE
            rvHotel.visibility = View.GONE
            txtNoResult.visibility = View.VISIBLE
            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }

    override fun onDestroy() {
        super.onDestroy()
        Application.getInstance().cancelPendingRequests()

    }
}
