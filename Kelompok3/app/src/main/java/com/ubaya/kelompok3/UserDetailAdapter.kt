package com.ubaya.kelompok3

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ubaya.kelompok3.databinding.UserDetailItemBinding
import org.json.JSONObject

class UserDetailAdapter(var totalCerbungsArray:ArrayList<Cerbung>, var cerbungs:ArrayList<Cerbung>, val username:String): RecyclerView.Adapter<UserDetailAdapter.UserDetailViewHolder>() {
    class UserDetailViewHolder(val binding: UserDetailItemBinding): RecyclerView.ViewHolder(binding.root)

    private var paragrafs:ArrayList<Paragraf> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailViewHolder {
        val binding = UserDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserDetailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return totalCerbungsArray.size
    }

    override fun onBindViewHolder(holder: UserDetailViewHolder, position: Int) {
        with(holder.binding) {
            var url = totalCerbungsArray[position].url
            val builder = Picasso.Builder(holder.itemView.context)
            builder.listener { picasso, uri, exception ->
                exception.printStackTrace()
            }
            Picasso.get().load(url).into(imgCerbungUser)

            val q = Volley.newRequestQueue(holder.itemView.context)
            val url2 = "https://ubaya.me/native/160821016/get_latest_update.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url2,
                {
                    Log.d("apisuccess", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK") {
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<Paragraf>>() { }.type
                        paragrafs = Gson().fromJson(data.toString(), sType) as ArrayList<Paragraf>

                        txtUpdate.text = "Last update: " + paragrafs[0].tanggal
                    }
                },
                { Log.e("apierror", it.printStackTrace().toString()) }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["idcerbung"] = totalCerbungsArray[position].idcerbung.toString()
                    return params
                }
            }

            q.add(stringRequest)


            txtTitle.text = totalCerbungsArray[position].title
            txtLikes.text = totalCerbungsArray[position].jumlah_like.toString()

            container.setOnClickListener {
                val intent = Intent(it.context, ReadCerbungActivity::class.java)
                intent.putExtra(CerbungHomeAdapter.KEY_ID_CERBUNG, totalCerbungsArray[position].idcerbung)
                intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)

                val gsoncerbung = Gson()
                val arrayCerbung = gsoncerbung.toJson(cerbungs)
                intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)

                it.context.startActivity(intent)
            }
        }
    }
}