package com.ubaya.kelompok3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.kelompok3.databinding.ActivityUsersBinding
import org.json.JSONObject

class UsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsersBinding
    private var users:ArrayList<User> = arrayListOf()
    private var cerbungs2:ArrayList<Cerbung> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intent untuk simpan user yang sedang login
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

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
    }

    fun updateList() {
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

        if (usernamelogin != null) {
            val lm = LinearLayoutManager(this)
            binding.recyclerViewUsers.layoutManager = lm
            binding.recyclerViewUsers.setHasFixedSize(true)
            binding.recyclerViewUsers.adapter = UsersAdapter(users, cerbungs, cerbungs2, usernamelogin)
        }
        else
        {
            Log.e("Username Null", "usernamelogin is null")
        }
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerViewUsers.adapter?.notifyDataSetChanged()

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/get_user.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it.toString())
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<User>>() { }.type
                    users = Gson().fromJson(data.toString(), sType) as ArrayList<User>
                    updateList()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)
    }
}