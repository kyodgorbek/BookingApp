package com.app.bookingapp.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.bookingapp.domain.model.BookApartmentData

class BookApartmentViewModel:ViewModel() {

    val apartmentBookList: MutableLiveData<ArrayList<BookApartmentData>> by lazy {
        MutableLiveData<ArrayList<BookApartmentData>>()
    }
}