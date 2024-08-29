package com.ubaya.kelompok3

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.kelompok3.databinding.ActivityCreateCerbung1Binding
import com.ubaya.kelompok3.databinding.ActivityCreateCerbung3Binding
import org.json.JSONObject

class CreateCerbung3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCerbung3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCerbung3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //ambil intent, ketika user sudah mengisikan textbox, maka akan ditampilkan ke textbox
        val username = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)
        val title = intent.getStringExtra(CreateCerbung1Activity.KEY_TITLE)
        val description = intent.getStringExtra(CreateCerbung1Activity.KEY_DESCRIPTION)
        val img = intent.getStringExtra(CreateCerbung1Activity.KEY_URL)
        val genreIndex = intent.getIntExtra(CreateCerbung1Activity.KEY_GENRE, 0)
        var access = intent.getStringExtra(CreateCerbung1Activity.KEY_ACCESS)
        val paragraph = intent.getStringExtra(CreateCerbung1Activity.KEY_PARAGRAPH)

        val gsongenre = Gson()
        val arrayGenre = intent.getStringExtra(CreateCerbung1Activity.ARRAY_GENRE)
        val type = object : TypeToken<ArrayList<Genre>>() {}.type
        val genres = gsongenre.fromJson<ArrayList<Genre>>(arrayGenre, type)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type2 = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type2)

        binding.txtPreviewTitle.text = title
        binding.txtPreviewDescription.text = description
        binding.txtFirstParagraph.text = paragraph
        binding.txtPreviewAccess.text = access
        binding.txtPreviewGenre.text = genres[genreIndex].name

        //selected button
        binding.bottomNav.selectedItemId = R.id.itemCreate

        //kalau button diklik, maka halaman ikut ganti juga
        binding.bottomNav.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.itemHome -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemFollowing -> {
                    val intent = Intent(this, FollowingActivity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemsPrefs -> {
                    val intent = Intent(this, PrefsActivity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)
                    val gsoncerbung = Gson()
                    val arrayCerbung = gsoncerbung.toJson(cerbungs)
                    intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.itemUsers -> {
                    val intent = Intent(this, UsersActivity::class.java)
                    intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)
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

        binding.btnPublish.setOnClickListener{
            if (binding.checkBoxAgree.isChecked) {
                val dialog = AlertDialog.Builder(it.context)
                dialog.setMessage("Publish cerbung \"$title\"?")
                dialog.setPositiveButton("PUBLISH", DialogInterface.OnClickListener { dialogInterface, i ->
                    val q = Volley.newRequestQueue(this)
                    val url = "https://ubaya.me/native/160821016/insert_cerbung.php"
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        {
                            Log.d("apisuccess", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK") {
                                val idcerbung = obj.getInt("idcerbung")
                                insertParagraf(idcerbung)
                                Toast.makeText(this, "Your new cerbung is successfully added.", Toast.LENGTH_SHORT).show()

                                //kembali ke home
                                val intent = Intent(this, HomeActivity::class.java)
                                intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)
                                startActivity(intent)
                                finish()
                            }
                        },
                        {
                            Log.e("apierror", it.printStackTrace().toString())
                            Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                        }) {
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["title"] = title.toString()
                            params["description"] = description.toString()
                            params["access"] = access.toString()
                            params["url"] = img.toString()
                            params["username"] = username.toString()
                            params["idgenre"] = genres[genreIndex].idgenre.toString()
                            return params
                        }
                    }

                    q.add(stringRequest)
                })
                dialog.setNegativeButton("CANCEL", null)
                dialog.create().show()
            }
            else {
                Toast.makeText(this, "You haven't agreed to the rules and policies agreement", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnPrev3.setOnClickListener {
            val intent = Intent(this, CreateCerbung2Activity::class.java)
            intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)
            intent.putExtra(CreateCerbung1Activity.KEY_TITLE, title)
            intent.putExtra(CreateCerbung1Activity.KEY_DESCRIPTION, description)
            intent.putExtra(CreateCerbung1Activity.KEY_URL, img)
            intent.putExtra(CreateCerbung1Activity.KEY_GENRE, genreIndex)
            intent.putExtra(CreateCerbung1Activity.KEY_ACCESS, access)
            intent.putExtra(CreateCerbung1Activity.KEY_PARAGRAPH, paragraph)

            val arrayGenre = gsongenre.toJson(genres)
            intent.putExtra(CreateCerbung1Activity.ARRAY_GENRE, arrayGenre)

            val arrayCerbung = gsoncerbung.toJson(cerbungs)
            intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)

            startActivity(intent)
            finish()
        }
    }

    private fun insertParagraf(idcerbung: Int) {
        val username = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)
        val paragraph = intent.getStringExtra(CreateCerbung1Activity.KEY_PARAGRAPH)

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/insert_paragraf.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("apisuccess", it)
            },
            {
                Log.e("apierror", it.printStackTrace().toString())
                Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["paragraf"] = paragraph.toString()
                params["idcerbung"] = idcerbung.toString()
                params["username"] = username.toString()
                return params
            }
        }

        q.add(stringRequest)
    }
}