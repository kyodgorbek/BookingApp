package com.app.bookingapp

import android.os.Bundle
import android.util.Log
import com.app.bookingapp.base.BaseActivity
import com.app.bookingapp.model.BookApartmentViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.bookingapp.model.BookApartmentData

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric




class MainActivity : BaseActivity() {

    companion object{
        var PERMISSION_REQUEST_LOCATION = 111
        lateinit var apartmentListViewModel:BookApartmentViewModel

        fun getTimeMilesFromDate(date:String):Long{
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
           return sdf.parse(date)!!.time
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)


        // Get the ViewModel.
        apartmentListViewModel = ViewModelProviders.of(this).get(BookApartmentViewModel::class.java)

        val bookApartmentListObserver = Observer<ArrayList<BookApartmentData>> { apartmentBookList ->
            // Update the UI, in this case, a TextView.

            Log.d(">>CART:VALUE MAIN -", " "+apartmentBookList.size)

        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        apartmentListViewModel.apartmentBookList.observe(this, bookApartmentListObserver)


    }




}
