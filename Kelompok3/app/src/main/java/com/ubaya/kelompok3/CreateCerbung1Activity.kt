package com.ubaya.kelompok3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.kelompok3.databinding.ActivityCreateCerbung1Binding
import org.json.JSONObject

class CreateCerbung1Activity : AppCompatActivity() {
    private var genres:ArrayList<Genre> = arrayListOf()
    private lateinit var binding: ActivityCreateCerbung1Binding

    companion object {
        val KEY_USERNAME = "username"
        val KEY_TITLE = "title"
        val KEY_DESCRIPTION = "description"
        val KEY_URL = "url"
        val KEY_GENRE = "genre"
        val KEY_ACCESS = "access"
        val KEY_PARAGRAPH = "firstparagraph"
        val ARRAY_GENRE = "arraygenre"
        val ARRAY_CERBUNG = "arraycerbung"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCerbung1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //read genre from database
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/get_genre.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it.toString())
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Genre>>() { }.type
                    genres = Gson().fromJson(data.toString(), sType) as ArrayList<Genre>
                    Log.d("apiresult", genres[1].toString())

                    //masukkan daftar genre ke dalam spinner genre
                    val genreNames = genres.map { genre -> genre.name }
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, genreNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinGenre.adapter = adapter
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)

        //ambil intent, ketika user sudah mengisikan textbox, maka akan ditampilkan ke textbox
        val username = intent.getStringExtra(KEY_USERNAME)
        val title = intent.getStringExtra(KEY_TITLE)
        val description = intent.getStringExtra(KEY_DESCRIPTION)
        val img = intent.getStringExtra(KEY_URL)
        val genreIndex = intent.getIntExtra(KEY_GENRE, 0)
        var access = intent.getStringExtra(KEY_ACCESS)
        val paragraph = intent.getStringExtra(KEY_PARAGRAPH)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

        //isi textbox pake data yg tadi diambil di intent
        binding.txtCreateTitle.setText(title)
        binding.txtCreateDescription.setText(description)
        binding.txtCerbungImage.setText(img)
        binding.spinGenre.setSelection(genreIndex)

        //selected button
        binding.bottomNav.selectedItemId = R.id.itemCreate

        //kalau button diklik, maka halaman ikut ganti juga
        binding.bottomNav.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.itemHome -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra(KEY_USERNAME, username)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemFollowing -> {
                    val intent = Intent(this, FollowingActivity::class.java)
                    intent.putExtra(KEY_USERNAME, username)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemsPrefs -> {
                    val intent = Intent(this, PrefsActivity::class.java)
                    intent.putExtra(KEY_USERNAME, username)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemUsers -> {
                    val intent = Intent(this, UsersActivity::class.java)
                    intent.putExtra(KEY_USERNAME, username)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        binding.btnNext1.setOnClickListener {
            //pengecekan textbox kosong atau tidak
            if (binding.txtCreateTitle.text.isEmpty() || binding.txtCreateDescription.text.isEmpty() || binding.txtCerbungImage.text.isEmpty()){
                Toast.makeText(this, "Please fill all the data needed", Toast.LENGTH_SHORT).show()
            }
            else{
                //jika textbox sudah terisi, maka passing variabel
                val title = binding.txtCreateTitle.text.toString()
                val description = binding.txtCreateDescription.text.toString()
                val img = binding.txtCerbungImage.text.toString()
                val genreIndex = binding.spinGenre.selectedItemPosition

                //passing variabel ke create2
                val intent = Intent(this, CreateCerbung2Activity::class.java)
                intent.putExtra(KEY_USERNAME, username)
                intent.putExtra(KEY_TITLE, title)
                intent.putExtra(KEY_DESCRIPTION, description)
                intent.putExtra(KEY_URL, img)
                intent.putExtra(KEY_GENRE, genreIndex)
                intent.putExtra(KEY_ACCESS, access)
                intent.putExtra(KEY_PARAGRAPH, paragraph)

                val gsongenre = Gson()
                val arrayGenre = gsongenre.toJson(genres)
                intent.putExtra(ARRAY_GENRE, arrayGenre)

                val gsoncerbung = Gson()
                val arrayCerbung = gsoncerbung.toJson(cerbungs)
                intent.putExtra(ARRAY_CERBUNG, arrayCerbung)

                startActivity(intent)
                finish()
            }
        }
    }
}