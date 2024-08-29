package com.ubaya.kelompok3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ubaya.kelompok3.databinding.ActivityUserDetailBinding
import org.json.JSONObject

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private var users:ArrayList<User> = arrayListOf()
    private var latestPostArray:ArrayList<Paragraf> = arrayListOf()
    private var totalCerbungsLikes:ArrayList<Cerbung> = arrayListOf()
    private var totalCerbungsArray:ArrayList<Cerbung> = arrayListOf()

    private var totalLikes = ""
    private var totalCerbungs = ""
    private var latestPost = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usernameClicked = intent.getStringExtra(UsersAdapter.USERNAME_CLICKED)
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

        binding.txtWhoseCerbungs.text = usernameClicked + "'s Cerbungs"

        binding.recyclerViewUserDetail.adapter?.notifyDataSetChanged()

        //selected button
        binding.bottomNav.selectedItemId = R.id.itemUsers

        //kalau button diklik, maka halaman ikut ganti juga
        binding.bottomNav.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.itemHome -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, usernamelogin)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemCreate -> {
                    val intent = Intent(this, CreateCerbung1Activity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, usernamelogin)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemsPrefs -> {
                    val intent = Intent(this, PrefsActivity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, usernamelogin)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemFollowing -> {
                    val intent = Intent(this, FollowingActivity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, usernamelogin)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemUsers -> {
                    val intent = Intent(this, UsersActivity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, usernamelogin)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/get_user_detail.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<User>>() { }.type
                    users = Gson().fromJson(data.toString(), sType) as ArrayList<User>

                    setupUI()
                    getUserDetails()
                }
            },
            { Log.e("apierror", it.printStackTrace().toString()) }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = usernameClicked.toString()
                return params
            }
        }

        q.add(stringRequest)
    }

    private fun getUserDetails() {
        val usernameClicked = intent.getStringExtra(UsersAdapter.USERNAME_CLICKED)

        //get total likes of user
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/get_total_likes.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Cerbung>>() { }.type
                    totalCerbungsLikes = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>

                    for (i in 0 until totalCerbungsLikes.size) {
                        totalLikes = totalCerbungsLikes[i].jumlah_like.toString() + " Likes"
                        break
                    }
                }
                else if (obj.getString("result") == "ERROR") {
                    totalLikes = "0 Like"
                }

                binding.txtTotalLikes.text = totalLikes + " |"
            },
            { Log.e("apierror", it.printStackTrace().toString()) }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = usernameClicked.toString()
                return params
            }
        }

        q.add(stringRequest)


        //get total cerbungs created by user
        val q2 = Volley.newRequestQueue(this)
        val url2 = "https://ubaya.me/native/160821016/get_total_cerbungs.php"
        val stringRequest2 = object : StringRequest(
            Request.Method.POST,
            url2,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Cerbung>>() { }.type
                    totalCerbungsArray = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>

                    totalCerbungs = totalCerbungsArray.size.toString()

                    binding.txtTotalCerbungs.text = totalCerbungs + " Cerbungs Created"

                    updateList(totalCerbungsArray)
                }
            },
            { Log.e("apierror", it.printStackTrace().toString()) }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = usernameClicked.toString()
                return params
            }
        }

        q2.add(stringRequest2)


        //get latest post date
        val q3 = Volley.newRequestQueue(this)
        val url3 = "https://ubaya.me/native/160821016/get_latest_post.php"
        val stringRequest3 = object : StringRequest(
            Request.Method.POST,
            url3,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Paragraf>>() { }.type
                    latestPostArray = Gson().fromJson(data.toString(), sType) as ArrayList<Paragraf>

                    for (i in 0 until latestPostArray.size) {
                        latestPost = latestPostArray[i].tanggal
                        break
                    }

                    binding.txtPost.text = "Latest post: ${latestPost ?: "User hasn't posted yet"}"
                }
            },
            { Log.e("apierror", it.printStackTrace().toString()) }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = usernameClicked.toString()
                return params
            }
        }

        q3.add(stringRequest3)
    }

    fun updateList(totalCerbungsArray: ArrayList<Cerbung>) {
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

        if (usernamelogin != null) {
            val lm = LinearLayoutManager(this)
            binding.recyclerViewUserDetail.layoutManager = lm
            binding.recyclerViewUserDetail.setHasFixedSize(true)
            binding.recyclerViewUserDetail.adapter = UserDetailAdapter(totalCerbungsArray, cerbungs, usernamelogin)
        }
        else
        {
            Log.e("Username Null", "usernamelogin is null")
        }
    }

    private fun setupUI() {
        val usernameClicked = intent.getStringExtra(UsersAdapter.USERNAME_CLICKED)

        //display to the respective textviews
        for (i in 0 until users.size) {
            if (users[i].username == usernameClicked) {
                val url = users[i].url
                val builder = Picasso.Builder(this)
                builder.listener { picasso, uri, exception ->
                    exception.printStackTrace() }
                builder.build().load(url).into(binding.imgUserDetail)

                binding.txtUsername.text = users[i].username

                break
            }
        }
    }
}