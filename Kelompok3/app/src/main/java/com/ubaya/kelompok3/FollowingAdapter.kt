package com.ubaya.kelompok3

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.ubaya.kelompok3.databinding.FollowingItemBinding

class FollowingAdapter(var cerbungs:ArrayList<Cerbung>, var followedcerbungs:ArrayList<Cerbung>, val username:String): RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
    class FollowingViewHolder(val binding: FollowingItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = FollowingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followedcerbungs.size
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        with(holder.binding) {
            var url = followedcerbungs[position].url
            val builder = Picasso.Builder(holder.itemView.context)
            builder.listener { picasso, uri, exception ->
                exception.printStackTrace()
            }
            Picasso.get().load(url).into(imgCerbungFollow)

            txtTitle.text = followedcerbungs[position].title
            txtWriter.text = "by " + followedcerbungs[position].username
            txtUpdate.text = "Last update: " + followedcerbungs[position].tanggal


            cardView.setOnClickListener {
                val intent = Intent(it.context, ReadCerbungActivity::class.java)
                intent.putExtra(CerbungHomeAdapter.KEY_ID_CERBUNG, followedcerbungs[position].idcerbung)
                intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)

                val gsoncerbung = Gson()
                val arrayCerbung = gsoncerbung.toJson(cerbungs)
                intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)

                intent.putExtra(CerbungHomeAdapter.KEY_POSITION, position)
                it.context.startActivity(intent)
            }
        }
    }
}