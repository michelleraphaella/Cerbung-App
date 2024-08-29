package com.ubaya.kelompok3

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ubaya.kelompok3.databinding.HomeItemBinding
import org.json.JSONObject

class CerbungHomeAdapter(var cerbungs:ArrayList<Cerbung>, val username:String): RecyclerView.Adapter<CerbungHomeAdapter.CerbungHomeViewHolder>() {
    class CerbungHomeViewHolder(val binding: HomeItemBinding): RecyclerView.ViewHolder(binding.root)

    companion object {
        val KEY_ID_CERBUNG = "IDCERBUNG"
        val KEY_POSITION = "POSITIONCERBUNG"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CerbungHomeViewHolder {
        val binding = HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CerbungHomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cerbungs.size
    }

    override fun onBindViewHolder(holder: CerbungHomeViewHolder, position: Int) {
        with(holder.binding) {
            var url = cerbungs[position].url
            val builder = Picasso.Builder(holder.itemView.context)
            builder.listener { picasso, uri, exception ->
                exception.printStackTrace()
            }
            Picasso.get().load(url).into(imgCerbung)

            txtTitle.text = cerbungs[position].title
            txtWriter.text = "by " + cerbungs[position].username
            txtParagraph.text = cerbungs[position].jumlah_paragraf.toString()
            txtLike.text = cerbungs[position].jumlah_like.toString()
            txtDescription.text = cerbungs[position].description


            btnRead.setOnClickListener {
                val intent = Intent(it.context, ReadCerbungActivity::class.java)
                intent.putExtra(KEY_ID_CERBUNG, cerbungs[position].idcerbung)
                intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)

                val gsoncerbung = Gson()
                val arrayCerbung = gsoncerbung.toJson(cerbungs)
                intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)

                it.context.startActivity(intent)
            }
        }
    }
}