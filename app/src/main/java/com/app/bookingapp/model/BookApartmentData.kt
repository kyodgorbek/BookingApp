package com.app.bookingapp.model

import java.io.Serializable

class BookApartmentData : Serializable {

    var id: String? = null
    var bedrooms: Int? = null
    var name: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var diatance: Float? = null
    var fromDate: Long? = null
    var toDate:Long? = null

    constructor(
        id: String, bedrooms: Int, name: String,
        latitude: Double, longitude: Double, diatance: Float,
        fromDate: Long, toDate: Long
                ){
        this.id = id
        this.bedrooms = bedrooms
        this.name = name
        this.latitude = latitude
        this.longitude = longitude
        this.diatance = diatance
        this.fromDate = fromDate
        this.toDate = toDate
    }
}