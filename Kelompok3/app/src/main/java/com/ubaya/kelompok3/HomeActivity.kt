package com.ubaya.kelompok3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ubaya.kelompok3.databinding.ActivityHomeBinding
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var cerbungs:ArrayList<Cerbung> = arrayListOf()
    private var users:ArrayList<User> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intent untuk simpan user yang sedang login
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        //selected button
        binding.bottomNav.selectedItemId = R.id.itemHome

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

        if (usernamelogin != null) {
            val lm = LinearLayoutManager(this)
            binding.recyclerViewHome.layoutManager = lm
            binding.recyclerViewHome.setHasFixedSize(true)
            binding.recyclerViewHome.adapter = CerbungHomeAdapter(cerbungs, usernamelogin)
        }
        else
        {
            Log.e("Username Null", "usernamelogin is null")
        }
    }

    private fun setupTheme(usernamelogin: String?) {
        //display to the respective textviews
        for (i in 0 until users.size) {
            if (users[i].username == usernamelogin) {
                if (users[i].theme == 0) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                break
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)
        binding.recyclerViewHome.adapter?.notifyDataSetChanged()

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/get_cerbung.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it.toString())
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Cerbung>>() { }.type
                    cerbungs = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>
                    Log.d("apiresult", cerbungs[1].toString())
                    updateList()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)

        val q2 = Volley.newRequestQueue(this)
        val url2 = "https://ubaya.me/native/160821016/get_user.php"
        var stringRequest2 = StringRequest(
            Request.Method.POST, url2,
            Response.Listener<String> {
                Log.d("apiresult", it.toString())
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<User>>() { }.type
                    users = Gson().fromJson(data.toString(), sType) as ArrayList<User>
                    Log.d("apiresult", users[1].toString())

                    setupTheme(usernamelogin)
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q2.add(stringRequest2)
    }
}