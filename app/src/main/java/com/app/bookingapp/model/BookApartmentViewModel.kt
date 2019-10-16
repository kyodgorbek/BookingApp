package com.app.bookingapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookApartmentViewModel:ViewModel() {

    val apartmentBookList: MutableLiveData<ArrayList<BookApartmentData>> by lazy {
        MutableLiveData<ArrayList<BookApartmentData>>()
    }
}