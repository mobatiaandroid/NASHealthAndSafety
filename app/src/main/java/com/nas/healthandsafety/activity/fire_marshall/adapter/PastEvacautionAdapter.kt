package com.nas.healthandsafety.activity.fire_marshall.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.home.model.EvacuationStatusResponseModel
import java.text.SimpleDateFormat
import java.util.Locale


class PastEvacautionAdapter(
    val context: Context,
    var evacuationsList: ArrayList<EvacuationStatusResponseModel.Data>
) :
    RecyclerView.Adapter<PastEvacautionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(context)
            .inflate(R.layout.adapter_evacuation_item, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.evacuationCauseTextView.text = evacuationsList[position].evacuate_type
        var startTime = ""
        var endTime = ""

        var inputString = evacuationsList[position].evacuate_start
        var inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        var parsedString = inputFormat.parse(inputString!!)
        var outputString = outputFormat.format(parsedString!!)

        holder.evacuationDate.text = outputString

        inputString = evacuationsList[position].evacuate_start
        inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        parsedString = inputFormat.parse(inputString)
        outputString = outputFormat.format(parsedString!!)
        startTime = outputString

        holder.evacuationStartTextView.text = outputString

        inputString = evacuationsList[position].evacuate_end
        inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        parsedString = inputFormat.parse(inputString)
        outputString = outputFormat.format(parsedString!!)
        endTime = outputString

        holder.evacuationEndTextView.text = outputString


        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val date1 = dateFormat.parse(evacuationsList[position].evacuate_start!!)
        val date2 = dateFormat.parse(evacuationsList[position].evacuate_end!!)

        val diffInMilliSeconds = date2!!.time - date1!!.time
        val diffInSeconds = diffInMilliSeconds / 1000
        val diffInMinutes = diffInSeconds / 60
        val diffInHours = diffInMinutes / 60

        if (diffInMinutes < 0) {
            holder.evacuationTimeTaken.text = "Ongoing"
        } else {
            holder.evacuationTimeTaken.text = "$diffInMinutes min"
        }


        /*Log.e("designtion",employee_array.get(position).designation.toString())*/


    }

    override fun getItemCount(): Int {
        return evacuationsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var evacuationCauseTextView = itemView.findViewById<TextView>(R.id.evacuationCauseTextView)
        var evacuationDate = itemView.findViewById<TextView>(R.id.evacuationOnTextView)
        var evacuationStartTextView = itemView.findViewById<TextView>(R.id.evacuationStartTextView)
        var evacuationEndTextView = itemView.findViewById<TextView>(R.id.evacuationEndTextView)
        var evacuationTimeTaken = itemView.findViewById<TextView>(R.id.evacuationTimeTextView)


    }
}


