package com.app.myarticleapp.ui.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.myarticleapp.apiSource.responseEntity.Duration
import com.app.myarticleapp.apiSource.responseEntity.Result
import com.app.myarticleapp.databinding.ArticleListItemBinding
import com.app.myarticleapp.databinding.DurationItemListBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DurationAdapter (private  val listItem: List<Duration>, listener: OnItemClickListener):
    RecyclerView.Adapter<DurationAdapter.WeatherViewModel>(){

    private var listener: OnItemClickListener? = null

    init {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewModel {
        val binding = DurationItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewModel, position: Int) {
        with(holder){
            with(listItem[position]){

                binding.duration.text = StringBuilder().append(period).append("day(s)")

                itemView.setOnClickListener {
                    listener?.onItemClick(position, listItem[position])
                }
            }
        }
    }

    override fun getItemCount() = listItem.size

    interface OnItemClickListener{
        fun onItemClick(position: Int, item: Duration)

    }

    class WeatherViewModel(val binding: DurationItemListBinding):
        ViewHolder(binding.root)

}