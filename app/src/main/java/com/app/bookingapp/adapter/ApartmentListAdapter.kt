package com.app.bookingapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.bookingapp.R
import com.app.bookingapp.model.ApartmentData


class ApartmentListAdapter(context:Context, offerList: List<ApartmentData>) :  RecyclerView.Adapter<ApartmentListAdapter.MyViewHolder>(),
    Filterable {
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    listOfApartments = arrayList
                } else {

                    val filteredList = java.util.ArrayList<ApartmentData>()

                    for (aparmentsData in arrayList) {

                        if (aparmentsData.bedrooms.toString()!!.toLowerCase().equals(charString)) {

                            filteredList.add(aparmentsData)
                        }
                    }

                    listOfApartments = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = listOfApartments
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: Filter.FilterResults
            ) {
                listOfApartments = filterResults.values as java.util.ArrayList<ApartmentData>
                notifyDataSetChanged()
            }
        }
    }


    var listOfApartments = offerList
    var arrayList = offerList
    var ctx = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view

            // Grid Mode
           var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recyler_aparments, parent, false)

        var viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listOfApartments.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(position<listOfApartments.size) {
            var data = listOfApartments.get(position)



            holder.tvName.setText(data.name)
            holder.tvID.setText("ID: ${data.id}")
            holder.tvBedRooms.setText("Bedroom: ${data.bedrooms}")
            holder.tvLatitude.setText("Lat: ${data.latitude}")
            holder.tvLongitude.setText("Lng: ${data.longitude}")
            holder.tvDistance.setText("Distance: ${data.diatance} (meters)")

            holder.lay_row.setOnClickListener {

                var bundle = Bundle()
                bundle.putSerializable("iApartmentData", data)
               it.findNavController().navigate(R.id.action_homeFragment_to_bookFragment, bundle)

            }


        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvID = itemView.findViewById<TextView>(R.id.tvID)
        var tvName = itemView.findViewById<TextView>(R.id.tvName)
        var tvBedRooms = itemView.findViewById<TextView>(R.id.tvBedRooms)
        var tvLatitude = itemView.findViewById<TextView>(R.id.tvLatitude)
        var tvLongitude = itemView.findViewById<TextView>(R.id.tvLongitude)
        var tvDistance = itemView.findViewById<TextView>(R.id.tvDistance)

        var lay_row = itemView.findViewById<ConstraintLayout>(R.id.lay_row)


    }

    fun TextView.showStrikeThrough(show: Boolean) {
        paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }


}