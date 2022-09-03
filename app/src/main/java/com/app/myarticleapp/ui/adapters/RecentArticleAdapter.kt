package com.app.myarticleapp.ui.adapters


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.myarticleapp.R
import com.app.myarticleapp.apiSource.responseEntity.Result
import com.app.myarticleapp.databinding.RecentArticleItemsBinding
import com.bumptech.glide.Glide


class RecentArticleAdapter (private  val listItem: List<Result>, listener: OnItemClickListener):
    RecyclerView.Adapter<RecentArticleAdapter.WeatherViewModel>(){

    private var listener: OnItemClickListener? = null

    init {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewModel {
        val binding = RecentArticleItemsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewModel, position: Int) {
        with(holder){
            with(listItem[position]){
                binding.title.text = title
                binding.source.text = source
                binding.date.text = publishedDate
                binding.byLine.text = byline

                media.forEach { media ->
                     media.mediaMetadata.let {image ->
                        try {
                            Glide.with(itemView.context)
                                .load(image[2].url)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(binding.shapeableImageView)
                        }catch (ex: Exception){
                            Log.i("out of bound", "${ex.printStackTrace()}")
                        }
                    }

                }

                itemView.setOnClickListener {
                    listener?.onItemClick(position, listItem[position])
                }
            }
        }
    }

    override fun getItemCount() = 5

    interface OnItemClickListener{
        fun onItemClick(position: Int, item: Result)

    }

    class WeatherViewModel(val binding: RecentArticleItemsBinding):
        ViewHolder(binding.root)

}