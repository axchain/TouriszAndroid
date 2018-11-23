package com.tourisz.util

interface SearchTypes {
    companion object {

        val USER_HOTELS = 0
        val USER_EVENTS = 1
        val USER_BOOKED_HOTELS = 2
        val USER_BOOKED_EVENTS = 3

        val AGNET_HOTELS = 4
        val AGNET_EVENTS = 5
        val AGNET_HOTEL_BOOKINGS = 15
        val AGNET_EVENT_BOOKINGS = 16

        val ADMIN_USERS = 6
        val ADMIN_AGNETS = 7
        val ADMIN_HOTELS = 8
        val ADMIN_EVENTS = 9
        val ADMIN_HOTEL_BOOKINGS = 10
        val ADMIN_EVENT_BOOKINGS = 11
        val ADMIN_HOTEL_TRANSACTIONS = 12
        val ADMIN_EVENT_TRANSACTIONS = 13

        val ADMIN_FLYER = 14

    }
}
