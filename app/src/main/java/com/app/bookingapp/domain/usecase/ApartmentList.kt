package com.app.bookingapp.domain.usecase

import com.app.bookingapp.domain.model.BedRoomFilter

interface ApartmentList {
    fun getApartmentList(bedRoomFilter: BedRoomFilter, dateFrom: String, dateTo: String, listener: GetApartmentListener)
}