package com.ubaya.kelompok3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.kelompok3.databinding.ActivityCreateCerbung1Binding
import com.ubaya.kelompok3.databinding.ActivityCreateCerbung2Binding

class CreateCerbung2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCerbung2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCerbung2Binding.inflate(layoutInflater)
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

        //isi ke radiobutton dan textbox
        if (access == "Public") {
            binding.radioPublic.isChecked = true
        }
        else {
            binding.radioRestricted.isChecked = true
        }
        binding.txtCreateParagraph.setText(paragraph)

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

        binding.txtCreateParagraph.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var charaCount = s?.length ?: 0
                binding.txtCharacterCount.text = "(" + charaCount + " of 70 characters)"
            }
        })

        binding.btnPrev2.setOnClickListener {
            var idradio = binding.groupAccess.checkedRadioButtonId
            if (binding.radioRestricted.id == idradio) {
                access = "Restricted"
            }
            else {
                access = "Public"
            }

            val paragraph = binding.txtCreateParagraph.text.toString()

            val intent = Intent(this, CreateCerbung1Activity::class.java)
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

        binding.btnNext2.setOnClickListener {
            if (binding.txtCreateParagraph.text.isEmpty()) {
                Toast.makeText(this, "Please fill all the data needed", Toast.LENGTH_SHORT).show()
            }
            else {
                var idradio = binding.groupAccess.checkedRadioButtonId
                if (binding.radioRestricted.id == idradio) {
                    access = "Restricted"
                }
                else {
                    access = "Public"
                }

                val paragraph = binding.txtCreateParagraph.text.toString()

                val intent = Intent(this, CreateCerbung3Activity::class.java)
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
    }
}