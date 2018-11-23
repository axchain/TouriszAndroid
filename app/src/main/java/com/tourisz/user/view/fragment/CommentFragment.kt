package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.tourisz.R
import com.tourisz.api.request.Object
import com.tourisz.api.request.UpdateEventBookingRequest
import com.tourisz.api.request.UpdateHotelBookingRequest
import com.tourisz.api.response.BookingData

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CommentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var path: String? = null
    private var obj: Object? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                path = it.getString(ARG_PARAM1)
                obj = it.getSerializable(ARG_PARAM2) as Object
            }
        }
    }

    private lateinit var mView: View
    private lateinit var btnComment: Button
    private lateinit var edtComment: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_comment, container, false)
        btnComment = mView.findViewById(R.id.btnComment)
        edtComment = mView.findViewById(R.id.edtComment)

        btnComment.setOnClickListener { onCommentSubmitted() }

        return mView
    }

    fun onCommentSubmitted() {
        if (obj is UpdateHotelBookingRequest){
            (obj as UpdateHotelBookingRequest).attachmentDescription = edtComment.text.toString()
        } else if (obj is UpdateEventBookingRequest){
            (obj as UpdateEventBookingRequest).attachmentDescription = edtComment.text.toString()
        }
        listener?.onCommentSubmitted(path, obj)
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
        fun onCommentSubmitted(path: String?, obj: Object?)
    }

    companion object {
        @JvmStatic
        fun newInstance(path: String?, obj: Object?) =
                CommentFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, path)
                        putSerializable(ARG_PARAM2, obj)
                    }
                }
    }
}
