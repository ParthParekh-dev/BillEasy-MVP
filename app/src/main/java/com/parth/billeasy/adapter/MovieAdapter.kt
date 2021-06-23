package com.parth.billeasy.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.squareup.picasso.Picasso
import com.parth.billeasy.R
import com.parth.billeasy.model.VideoList
import java.util.*
import kotlinx.android.synthetic.main.video_item.view.*


class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>, Filterable {

    var data: ArrayList<VideoList>
    var dataSearch: ArrayList<VideoList>
    var context: Context
    var itemClickListener: OnItemClickListener? = null

    constructor(context: Context) : super() {

        this.context = context
        this.data = ArrayList()
        this.dataSearch = ArrayList()

    }

    fun setDataValue(data: ArrayList<VideoList>?) {
        dataSearch.clear()
        this.data.clear()
        this.data.addAll(data!!)
        this.dataSearch.addAll(data)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charText = charSequence.toString().toLowerCase(Locale.getDefault())
                data.clear()
                if (charText.length == 0) {
                    data.addAll(dataSearch)
                } else {
                    for (video in dataSearch) {
                        if (charText.length != 0 && video.title.toLowerCase(Locale.getDefault()).contains(charText)) {
                            data.add(video)
                        }
                    }
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = data
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                @Suppress("UNCHECKED_CAST")
                data = filterResults.values as ArrayList<VideoList>
                notifyDataSetChanged()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.movieTitle.text = data.get(position).title
        holder.itemView.releaseDate.text = "Release date -"+data.get(position).release_date
        holder.itemView.movieRating.text = data.get(position).rating
//        holder.itemView.textViewDuration.text = data.get(position).duration
        if (data.get(position).img_url.trim().length > 0) {
            Picasso.get().load(data.get(position).img_url)
                .into(holder.itemView.movieThumbnail)
        }

//        notifyDataSetChanged()


    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ViewHolder : RecyclerView.ViewHolder {

        constructor(itemView: View) : super(itemView) {
            itemView.setOnClickListener {

                itemClickListener?.onItemClicked(data.get(adapterPosition))
            }

        }

    }

    interface OnItemClickListener {
        fun onItemClicked(video: VideoList)
    }

}