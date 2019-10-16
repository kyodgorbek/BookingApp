package com.app.bookingapp


import android.app.DatePickerDialog
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.app.bookingapp.adapter.ApartmentListAdapter
import com.app.bookingapp.base.BaseActivity
import com.app.bookingapp.connection.APIrequest
import com.app.bookingapp.connection.ErrorCode
import com.app.bookingapp.connection.UrlCons
import com.app.bookingapp.model.ApartmentData
import com.app.bookingapp.receiver.ConnectivityReceiver
import com.app.bookingapp.receiver.OnApiResponse
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), OnApiResponse {

    lateinit var baseActivity: BaseActivity
    var listApartments = ArrayList<ApartmentData>()

    lateinit var adapter:ApartmentListAdapter

    var isFromSearch = false
    var bed = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivity = BaseActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listApartments.clear()
        adapter = context?.let { ApartmentListAdapter(it,listApartments) }!!
        recylerApartment.adapter = adapter

        isFromSearch = false

        callAPI()


        ed_fromDate.setOnClickListener { selectDate(ed_fromDate,"FROM") }
        ed_toDate.setOnClickListener { selectDate(ed_toDate,"TO") }

        btnSearch.setOnClickListener { doSearch() }

    }


    fun callAPI(){
        if(ConnectivityReceiver.isInternet(context!!)){

            baseActivity.showCustomProgrssDialog(context!!)

            activity?.let { APIrequest(baseActivity,it,
                context!!, this, UrlCons.tagApartments,
                UrlCons.GetApartments,
                Request.Method.GET, "") }

        }else{
            baseActivity.noInternetConnection(baseActivity, context!!)
        }
    }

    fun doSearch(){

        bed = spinner.selectedItem.toString()
        var fromDate = ed_fromDate.text.toString()
        var toDate = ed_toDate.text.toString()

        if(bed.equals("0")){
            Toast.makeText(context,"Select bedroom",Toast.LENGTH_SHORT).show()
        }else{
            if(fromDate.length==0){
                Toast.makeText(context,"Select From Date",Toast.LENGTH_SHORT).show()
            }else{
                if (toDate.length == 0){
                    Toast.makeText(context,"Select To Date",Toast.LENGTH_SHORT).show()
                }else{

                    // Do Search

                    Toast.makeText(context,"DONE",Toast.LENGTH_SHORT).show()
                    isFromSearch = true
                    callAPI()

                   // adapter.filter.filter(bed)

                }
            }
        }


    }





    override fun onSuccess(result: String, type: String) {

        baseActivity.hideCustomProgrssDialog()

       //Log.d(">>Result:",""+result)

        listApartments.clear()

        try {


            var valueArray = JSONArray(result)


            if(isFromSearch){

                populateFilterList(valueArray)

            }else {
                for (i in 0 until valueArray.length()) {

                    var jsonObject = valueArray.optJSONObject(i)

                    var apartmentData = ApartmentData(
                        jsonObject.optString("id"),
                        jsonObject.optInt("bedrooms"),
                        jsonObject.optString("name"),
                        jsonObject.optDouble("latitude"),
                        jsonObject.optDouble("longitude"),
                        getDistance(
                            jsonObject.optDouble("latitude"),
                            jsonObject.optDouble("longitude")
                        )
                    )


                    listApartments.add(apartmentData)

                }


                // Sort list

                Collections.sort(listApartments, object : Comparator<ApartmentData> {
                    override fun compare(one: ApartmentData?, other: ApartmentData?): Int {
                        return one?.diatance?.compareTo(other?.diatance!!)!!
                    }
                })

                adapter.notifyDataSetChanged()
                if(listApartments.size == 0){
                    Toast.makeText(context,"No apartment found",Toast.LENGTH_SHORT).show()
                }
            }



        }catch (e:Exception){
            Log.d(">>-",e.toString())
            baseActivity.alertDialogWithMsg(
                context!!,
                android.R.drawable.ic_dialog_alert, "Error !", ErrorCode.EXCEPTION_MSG
            )
        }

    }

    override fun onFailure(responseCode: Int, responseMessage: String) {

        baseActivity.hideCustomProgrssDialog()
        Log.d(">>ERROR:",""+responseMessage)


    }

    fun populateFilterList(valueArray:JSONArray){

        var bookAparmentList = MainActivity.apartmentListViewModel.apartmentBookList.value


        for (i in 0 until valueArray.length()){

           var isNeedToAdd = false

            var jsonObject = valueArray.optJSONObject(i)

            var apartmentData = ApartmentData(
                jsonObject.optString("id"),
                jsonObject.optInt("bedrooms"),
                jsonObject.optString("name"),
                jsonObject.optDouble("latitude"),
                jsonObject.optDouble("longitude"),
                getDistance(
                    jsonObject.optDouble("latitude"),
                    jsonObject.optDouble("longitude")
                )
            )


            if(apartmentData.bedrooms.toString() == bed){

                // bed match
                isNeedToAdd = true

                if(bookAparmentList != null) {

                    for (j in 0 until bookAparmentList.size) {

                        if(bed == bookAparmentList.get(j).bedrooms.toString()){

                            if(MainActivity.getTimeMilesFromDate(ed_fromDate.text.toString()) <
                                bookAparmentList.get(j).toDate!!
                            ){

                                if(apartmentData.id == bookAparmentList.get(j).id) {
                                    isNeedToAdd = false
                                }
                            }
                        }

                    }
                }


            }

            //---ADD to List
            if(isNeedToAdd){
                listApartments.add(apartmentData)
            }

        }


        // Sort list

        Collections.sort(listApartments, object : Comparator<ApartmentData> {
            override fun compare(one: ApartmentData?, other: ApartmentData?): Int {
                return one?.diatance?.compareTo(other?.diatance!!)!!
            }
        })

        adapter.notifyDataSetChanged()

        if(listApartments.size == 0){
            Toast.makeText(context,"No apartment found",Toast.LENGTH_SHORT).show()
        }
    }


    private fun getDistance(latitude:Double, longitude:Double):Float{

        var fromLatLng = Location("FromLocation")
        fromLatLng.latitude = 59.329440
        fromLatLng.longitude = 18.069124

        var toLocation = Location("ToLocation")
        toLocation.latitude = latitude
        toLocation.longitude = longitude

        return fromLatLng.distanceTo(toLocation)
    }


    fun selectDate(editText: EditText, value:String) {


        val dateFormatterShow = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        var fromTime:Long = 0
        var toTime:Long = 0

        val newCalendar = Calendar.getInstance()
        if (value == "FROM") {
            newCalendar.add(Calendar.DATE, 1)// after 1 DAY from today
            fromTime = newCalendar.getTimeInMillis()
        }
        if(value == "TO"){
            newCalendar.add(Calendar.DATE, 2)// after 2 DAY from today
            toTime = newCalendar.getTimeInMillis()
        }

        val datePickerDialog =
            context?.let {
                DatePickerDialog(it, R.style.datepicker, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    editText.setText(dateFormatterShow.format(newDate.time))


                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
            }

        if (value == "FROM")
            datePickerDialog?.datePicker?.minDate = fromTime//System.currentTimeMillis() + TimeUnit.DAYS.toMillis(180)
        else if (value == "TO")
            datePickerDialog?.datePicker?.minDate = toTime
        else {

        }

        datePickerDialog?.show()


    }


}
