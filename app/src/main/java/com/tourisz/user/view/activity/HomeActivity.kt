package com.tourisz.user.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.widget.Toolbar
import android.view.View
import com.tourisz.R
import com.tourisz.user.view.fragment.*
import com.tourisz.util.listener.DateSelectListener
import com.tourisz.util.HomeOptions
import com.tourisz.util.listener.TimeSelectListener
import com.tourisz.util.helper.FragmentHelper
import com.tourisz.util.pickers.DatePickerFragment
import com.tourisz.util.pickers.TimePickerFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.*
import android.provider.MediaStore
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.android.volley.Request
import com.android.volley.Response
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.tourisz.Application
import com.tourisz.BuildConfig
import com.tourisz.api.URLS
import com.tourisz.api.request.*
import com.tourisz.api.response.*
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.paytm.PaymentActivity
import com.tourisz.service.UploaderService
import com.tourisz.util.Constants
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.user.view.fragment.child_fragment.MyEventBookingsFragment
import com.tourisz.user.view.fragment.child_fragment.MyHotelBookingsFragment
import com.tourisz.util.Constant
import com.tourisz.util.SearchTypes
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : BaseActivity(), ProfileFragment.OnFragmentInteractionListener, CommentFragment.OnFragmentInteractionListener, ImageUploadFragment.OnFragmentInteractionListener, View.OnClickListener, EventBookingDetailsFragment.OnFragmentInteractionListener, EventBookingFragment.OnFragmentInteractionListener, HotelBookingDetailsFragment.OnFragmentInteractionListener, HotelBookingFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener, BookNowFragment.OnFragmentInteractionListener, NumberPicker.OnValueChangeListener, SearchFragment.OnFragmentInteractionListener,
        MyBookingsFragment.OnFragmentInteractionListener, MyBookingDetailsFragment.OnFragmentInteractionListener,
        MyHotelBookingsFragment.OnFragmentInteractionListener, MyEventBookingsFragment.OnFragmentInteractionListener {

    private lateinit var txtTopTitle: TextView
    private lateinit var imgTopLogo: ImageView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var homeFragment: HomeFragment
    private lateinit var bookNowFragment: BookNowFragment
    private lateinit var hotelBookingFragment: HotelBookingFragment
    private lateinit var eventBookingFragment: EventBookingFragment

    private var searchMenu: MenuItem? = null
    private var searchType: Int = SearchTypes.USER_HOTELS
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUp()
        initIds()
        registerBroadcast()

        setTopLogo()
        homeFragment = HomeFragment.newInstance("", "")
        addFragment(homeFragment, false)

        fragmentManager.addOnBackStackChangedListener(this)

    }

    private fun registerBroadcast() {
        registerReceiver(broadCastReceiver, IntentFilter(Constants.ACTION_DONE))
        registerReceiver(broadCastReceiver, IntentFilter(Constants.ACTION_PAYMENT_FAIL))
        registerReceiver(broadCastReceiver, IntentFilter(Constants.ACTION_PAYMENT_DONE))

    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            when (intent?.action) {
                Constants.ACTION_DONE -> {
                    var currentFrag = getCurrentFragment()
                    if (currentFrag is CommentFragment) {
                        hideProgress()
                        showHome()
                    }

                    Handler().postDelayed({

                        when (intent.getIntExtra("type", 0)) {
                            UploaderService.type.UPDATE_BOOKED_HOTEL -> {
                                hideProgress()
                            }
                            UploaderService.type.UPDATE_BOOKED_EVENT -> {
                                hideProgress()
                            }

                        }

                    }, 1000)
                }
                Constants.ACTION_PAYMENT_DONE -> {
                    showSuccessDialog(intent.getSerializableExtra("data") as Object)
                }
                Constants.ACTION_PAYMENT_FAIL -> {
                    AndroidHelper.showToast(applicationContext, intent.getStringExtra("error"))
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchMenu = menu!!.findItem(R.id.action_search)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_search) {

            addSearchFragment(SearchFragment.newInstance(searchType, ""), true)

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackStackChanged() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is HomeFragment) {
            setTopLogo()
            setMenuVisibility(false)
        } else if (currentFragment is HotelBookingFragment) {
            setTopTitle(getString(R.string.hotel_booking))
            setMenuVisibility(true)
            searchType = SearchTypes.USER_HOTELS
        } else if (currentFragment is HotelBookingDetailsFragment) {
            setTopTitle(getString(R.string.hotel_details))
            setMenuVisibility(true)
            searchType = SearchTypes.USER_HOTELS
        } else if (currentFragment is EventBookingFragment) {
            setTopTitle(getString(R.string.event_booking))
            setMenuVisibility(true)
            searchType = SearchTypes.USER_EVENTS
        } else if (currentFragment is EventBookingDetailsFragment) {
            setTopTitle(getString(R.string.event_details))
            setMenuVisibility(true)
            searchType = SearchTypes.USER_EVENTS
        } else if (currentFragment is BookNowFragment) {
            setTopTitle(getString(R.string.book_now_title))
            setMenuVisibility(false)
        } else if (currentFragment is MyBookingsFragment) {
            setTopTitle(getString(R.string.my_booking))
            setMenuVisibility(true)
        } else if (currentFragment is MyBookingDetailsFragment) {
            setTopTitle(getString(R.string.details))
            setMenuVisibility(true)
        } else if (currentFragment is ImageUploadFragment) {
            setTopTitle(getString(R.string.img_upload))
            setMenuVisibility(false)
        } else if (currentFragment is CommentFragment) {
            setTopTitle(getString(R.string.cmt_upload))
            setMenuVisibility(false)
        } else if (currentFragment is AboutFragment) {
            setTopTitle(getString(R.string.about_us))
            setMenuVisibility(false)
        } else if (currentFragment is ContactFragment) {
            setTopTitle(getString(R.string.contact_us))
            setMenuVisibility(false)
        } else if (currentFragment is ProfileFragment) {
            setTopTitle(getString(R.string.my_profile))
            setMenuVisibility(false)
        } else {
            setMenuVisibility(false)
        }
        AndroidHelper.hideKeyboard(this)
    }

    private fun setUp() {
        val mToolbar = findViewById(R.id.toolbar) as Toolbar?
        supportActionBar?.setDisplayShowTitleEnabled(false);
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)
        mToolbar?.setNavigationOnClickListener { toggleDrawer() }

        fragmentManager = supportFragmentManager
    }

    private fun initIds() {
        txtTopTitle = findViewById(R.id.txtTopTitle)
        imgTopLogo = findViewById(R.id.imgTopLogo)


    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val currentFragment = getCurrentFragment()
            if (currentFragment is ImageUploadFragment) {
                showAttachmentWarning()
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun showAttachmentWarning() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert))
        builder.setMessage(getString(R.string.cancel_attachment))
        builder.setPositiveButton(getString(R.string.yes)) { dialogInterface: DialogInterface, i: Int ->
            fragmentManager.popBackStack()
        }

        builder.setNegativeButton(getString(R.string.no)) { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()
    }

    private fun showAttachmentWarning(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert))
        builder.setMessage(getString(R.string.cancel_attachment))
        builder.setPositiveButton(getString(R.string.yes)) { dialogInterface: DialogInterface, i: Int ->
            openNavItem(id)
        }

        builder.setNegativeButton(getString(R.string.no)) { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()
    }


    override fun onClick(view: View) {

        val currentFragment = getCurrentFragment()

        when (view.id) {
            R.id.txtNavHome -> {
                if (currentFragment is ImageUploadFragment) {
                    toggleDrawer()
                    showAttachmentWarning(view.id)
                } else {
                    openNavItem(view.id)
                }
            }
            R.id.txtNavHotel -> {
                if (currentFragment is ImageUploadFragment) {
                    toggleDrawer()
                    showAttachmentWarning(view.id)
                } else {
                    openNavItem(view.id)
                }
            }
            R.id.txtEvent -> {
                if (currentFragment is ImageUploadFragment) {
                    toggleDrawer()
                    showAttachmentWarning(view.id)
                } else {
                    openNavItem(view.id)
                }
            }
            R.id.txtNavBooking -> {
                if (currentFragment is ImageUploadFragment) {
                    toggleDrawer()
                    showAttachmentWarning(view.id)
                } else {
                    openNavItem(view.id)
                }
            }
            R.id.txtNavProfile -> {
                if (currentFragment is ImageUploadFragment) {
                    toggleDrawer()
                    showAttachmentWarning(view.id)
                } else {
                    openNavItem(view.id)
                }
            }
            R.id.txtNavAbout -> {
                if (currentFragment is ImageUploadFragment) {
                    toggleDrawer()
                    showAttachmentWarning(view.id)
                } else {
                    openNavItem(view.id)
                }
            }
            R.id.txtNavContact -> {
                if (currentFragment is ImageUploadFragment) {
                    toggleDrawer()
                    showAttachmentWarning(view.id)
                } else {
                    openNavItem(view.id)
                }
            }
            R.id.txtNavLogout -> {
                if (currentFragment is ImageUploadFragment) {
                    toggleDrawer()
                    showAttachmentWarning(view.id)
                } else {
                    openNavItem(view.id)
                }
            }
        }

    }

    private fun openNavItem(id: Int) {
        when (id) {
            R.id.txtNavHome -> {
                openHome()
            }
            R.id.txtNavHotel -> {
                openHotels()
            }
            R.id.txtEvent -> {
                openEvents()
            }
            R.id.txtNavBooking -> {
                openBooking()
            }
            R.id.txtNavProfile -> {
                openProfile()
            }
            R.id.txtNavAbout -> {
                openAbout()
            }
            R.id.txtNavContact -> {
                openContact()
            }
            R.id.txtNavLogout -> {
                logout()
            }
        }
    }

    fun toggleDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        AndroidHelper.hideKeyboard(this)
    }


    fun openHome() {
        setTopLogo()
        toggleDrawer()
        showHome()
    }


    fun showHome() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is HomeFragment) {
            clearBackstack()
        }

    }

    fun openHotels() {
        toggleDrawer()
        showHotels()
    }

    fun showHotels() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is HotelBookingFragment) {
            clearBackstack()
            setMenuVisibility(false)
            hotelBookingFragment = HotelBookingFragment.newInstance("", "")
            addFragment(hotelBookingFragment, true)
        }
    }

    fun openEvents() {
        toggleDrawer()
        showEvents()
    }

    fun showEvents() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is EventBookingFragment) {
            clearBackstack()
            setMenuVisibility(false)
            eventBookingFragment = EventBookingFragment.newInstance("", "")
            addFragment(eventBookingFragment, true)
        }
    }


    fun openBooking() {
        toggleDrawer()
        showBooking()
    }


    fun showBooking() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is MyBookingsFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(MyBookingsFragment.newInstance("", ""), true)
        }
    }

    fun openProfile() {
        toggleDrawer()
        showProfile()
    }

    fun showProfile() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is ProfileFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(ProfileFragment.newInstance("", ""), true)
        }
    }


    fun openAbout() {
        toggleDrawer()
        showAbout()
    }

    fun showAbout() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is AboutFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(AboutFragment.newInstance(false, ""), true)
        }
    }


    fun openContact() {
        toggleDrawer()
        showContact()
    }

    fun showContact() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is ContactFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(ContactFragment.newInstance(false, ""), true)
        }
    }

    fun logout() {
        AndroidHelper.addSharedPreference(this, Constants.USER_DATA, "")
        toggleDrawer()
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }


    fun setTopTitle(title: String) {
        imgTopLogo.visibility = View.GONE
        txtTopTitle.visibility = View.VISIBLE
        txtTopTitle.text = title
    }

    fun setTopLogo() {
        imgTopLogo.visibility = View.VISIBLE
        txtTopTitle.visibility = View.GONE
    }


    fun addSearchFragment(fragment: Fragment, addToBackstack: Boolean) {
        FragmentHelper.newInstance().addFragment(fragmentManager, fragment, addToBackstack, R.id.homeMainContainer)
    }

    fun addFragment(fragment: Fragment, addToBackstack: Boolean) {
        FragmentHelper.newInstance().addFragment(fragmentManager, fragment, addToBackstack, R.id.mainContainer)
    }

    fun replaceFragment(fragment: Fragment, addToBackstack: Boolean) {
        FragmentHelper.newInstance().replaceFragment(fragmentManager, fragment, addToBackstack, R.id.mainContainer)
    }


    override fun onHomeOptionSelected(selection: Int) {
        when (selection) {
            HomeOptions.HOTEL -> showHotels()

            HomeOptions.EVENT -> showEvents()

            HomeOptions.MY_BOOKING -> showBooking()

            HomeOptions.MY_PROFILE -> showProfile()

        }
    }

    override fun onHotelListItemClick(obj: Object) {
        if (obj is HotelData) {
            addFragment(HotelBookingDetailsFragment.newInstance(obj, ""), true)
        } else if (obj is EventData) {
            addFragment(EventBookingDetailsFragment.newInstance(obj, ""), true)
        }
    }

    override fun onHotelListBookingClick(obj: Object) {
        bookNowFragment = BookNowFragment.newInstance(obj, "")
        addFragment(bookNowFragment, true)
    }

    override fun onBookingClicked(obj: Object, forHotel: Boolean) {
        bookNowFragment = BookNowFragment.newInstance(obj, "")
        addFragment(bookNowFragment, true)
    }

    var fromDate: Long = System.currentTimeMillis()

    //private var datePickerFragment: DatePickerFragment? = null
    private var isDateShown = false

    fun showDatePickerDialog() {

        if (isBookNowFromDate) {
            fromDate = System.currentTimeMillis()
        }

        if (!isDateShown) {
            // isDateShown = true

        }

        val datePickerFragment = DatePickerFragment(dateSelectListener, fromDate)
        datePickerFragment?.show(fragmentManager, "datePicker")
    }


    private var timePickerFragment: TimePickerFragment? = null

    fun showTimePickerDialog() {

        if (timePickerFragment == null) {
            timePickerFragment = TimePickerFragment(timeSelectListener, selDate)
            timePickerFragment?.show(fragmentManager, "timePicker")
        } else {
            timePickerFragment?.dismiss()
            timePickerFragment = TimePickerFragment(timeSelectListener, selDate)
            timePickerFragment?.show(fragmentManager, "timePicker")
        }
    }

    var selDate = Date()

    internal var dateSelectListener: DateSelectListener = object : DateSelectListener {
        override fun onDateSelected(date: String) {

            // isDateShown = false

            val currentFragment = getCurrentFragment()
            if (currentFragment is BookNowFragment) {
                if (isBookNowFromDate) {
                    bookNowFragment.setFromDate(date)

                    try {
                        fromDate = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(date).time
                        Log.e("seltime", fromDate.toString())

                        selDate = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(date)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                } else {
                    bookNowFragment.setToDate(date)
                }
            }
        }
    }

    internal var timeSelectListener: TimeSelectListener = object : TimeSelectListener {
        override fun onTimeSelected(time: String) {
        }
    }

    override fun onBookedViewPagerChanged(position: Int) {
        if (position == 0) {
            searchType = SearchTypes.USER_BOOKED_HOTELS

        } else {
            searchType = SearchTypes.USER_BOOKED_EVENTS
        }
    }

    var isBookNowFromDate = true

    override fun onBookNowFromDateClicked() {
        isBookNowFromDate = true
        showDatePickerDialog()
    }

    override fun onBookNowToDateClicked() {
        isBookNowFromDate = false
        showDatePickerDialog()

    }


    override fun onValueChange(picker: NumberPicker?, old: Int, new: Int) {
    }

    override fun onBookNowBookClicked(dataObj: Object?, bookObj: Object?) {
        if (dataObj != null && bookObj != null) {
            showConfirmBookingDialog(dataObj, bookObj)
        }
    }


    override fun onBookNumPerClicked() {
        showNumberPicker(true)
    }

    override fun onBookNumRoomClicked() {
        showNumberPicker(false)
    }

    override fun onBookNumRoomTypeClicked() {
        showRoomTypeDialog()
    }


    override fun onBookingListItemClick(obj: Object) {
        if (obj is HotelBookingData) {
            addFragment(MyBookingDetailsFragment.newInstance(obj, ""), true)
        } else if (obj is EventBookingData) {
            addFragment(MyBookingDetailsFragment.newInstance(obj, ""), true)
        }
    }

    override fun onBookedDetailsCommentClicked(data: String) {
        showTextDialog(data)
    }

    override fun onBookedDetailsImageClicked(name: String) {
        showImageDialog(name)
    }

    override fun onCameraClicked() {
        openCamera()
    }

    override fun onGalleryClicked() {
        pickImage()
    }

    override fun onUploadClicked(path: String, obj: Object?) {
        addFragment(CommentFragment.newInstance(path, obj), true)
    }


    override fun onCommentSubmitted(path: String?, obj: Object?) {
        startUploaderService(path, obj)

    }

    override fun onImgUploadCancelled() {
        showHome()
    }


    override fun onSearchItemClicked(obj: Object, searchType: Int) {

        fragmentManager.popBackStack()
        when (searchType) {
            SearchTypes.USER_HOTELS -> {
                addFragment(HotelBookingDetailsFragment.newInstance(obj as HotelData, ""), true)
            }
            SearchTypes.USER_EVENTS -> {
                addFragment(EventBookingDetailsFragment.newInstance(obj as EventData, ""), true)
            }
            SearchTypes.USER_BOOKED_HOTELS -> {
                addFragment(MyBookingDetailsFragment.newInstance(obj, ""), true)
            }
            SearchTypes.USER_BOOKED_EVENTS -> {
                addFragment(MyBookingDetailsFragment.newInstance(obj, ""), true)
            }
        }
    }

    override fun onSearchBookClicked(obj: Object, searchType: Int) {
        fragmentManager.popBackStack()
        when (searchType) {
            SearchTypes.USER_HOTELS -> {
                bookNowFragment = BookNowFragment.newInstance(obj, "")
                addFragment(bookNowFragment, true)
            }
            SearchTypes.USER_EVENTS -> {
                bookNowFragment = BookNowFragment.newInstance(obj, "")
                addFragment(bookNowFragment, true)
            }
        }
    }

    override fun onChangePasswordClicked(userData: UserData, password: String) {
        if (AndroidHelper.isNetworkAvailable(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.change_pass_confirm))
            builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                callUpdateProfileAPI(userData, password)
            }

            builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            AndroidHelper.showToast(this, getString(R.string.no_internet))
        }
    }

    fun showNumberPicker(isPerson: Boolean) {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.number_picker_dialog)
        val txtNumberTitle = dialog.findViewById(R.id.txtNumberTitle) as TextView

        if (isPerson) {
            txtNumberTitle.text = getText(R.string.num_person)
        } else {
            txtNumberTitle.text = getText(R.string.num_room)
        }

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSet = dialog.findViewById(R.id.btnSet) as Button
        val np = dialog.findViewById(R.id.numberPicker) as NumberPicker
        np.maxValue = 100
        np.minValue = 1
        np.wrapSelectorWheel = false
        np.setOnValueChangedListener(this)
        btnSet.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                //tv.setText(np.value.toString())
                val currentFragment = getCurrentFragment()
                if (currentFragment is BookNowFragment) {
                    if (isPerson) {
                        bookNowFragment.setNoPer(np.value.toString())
                    } else {
                        bookNowFragment.setNoRoom(np.value.toString())
                    }
                }

                dialog.dismiss()
            }
        })
        btnCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog.dismiss()
            }
        })
        dialog.show()

    }


    fun showConfirmBookingDialog(dataObj: Object?, bookObj: Object?) {

        val dialog = Dialog(this, android.R.style.Theme)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black_trans_dialog)))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirm_booking_dialog)
        dialog.setCancelable(false)

        val linTime = dialog.findViewById(R.id.linTime) as LinearLayout
        val linPerson = dialog.findViewById(R.id.linPerson) as LinearLayout
        val linRoom = dialog.findViewById(R.id.linRoom) as LinearLayout
        val linRoomType = dialog.findViewById(R.id.linRoomType) as LinearLayout
        val linHotel = dialog.findViewById(R.id.linHotel) as LinearLayout
        val txtTitle = dialog.findViewById(R.id.txtTitle) as TextView
        val txtAddress = dialog.findViewById(R.id.txtAddress) as TextView
        val txtDate = dialog.findViewById(R.id.txtDate) as TextView
        val txtTime = dialog.findViewById(R.id.txtTime) as TextView
        val txtRoomType = dialog.findViewById(R.id.txtRoomType) as TextView

        val txtNoPerson = dialog.findViewById(R.id.txtNoPerson) as TextView
        val txtNoRoom = dialog.findViewById(R.id.txtNoRoom) as TextView
        val txtRate = dialog.findViewById(R.id.txtRate) as TextView


        if (dataObj is EventData && bookObj is BookEventRequest) {
            txtDate.text = dataObj.eventDateTime
            txtAddress.text = dataObj.address
            txtTitle.text = dataObj.eventTitle
            txtRate.text = bookObj.total.toString()
            txtNoPerson.text = bookObj.numberOfPersons.toString()

            linTime.visibility = View.GONE
            linPerson.visibility = View.VISIBLE
            linHotel.visibility = View.VISIBLE
            linRoom.visibility = View.GONE
            linRoomType.visibility = View.GONE
        } else if (dataObj is HotelData && bookObj is BookHotelRequest) {
            txtDate.text = "From " + bookObj.bookingFromDate + " to " + bookObj.bookingToDate
            txtAddress.text = dataObj.address
            txtTitle.text = dataObj.hotelName
            txtRate.text = bookObj.total.toString()
            txtRoomType.text = AndroidHelper.getRoomType(bookObj.roomType)
            txtNoPerson.text = bookObj.numberOfPersons.toString()
            txtNoRoom.text = bookObj.numberOfRooms.toString()

            linTime.visibility = View.GONE
            linHotel.visibility = View.GONE
            linPerson.visibility = View.VISIBLE
            linRoom.visibility = View.VISIBLE
            linRoomType.visibility = View.VISIBLE
        }

        val btnConfirm = dialog.findViewById(R.id.btnConfirm) as Button
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            showPayDialog(dataObj, bookObj)
        }


        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    fun showPayDialog(dataObj: Object?, bookObj: Object?) {

        val dialog = Dialog(this, android.R.style.Theme)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black_trans_dialog)))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.pay_now_dialog)
        dialog.setCancelable(false)

        var btnPaypal = dialog.findViewById(R.id.btnPaypal) as Button
        btnPaypal.setOnClickListener {
            dialog.dismiss()
            showPayPalDialog(dataObj, bookObj)
        }


        var btnWallet = dialog.findViewById(R.id.btnWallet) as Button
        btnWallet.setOnClickListener {
            dialog.dismiss()
            showWalletDialog(dataObj, bookObj)
        }


        var btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    fun showRoomTypeDialog() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.room_type_dialog)

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSet = dialog.findViewById(R.id.btnSet) as Button
        val spnType = dialog.findViewById(R.id.spnRoomType) as Spinner

        btnSet.setOnClickListener {
            val currentFragment = getCurrentFragment()
            if (currentFragment is BookNowFragment) {
                bookNowFragment.setRoomType(spnType.selectedItem.toString())
            }
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    fun showPayPalDialog(dataObj: Object?, bookObj: Object?) {

        val dialog = Dialog(this, android.R.style.Theme)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black_trans_dialog)))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.paypal_pay_dialog)
        dialog.setCancelable(false)
        var txtConfirm = dialog.findViewById(R.id.txtConfirm) as TextView


        if (bookObj is BookHotelRequest) {
            txtConfirm.text = "To confirm booking  \$" + bookObj.total

        } else if (bookObj is BookEventRequest) {
            txtConfirm.text = "To confirm booking  \$" + bookObj.total

        }

        var btnPayNow = dialog.findViewById(R.id.btnPayNow) as Button
        btnPayNow.setOnClickListener {
            dialog.dismiss()
            doPayPalPayment(dataObj, bookObj)
        }

        var btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showPayPalPaymentSuccessDialog(obj: Object?) {

        val dialog = Dialog(this, android.R.style.Theme)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black_trans_dialog)))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.paypal_payment_success_dialog)
        dialog.setCancelable(false)

        val btnOk = dialog.findViewById(R.id.btnOk) as Button
        btnOk.setOnClickListener {

            dialog.dismiss()
            openUploadAttach(obj)

        }
        dialog.show()

    }


    private fun openUploadAttach(obj: Object?) {

        val currentFrag = getCurrentFragment()
        if (currentFrag is BookNowFragment) {
            fragmentManager.popBackStack()
        }

        if (obj is BookedHotelData) {
            hotelBookingFragment.callAPI()
        } else if (obj is BookedEventData) {
            eventBookingFragment.callAPI()
        }
        addFragment(ImageUploadFragment.newInstance(obj, ""), true)

    }

    fun showWalletDialog(dataObj: Object?, bookObj: Object?) {

        val dialog = Dialog(this, android.R.style.Theme)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black_trans_dialog)))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.wallet_payment_message_dialog)
        dialog.setCancelable(false)

        var btnOk = dialog.findViewById(R.id.btnOk) as Button
        btnOk.setOnClickListener {
            dialog.dismiss()
            showPayDialog(dataObj, bookObj)
        }
        dialog.show()

    }


    fun showImageDialog(name: String) {

        Log.e("name", name)
        val dialog = Dialog(this, android.R.style.Theme)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black_trans_dialog)))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.show_image_dialog)
        dialog.setCancelable(false)
        var img = dialog.findViewById(R.id.img) as ImageView
        Glide.with(this).load(URLS.newInstance().IMG_URL + name)
                .placeholder(R.drawable.ic_hotelplaceholder)
                .error(R.drawable.ic_hotelplaceholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)

        var btnOk = dialog.findViewById(R.id.btnOk) as Button
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    fun showTextDialog(data: String) {

        val dialog = Dialog(this, android.R.style.Theme)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black_trans_dialog)))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.show_text_dialog)
        dialog.setCancelable(false)
        var txtData = dialog.findViewById(R.id.txtData) as TextView
        txtData.text = if (data.isEmpty()) "NA" else data

        var btnOk = dialog.findViewById(R.id.btnOk) as Button
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    private val REQUEST_IMAGE_CAPTURE = 1
    private val MY_CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 10

    private val PICK_PHOTO_FOR_AVATAR = 2;

    fun openCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),
                        MY_CAMERA_REQUEST_CODE)
            } else {
                dispatchTakePictureIntent()
            }
        } else {
            dispatchTakePictureIntent()
        }
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePhoto()
            } else {
                Toast.makeText(this, "write permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

/*
    private fun dispatchTakePictureIntent() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }


    }*/

    var mCurrentPhotoPath = ""

    fun createImageFile(): File {
        // Create an image file name
        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date());
        var imageFileName = "JPEG_" + timeStamp + "_";
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        var image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image
    }


    var REQUEST_TAKE_PHOTO = 1;

    fun dispatchTakePictureIntent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_REQUEST_CODE)
            } else {
                savePhoto()
            }
        } else {
            savePhoto()
        }


    }

    fun savePhoto() {
        var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile();
            } catch (e: IOException) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                var photoURI = FileProvider.getUriForFile(this,
                        "${BuildConfig.APPLICATION_ID}.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            /*  val extras = data?.extras
              val bm = extras?.get("data") as Bitmap*/
            Log.e("path", mCurrentPhotoPath)
            setUploadImage(mCurrentPhotoPath)


        } else if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return
            }
            val inputStream = getContentResolver().openInputStream(data.data)
            val bm = BitmapFactory.decodeStream(inputStream);
            setUploadImage("")
        }
    }

    fun setUploadImage(path: String) {
        val currentFragment = getCurrentFragment()
        if (currentFragment is ImageUploadFragment) {
            currentFragment.setImage(path)
        }
    }


    fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR)
    }


    fun getCurrentFragment(): Fragment? {
        return fragmentManager.findFragmentById(R.id.mainContainer)
    }

    fun setMenuVisibility(shows: Boolean) {
        searchMenu?.isVisible = shows
    }

    fun clearBackstack() {
        for (i in 0..fragmentManager.backStackEntryCount) {
            if (i != 0) {
                fragmentManager.popBackStack()
            }
        }
    }


    fun doPayPalPayment(dataObj: Object?, bookObj: Object?) {

        var intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("data", bookObj)
        startActivity(intent)

        /*  if (bookObj is BookHotelRequest) {
              bookObj.paymentTransactionId = "dummy"
              bookObj.paymentStatus = 0
          } else if (bookObj is BookEventRequest) {
              bookObj.paymentTransactionId = "dummy"
              bookObj.paymentStatus = 0
          }*/

    }


    fun showSuccessDialog(bookObj: Object?) {
        if (AndroidHelper.isNetworkAvailable(this)) {
            callBookAPI(bookObj)
        } else {
            AndroidHelper.showToast(this, getString(R.string.no_internet))
        }
    }


    fun callBookAPI(bookObj: Object?) {

        showProgress("Booking your request..")

        var url = ""
        var jsonObj: JSONObject? = null
        if (bookObj is BookHotelRequest) {
            url = URLS.newInstance().BOOK_HOTEL()
            val date = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(bookObj.bookingFromDate)

            bookObj.bookingFromDate = SimpleDateFormat(Constant.SERVER_DATE_FORMAT, Locale.ENGLISH).format(date)

            val date1 = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(bookObj.bookingToDate)

            bookObj.bookingToDate = SimpleDateFormat(Constant.SERVER_DATE_FORMAT, Locale.ENGLISH).format(date1)

            jsonObj = JSONObject(AndroidHelper.objectToString(bookObj))

        } else if (bookObj is BookEventRequest) {
            url = URLS.newInstance().BOOK_EVENT()
            jsonObj = JSONObject(AndroidHelper.objectToString(bookObj))
        }


        Log.e("req", AndroidHelper.objectToString(bookObj))

        if (jsonObj != null) {
            val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                    url, jsonObj,
                    Response.Listener<JSONObject> { response ->
                        Log.e("Response", response.toString())
                        hideProgress()

                        if (bookObj is BookHotelRequest) {
                            val bookedHotelResponse: BookedHotelResponse = Gson().fromJson(response.toString(), BookedHotelResponse::class.java)

                            if (bookedHotelResponse.result) {
                                AndroidHelper.showToast(this, bookedHotelResponse.response)
                                showPayPalPaymentSuccessDialog(bookedHotelResponse.getData())
                                //openUploadAttach(bookedHotelResponse.getData())

                            }
                        } else if (bookObj is BookEventRequest) {
                            val bookedEventResponse: BookedEventResponse = Gson().fromJson(response.toString(), BookedEventResponse::class.java)

                            if (bookedEventResponse.result) {
                                AndroidHelper.showToast(this, bookedEventResponse.response)
                                //openUploadAttach(bookedEventResponse.getData())

                                showPayPalPaymentSuccessDialog(bookedEventResponse.getData())


                            }
                        }


                    }, Response.ErrorListener { error ->
                hideProgress()
                AndroidHelper.showToast(this, "Error")

            })

            Application.getInstance().addToRequestQueue(jsonObjReq)

        }

    }


    fun callUpdateProfileAPI(userData: UserData, password: String) {
        showProgress("Updating profile..")

        var jsonObj = JSONObject(AndroidHelper.objectToString(userData))
        jsonObj.put("password", password)
        Log.e("req", AndroidHelper.objectToString(userData))

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().UPDATE_PROFILE(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    Log.e("Response", response.toString())
                    hideProgress()
                    AndroidHelper.showToast(this, response.getString("response"))

                }, Response.ErrorListener { error ->
            hideProgress()
            AndroidHelper.showToast(this, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)


    }

    override fun onChangeAdminPasswordClicked(oldPass: String, newPass: String) {
    }


    private fun startUploaderService(path: String?, obj: Object?) {

        showProgress("Updating..")
        val mServiceIntent = Intent(this, UploaderService::class.java)

        mServiceIntent.putExtra("data", obj)
        mServiceIntent.putExtra("path", path)
        mServiceIntent.setAction("start_service")

        startService(mServiceIntent)

    }


    fun showProgress(msg: String) {
        progressDialog = ProgressDialog.show(this, "Please wait", msg, true, false)
    }

    fun hideProgress() {
        progressDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReceiver)
    }

}

