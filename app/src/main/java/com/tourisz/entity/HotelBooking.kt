package com.tourisz.entity

import java.io.Serializable

class HotelBooking : Serializable {
    var type: Int = 0

    constructor() {}

    constructor(type: Int) {
        this.type = type
    }
}
