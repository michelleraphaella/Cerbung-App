package com.ubaya.kelompok3

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ubaya.kelompok3.databinding.HomeItemBinding
import com.ubaya.kelompok3.databinding.ReadCerbungItemBinding
import org.json.JSONObject

class ReadCerbungAdapter(var paragrafs:ArrayList<Paragraf>, val idcerbung:Int, val username:String, var likesparagraf:ArrayList<LikeParagraf>): RecyclerView.Adapter<ReadCerbungAdapter.ReadCerbungViewHolder>() {
    class ReadCerbungViewHolder(val binding: ReadCerbungItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadCerbungViewHolder {
        val binding = ReadCerbungItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ReadCerbungViewHolder(binding)
    }

    override fun getItemCount(): Int {
        val jumlahDisplayed = paragrafs.count { it.idcerbung == idcerbung }
        return jumlahDisplayed
    }

    override fun onBindViewHolder(holder: ReadCerbungViewHolder, position: Int) {
        with(holder.binding) {
            val par = paragrafs.filter { it.idcerbung == idcerbung }

            txtReadParagraph.text = par[position].paragraf
            txtReadUser.text = par[position].username

            val q = Volley.newRequestQueue(holder.itemView.context)
            val url = "https://ubaya.me/native/160821016/get_like_paragraf.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                {
                    Log.d("apisuccess", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK") {
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<LikeParagraf>>() { }.type
                        likesparagraf = Gson().fromJson(data.toString(), sType) as ArrayList<LikeParagraf>
                        btnParagrafLike.setImageResource(R.drawable.baseline_thumb_up_alt_24)
                    }
                },
                { Log.e("apierror", it.printStackTrace().toString())
                    btnParagrafLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
                }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["username"] = username
                    params["idparagraf"] = paragrafs[position].idparagraf.toString()
                    params["idcerbung"] = idcerbung.toString()
                    return params
                }
            }

            q.add(stringRequest)

            btnParagrafLike.setOnClickListener {
                val currentIcon = btnParagrafLike.drawable

                if (currentIcon.constantState == ContextCompat.getDrawable(holder.itemView.context, R.drawable.baseline_thumb_up_off_alt_24)?.constantState) {
                    btnParagrafLike.setImageResource(R.drawable.baseline_thumb_up_alt_24)

                    val q = Volley.newRequestQueue(holder.itemView.context)
                    val url = "https://ubaya.me/native/160821016/insert_like_paragraf.php"
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        {
                            Log.d("apisuccess", it)
                        },
                        { Log.e("apierror", it.printStackTrace().toString()) }
                    ) {
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["username"] = username
                            params["idparagraf"] = paragrafs[position].idparagraf.toString()
                            params["idcerbung"] = idcerbung.toString()
                            return params
                        }
                    }

                    q.add(stringRequest)
                }
                else if (currentIcon.constantState == ContextCompat.getDrawable(holder.itemView.context, R.drawable.baseline_thumb_up_alt_24)?.constantState) {
                    btnParagrafLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)

                    val q = Volley.newRequestQueue(holder.itemView.context)
                    val url = "https://ubaya.me/native/160821016/delete_like_paragraf.php"
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        {
                            Log.d("apisuccess", it)
                        },
                        { Log.e("apierror", it.printStackTrace().toString()) }
                    ) {
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["username"] = username
                            params["idparagraf"] = paragrafs[position].idparagraf.toString()
                            params["idcerbung"] = idcerbung.toString()
                            return params
                        }
                    }

                    q.add(stringRequest)
                }
            }
        }
    }
}