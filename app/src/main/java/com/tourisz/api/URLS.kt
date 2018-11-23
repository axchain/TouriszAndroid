package com.tourisz.api

class URLS {

    companion object {
        @JvmStatic
        fun newInstance() = URLS()
    }

    var BASE_URL = "http://ec2-18-188-157-92.us-east-2.compute.amazonaws.com/api/?r="

    var FLYERSIMG_URL = "http://ec2-18-188-157-92.us-east-2.compute.amazonaws.com/api/flyers/"

    var IMG_URL = "http://ec2-18-188-157-92.us-east-2.compute.amazonaws.com/api/uploads/"

    fun SIGNUP(): String{
        return BASE_URL + "Users/Register"
    }

    fun LOGIN(): String{
        return BASE_URL + "Users/Login"
    }

    fun FORGOT(): String{
        return BASE_URL + "Users/ForgotPassword"
    }

    fun AGENT_ADD_HOTEL(): String{
        return BASE_URL + "Hotels/Create"
    }

    fun AGENT_UPDATE_HOTEL(): String{
        return BASE_URL + "Hotels/Update"
    }

    fun AGENT_ADD_EVENT(): String{
        return BASE_URL + "Events/Create"
    }

    fun AGENT_UPDATE_EVENT(): String{
        return BASE_URL + "Events/Update"
    }

    fun UPLOAD_IMAGES(): String{
        return BASE_URL + "General/UploadImages"
    }

    fun GET_UISETTING(): String{
        return "https://drive.google.com/uc?export=download&id=1R0zmGGt5HupCLSVivNHDW9GJLLkkcyvr"
    }

    fun GET_HOTEL_LIST(): String{
        return BASE_URL + "Hotels/Get"
    }

    fun GET_EVENT_LIST(): String{
        return BASE_URL + "Events/Get"
    }

    fun GET_FLYER_LIST(): String{
        return BASE_URL + "General/GetFlyer"
    }

    fun BOOK_HOTEL(): String{
        return BASE_URL + "Hotels/Book"
    }

    fun UPDATE_BOOKED_HOTEL(): String{
        return BASE_URL + "Hotels/BookingUpdate"
    }

    fun UPDATE_BOOKED_EVENT(): String{
        return BASE_URL + "Events/BookingUpdate"
    }

    fun BOOK_EVENT(): String{
        return BASE_URL + "Events/Book"
    }

    fun GET_HOTEL_BOOKINGS(): String{
        return BASE_URL + "Hotels/Bookings"
    }

    fun GET_EVENT_BOOKINGS(): String{
        return BASE_URL + "Events/Bookings"
    }

    fun UPDATE_PROFILE(): String{
        return BASE_URL + "Users/Update"
    }

    fun GET_ABOUT(): String{
        return BASE_URL + "General/GetAboutUs"
    }

    fun GET_CONTACT(): String{
        return BASE_URL + "General/GetContactUs"
    }

    fun GET_TNC(): String{
        return BASE_URL + "General/GetTermsAndCondition"
    }

    fun GET_AGENT_DETAILS(): String{
        return BASE_URL + "Users/AgentDetails"
    }

    fun GET_USER_LIST(): String{
        return BASE_URL + "Users/Users"
    }

    fun DELETE_HOTEL(): String{
        return BASE_URL + "Hotels/Delete"
    }

    fun DELETE_EVENT(): String{
        return BASE_URL + "Events/Delete"
    }

    fun UPDATE_USER(): String{
        return BASE_URL + "Users/Update"
    }

    fun GET_AGENT_LIST(): String{
        return BASE_URL + "Users/Agents"
    }

    fun GET_ADMIN_STATS(): String{
        return BASE_URL + "General/GetStats"
    }

    fun GET_AGENT_STATS(): String{
        return BASE_URL + "General/GetStats"
    }

    fun CREATE_FLYER(): String{
        return BASE_URL + "General/CreateFlyer"
    }

    fun UPDATE_FLYER(): String{
        return BASE_URL + "General/UpdateFlyer"
    }

    fun DELETE_FLYER(): String{
        return BASE_URL + "General/DeleteFlyer"
    }

    fun UPDATE_ABOUT(): String{
        return BASE_URL + "General/UpdateContent"
    }

    fun UPDATE_CONTACT(): String{
        return BASE_URL + "General/UpdateContent"
    }

    fun UPDATE_TNC(): String{
        return BASE_URL + "General/UpdateContent"
    }

    fun CHECK_EVENT_BOOKED(): String{
        return BASE_URL + "Events/IsBooked"
    }

    fun CHANGE_ADMIN_PASS(): String{
        return BASE_URL + "Users/UpdateAdminPassword"
    }
}