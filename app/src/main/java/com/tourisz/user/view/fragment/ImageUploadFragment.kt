package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.tourisz.R
import com.tourisz.api.request.Object
import com.tourisz.api.request.UpdateEventBookingRequest
import com.tourisz.api.request.UpdateHotelBookingRequest
import com.tourisz.api.response.BookedEventData
import com.tourisz.api.response.BookedHotelData
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ImageUploadFragment : Fragment(), View.OnClickListener {
    private var obj: Object? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                obj = it.getSerializable(ARG_PARAM1) as Object?
                param2 = it.getString(ARG_PARAM2)
            }
        }
    }

    private lateinit var mView: View
    private lateinit var relUpload: RelativeLayout
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button
    private lateinit var btnUpload: Button
    private lateinit var btnCancel: Button
    private lateinit var img: ImageView
    private lateinit var txtSelectImage: TextView
    private lateinit var path: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_image_upload, container, false)
        initIds()
        return mView
    }

    private fun initIds() {
        relUpload = mView.findViewById(R.id.relUpload)
        btnCamera = mView.findViewById(R.id.btnCamera)
        btnGallery = mView.findViewById(R.id.btnGallery)
        btnUpload = mView.findViewById(R.id.btnUpload)
        btnCancel = mView.findViewById(R.id.btnCancel)
        txtSelectImage = mView.findViewById(R.id.txtSelectImage)
        img = mView.findViewById(R.id.img)

        setListener(btnCamera, btnGallery, btnUpload, btnCancel)
    }

    fun setListener(vararg views: View){
        for (view in views){
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.btnCamera -> onCameraClicked()
            R.id.btnGallery -> onGalleryClicked()
            R.id.btnUpload -> onUploadClicked()
            R.id.btnCancel -> onImgUploadCancelled()
        }
    }

    fun onCameraClicked() {
        listener?.onCameraClicked()
    }

    fun onGalleryClicked() {
        listener?.onGalleryClicked()
    }

    fun onUploadClicked() {
        if (obj is BookedHotelData){
            var updateHotelBookingRequest = UpdateHotelBookingRequest()
            updateHotelBookingRequest.bookingId = (obj as BookedHotelData).bookingId
            listener?.onUploadClicked(path, updateHotelBookingRequest)

        } else if (obj is BookedEventData){
            var updateEventBookingRequest = UpdateEventBookingRequest()
            updateEventBookingRequest.bookingId = (obj as BookedEventData).bookingId
            listener?.onUploadClicked(path, updateEventBookingRequest)

        }
    }

    fun onImgUploadCancelled() {
        listener?.onImgUploadCancelled()
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
        fun onCameraClicked()
        fun onGalleryClicked()
        fun onUploadClicked(path: String, obj: Object?)
        fun onImgUploadCancelled()
    }

    companion object {
        @JvmStatic
        fun newInstance(obj: Object?, param2: String) =
                ImageUploadFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, obj)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun setImage(path: String){
        this.path = path
        relUpload.visibility =View.VISIBLE
        txtSelectImage.visibility =View.GONE

        Glide.with(this).load(File(path))
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .thumbnail(0.1f)
                .into(img)
    }
}
