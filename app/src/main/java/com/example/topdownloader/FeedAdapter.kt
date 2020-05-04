package com.example.topdownloader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ViewHolder(v: View) {
    val textViewName: TextView = v.findViewById(R.id.textViewName)
    val textViewArtist: TextView = v.findViewById(R.id.textViewArtist)
    val textViewSummary: TextView = v.findViewById(R.id.textViewSummary)
    val imageViewLogo: ImageView = v.findViewById(R.id.imageViewLogo)
}

class FeedAdapter(context: Context, private val resource: Int, private val data: List<FeedEntry>) :
    ArrayAdapter<FeedEntry>(context, resource) {
    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            Log.d(TAG, "getView called with null convertView")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            Log.d(TAG, "getView called with a convertView")
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentApp = data[position]

        viewHolder.textViewName.text = currentApp.name
        viewHolder.textViewArtist.text = currentApp.artist
        viewHolder.textViewSummary.text = currentApp.summary
        Picasso.get().load(currentApp.imageURL).into(viewHolder.imageViewLogo)

        return view
    }

    override fun getCount(): Int {
        return data.size
    }
}