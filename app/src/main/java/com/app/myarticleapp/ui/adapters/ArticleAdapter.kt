package com.app.myarticleapp.ui.adapters


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.myarticleapp.apiSource.responseEntity.Result
import com.app.myarticleapp.databinding.ArticleListItemBinding
import com.app.myarticleapp.utils.dateFormater.FormatDate.getFormattedFullDateString
import com.bumptech.glide.Glide
import java.lang.Exception


class ArticleAdapter (private  val listItem: List<Result>, listener: OnItemClickListener):
    RecyclerView.Adapter<ArticleAdapter.WeatherViewModel>(){

    private var listener: OnItemClickListener? = null

    init {
        this.listener = listener
    }

    private val items = ArrayList<Result>()

    @SuppressLint("NotifyDataSetChanged")
    fun filter(text: String) {
        var text = text
        items.clear()
        if (text.isEmpty()) {
            items.addAll(listItem)
        } else {
            text = text.lowercase()
            for (item in listItem) {
                if (item.title.lowercase().contains(text)
                ) {
                    items.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewModel {
        val binding = ArticleListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewModel, position: Int) {
        with(holder){
            with(items[position]){
                binding.title.text = title
                binding.source.text = source
                binding.date.text = publishedDate

                media.forEach { media ->
                     media.mediaMetadata.let {image ->
                        try {
                            Glide.with(itemView.context)
                                .load(image[2].url)
                                .into(binding.shapeableImageView)
                        }catch (ex: Exception){
                            Log.i("out of bound", "${ex.printStackTrace()}")
                        }
                    }

                }

                itemView.setOnClickListener {
                    listener?.onItemClick(position, items[position])
                }
            }
        }
    }

    override fun getItemCount() = items.size

    interface OnItemClickListener{
        fun onItemClick(position: Int, item: Result)

    }

    class WeatherViewModel(val binding: ArticleListItemBinding):
        ViewHolder(binding.root)

}