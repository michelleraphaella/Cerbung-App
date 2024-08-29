package com.ubaya.kelompok3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.kelompok3.databinding.ActivityFollowingBinding
import com.ubaya.kelompok3.databinding.ActivityHomeBinding
import org.json.JSONObject

class FollowingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFollowingBinding
    private var followedcerbungs:ArrayList<Cerbung> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intent untuk simpan user yang sedang login
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

        //selected button
        binding.bottomNav.selectedItemId = R.id.itemFollowing

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
    }

    fun updateList() {
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

        if (usernamelogin != null) {
            val lm = LinearLayoutManager(this)
            binding.recyclerViewFollowing.layoutManager = lm
            binding.recyclerViewFollowing.setHasFixedSize(true)
            binding.recyclerViewFollowing.adapter = FollowingAdapter(cerbungs, followedcerbungs, usernamelogin)
        }
        else
        {
            Log.e("Username Null", "usernamelogin is null")
        }
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerViewFollowing.adapter?.notifyDataSetChanged()

        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/get_follow_cerbung.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    binding.txtEmpty.visibility = View.INVISIBLE
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Cerbung>>() { }.type
                    followedcerbungs = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>
                    updateList()
                }
            },
            { Log.e("apierror", it.printStackTrace().toString())
                binding.txtEmpty.visibility = View.VISIBLE
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = usernamelogin.toString()
                return params
            }
        }

        q.add(stringRequest)
    }
}