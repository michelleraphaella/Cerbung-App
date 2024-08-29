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
import com.ubaya.kelompok3.databinding.UserItemBinding
import org.json.JSONObject

class UsersAdapter(var users:ArrayList<User>, var cerbungs:ArrayList<Cerbung>, var cerbungs2:ArrayList<Cerbung>, val username:String): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    class UsersViewHolder(val binding: UserItemBinding): RecyclerView.ViewHolder(binding.root)
    private var totalLikes = ""

    companion object {
        val USERNAME_CLICKED = "USERNAME"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        with(holder.binding) {
            var url = users[position].url
            val builder = Picasso.Builder(holder.itemView.context)
            builder.listener { picasso, uri, exception ->
                exception.printStackTrace()
            }
            Picasso.get().load(url).into(imgUser)

            val q = Volley.newRequestQueue(holder.itemView.context)
            val url2 = "https://ubaya.me/native/160821016/get_total_likes.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url2,
                {
                    Log.d("apisuccess", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK") {
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<Cerbung>>() { }.type
                        cerbungs2 = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>

                        for (i in 0 until cerbungs2.size) {
                            if (cerbungs2[i].username == users[position].username) {
                                totalLikes = cerbungs2[i].jumlah_like.toString() + " likes"
                                break
                            }
                        }
                    }
                    else if (obj.getString("result") == "ERROR") {
                        totalLikes = "0 likes"
                    }

                    txtTotalLikes.text = totalLikes
                },
                { Log.e("apierror", it.printStackTrace().toString()) }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["username"] = users[position].username
                    return params
                }
            }

            q.add(stringRequest)

            txtUsername.text = users[position].username
            txtJoined.text = "since " + users[position].tanggal

            container.setOnClickListener {
                val intent = Intent(it.context, UserDetailActivity::class.java)
                intent.putExtra(USERNAME_CLICKED, users[position].username)

                val gsoncerbung = Gson()
                val arrayCerbung = gsoncerbung.toJson(cerbungs)
                intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)

                intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)

                it.context.startActivity(intent)
            }
        }
    }
}