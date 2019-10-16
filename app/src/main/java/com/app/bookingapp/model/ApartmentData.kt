package com.app.bookingapp.model

import java.io.Serializable

class ApartmentData : Serializable {

    var id: String? = null
    var bedrooms: Int? = null
    var name: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var diatance: Float? = null

    constructor(id: String, bedrooms: Int, name: String, latitude: Double, longitude: Double, diatance: Float){
        this.id = id
        this.bedrooms = bedrooms
        this.name = name
        this.latitude = latitude
        this.longitude = longitude
        this.diatance = diatance
    }
}