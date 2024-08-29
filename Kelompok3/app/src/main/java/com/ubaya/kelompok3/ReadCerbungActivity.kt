package com.ubaya.kelompok3

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ubaya.kelompok3.databinding.ActivityReadCerbungBinding
import org.json.JSONObject

class ReadCerbungActivity : AppCompatActivity() {
    private var genres:ArrayList<Genre> = arrayListOf()
    private var paragrafs:ArrayList<Paragraf> = arrayListOf()
    private var likescerbung:ArrayList<LikeCerbung> = arrayListOf()
    private var likesparagraf:ArrayList<LikeParagraf> = arrayListOf()
    private var follows:ArrayList<Follow> = arrayListOf()
    private var requests:ArrayList<Requests> = arrayListOf()
    private lateinit var binding: ActivityReadCerbungBinding
    private var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadCerbungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFollowing.visibility = View.INVISIBLE
        binding.btnRequested.visibility = View.INVISIBLE

        //get the cerbung id and username
        val idcerbung = intent.getIntExtra(CerbungHomeAdapter.KEY_ID_CERBUNG, 0)
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

        Log.d("cerbsfck", cerbungs.toString())
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

                    setupUI(cerbungs, idcerbung, usernamelogin)
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)

        val q2 = Volley.newRequestQueue(this)
        val url2 = "https://ubaya.me/native/160821016/get_like_cerbung.php"
        val stringRequest2 = object : StringRequest(
            Request.Method.POST,
            url2,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<LikeCerbung>>() { }.type
                    likescerbung = Gson().fromJson(data.toString(), sType) as ArrayList<LikeCerbung>
                    isLiked = true
                    updateLikeButton(isLiked)
                }
            },
            { Log.e("apierror", it.printStackTrace().toString())
                isLiked = false
                updateLikeButton(isLiked)
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = usernamelogin.toString()
                params["idcerbung"] = idcerbung.toString()
                return params
            }
        }

        q2.add(stringRequest2)


        val q3 = Volley.newRequestQueue(this)
        val url3 = "https://ubaya.me/native/160821016/get_follow.php"
        val stringRequest3 = object : StringRequest(
            Request.Method.POST,
            url3,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Follow>>() { }.type
                    follows = Gson().fromJson(data.toString(), sType) as ArrayList<Follow>

                    binding.btnFollow.visibility = View.INVISIBLE
                    binding.btnFollowing.visibility = View.VISIBLE
                }
            },
            { Log.e("apierror", it.printStackTrace().toString())
                binding.btnFollow.visibility = View.VISIBLE
                binding.btnFollowing.visibility = View.INVISIBLE
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = usernamelogin.toString()
                params["idcerbung"] = idcerbung.toString()
                return params
            }
        }

        q3.add(stringRequest3)


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
                else -> false
            }
        }

        binding.txtContinueParagraph.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var charaCount = s?.length ?: 0
                binding.txtCharacterCount.text = "(" + charaCount + " of 70 characters)"
            }
        })

        binding.btnSubmit.setOnClickListener {
            val paragraf = binding.txtContinueParagraph.text.toString()

            if (binding.txtContinueParagraph.text.isEmpty()) {
                Toast.makeText(this, "Please insert your paragraph.", Toast.LENGTH_SHORT).show()
            }
            else {
                val dialog = AlertDialog.Builder(it.context)
                dialog.setMessage("Submit new paragraph?")
                dialog.setPositiveButton("SUBMIT", DialogInterface.OnClickListener { dialogInterface, i ->
                    val q = Volley.newRequestQueue(this)
                    val url = "https://ubaya.me/native/160821016/insert_paragraf.php"
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        {
                            Log.d("apisuccess", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK") {
                                Toast.makeText(this, "Your new paragraph is successfully added.", Toast.LENGTH_SHORT).show()

                                binding.recyclerViewRead.adapter?.notifyDataSetChanged()
                                updateList()

                                fetchParagraf()

                                binding.txtContinueParagraph.text.clear()
                            }
                        },
                        {
                            Log.e("apierror", it.printStackTrace().toString())
                            Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                        }) {
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["paragraf"] = paragraf
                            params["idcerbung"] = idcerbung.toString()
                            params["username"] = usernamelogin.toString()
                            return params
                        }
                    }

                    q.add(stringRequest)
                })
                dialog.setNegativeButton("CANCEL", null)
                dialog.create().show()
            }
        }

        binding.btnFollow.setOnClickListener {
            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.me/native/160821016/insert_follow.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                {
                    Log.d("apisuccess", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK") {
                        Log.d("apisuccess", it)
                        binding.btnFollow.visibility = View.INVISIBLE
                        binding.btnFollowing.visibility = View.VISIBLE
                    }
                },
                { Log.e("apierror", it.printStackTrace().toString()) }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["username"] = usernamelogin.toString()
                    params["idcerbung"] = idcerbung.toString()
                    return params
                }
            }

            q.add(stringRequest)
        }

        binding.btnRequest.setOnClickListener {
            val gsoncerbung = Gson()
            val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
            val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
            val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.me/native/160821016/insert_request.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                {
                    Log.d("apisuccess", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK") {
                        Log.d("apisuccess", it)
                        binding.btnRequest.visibility = View.INVISIBLE
                        binding.btnRequested.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.INVISIBLE

                        binding.lblContinue.visibility = View.INVISIBLE
                        binding.txtContinueParagraph.visibility = View.INVISIBLE
                        binding.txtCharacterCount.visibility = View.INVISIBLE

                        for (i in 0 until cerbungs.size) {
                            if (cerbungs[i].idcerbung == idcerbung) {
                                binding.txtAccessRead.text = cerbungs[i].access
                                break
                            }
                        }
                    }
                },
                { Log.e("apierror", it.printStackTrace().toString()) }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["username"] = usernamelogin.toString()
                    params["idcerbung"] = idcerbung.toString()
                    return params
                }
            }

            q.add(stringRequest)
        }

        binding.btnRequested.setOnClickListener {
            val gsoncerbung = Gson()
            val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
            val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
            val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.me/native/160821016/delete_request.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                {
                    Log.d("apisuccess", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK") {
                        Log.d("apisuccess", it)
                        binding.btnRequested.visibility = View.INVISIBLE
                        binding.btnRequest.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.INVISIBLE

                        binding.lblContinue.visibility = View.INVISIBLE
                        binding.txtContinueParagraph.visibility = View.INVISIBLE
                        binding.txtCharacterCount.visibility = View.INVISIBLE

                        for (i in 0 until cerbungs.size) {
                            if (cerbungs[i].idcerbung == idcerbung) {
                                binding.txtAccessRead.text = cerbungs[i].access
                                break
                            }
                        }
                    }
                },
                { Log.e("apierror", it.printStackTrace().toString()) }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["username"] = usernamelogin.toString()
                    params["idcerbung"] = idcerbung.toString()
                    return params
                }
            }

            q.add(stringRequest)
        }

        binding.btnFollowing.setOnClickListener {
            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.me/native/160821016/delete_follow.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                {
                    Log.d("apisuccess", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK") {
                        Log.d("apisuccess", it)
                        binding.btnFollowing.visibility = View.INVISIBLE
                        binding.btnFollow.visibility = View.VISIBLE
                    }
                },
                { Log.e("apierror", it.printStackTrace().toString()) }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["username"] = usernamelogin.toString()
                    params["idcerbung"] = idcerbung.toString()
                    return params
                }
            }

            q.add(stringRequest)
        }

        binding.btnLikeRead.setOnClickListener {
            val currentIcon = binding.btnLikeRead.drawable

            if (currentIcon.constantState == ContextCompat.getDrawable(this, R.drawable.baseline_thumb_up_off_alt_24)?.constantState) {
                isLiked = true
                updateLikeButton(isLiked)

                val q = Volley.newRequestQueue(this)
                val url = "https://ubaya.me/native/160821016/insert_like_cerbung.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    url,
                    {
                        Log.d("apisuccess", it)
                        val obj = JSONObject(it)
                        if (obj.getString("result") == "OK") {
                            for (i in 0 until cerbungs.size) {
                                if (cerbungs[i].idcerbung == idcerbung) {
                                    cerbungs[i].jumlah_like++
                                    binding.txtLikeRead.text = cerbungs[i].jumlah_like.toString()
                                    Log.d("apisuccess", it)
                                }
                            }
                        }
                    },
                    { Log.e("apierror", it.printStackTrace().toString()) }
                ) {
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["username"] = usernamelogin.toString()
                        params["idcerbung"] = idcerbung.toString()
                        return params
                    }
                }

                q.add(stringRequest)
            }
            else if (currentIcon.constantState == ContextCompat.getDrawable(this, R.drawable.baseline_thumb_up_alt_24)?.constantState) {
                isLiked = false
                updateLikeButton(isLiked)

                val q = Volley.newRequestQueue(this)
                val url = "https://ubaya.me/native/160821016/delete_like_cerbung.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    url,
                    {
                        Log.d("apisuccess", it)
                        val obj = JSONObject(it)
                        if (obj.getString("result") == "OK") {
                            for (i in 0 until cerbungs.size) {
                                if (cerbungs[i].idcerbung == idcerbung) {
                                    cerbungs[i].jumlah_like--
                                    binding.txtLikeRead.text = cerbungs[i].jumlah_like.toString()
                                    Log.d("apisuccess", it)
                                }
                            }
                        }
                    },
                    { Log.e("apierror", it.printStackTrace().toString()) }
                ) {
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["username"] = usernamelogin.toString()
                        params["idcerbung"] = idcerbung.toString()
                        return params
                    }
                }

                q.add(stringRequest)
            }
        }
    }

    private fun updateLikeButton(isLiked: Boolean) {
        // Set the appropriate icon based on the like status
        val iconResource = if (isLiked) {
            R.drawable.baseline_thumb_up_alt_24
        }
        else {
            R.drawable.baseline_thumb_up_off_alt_24
        }

        binding.btnLikeRead.setImageResource(iconResource)
    }

    private fun setupUI(cerbungs: ArrayList<Cerbung>, idcerbung: Int, usernamelogin: String?) {
        //display to the respective textviews
        for (i in 0 until cerbungs.size) {
            if (cerbungs[i].idcerbung == idcerbung) {
                binding.txtTitleRead.text = cerbungs[i].title
                binding.txtParagraphRead.text = cerbungs[i].jumlah_paragraf.toString()
                binding.txtLikeRead.text = cerbungs[i].jumlah_like.toString()

                val idgenre = cerbungs[i].idgenre
                for (j in 0 until genres.size) {
                    if (genres[j].idgenre == idgenre) {
                        binding.txtGenreRead.text = genres[j].name
                    }
                }

                //kalau user adalah writer dari cerbung, maka walaupun cerbung restricted, user tetap bisa akses secara publik
                if (cerbungs[i].username == usernamelogin) {
                    if (cerbungs[i].access == "Public") {
                        binding.txtAccessRead.visibility = View.INVISIBLE
                        binding.btnRequest.visibility = View.INVISIBLE
                        binding.btnRequested.visibility = View.INVISIBLE
                    }
                    else if (cerbungs[i].access == "Restricted") {
                        binding.txtAccessRead.text = cerbungs[i].access
                        binding.btnRequest.visibility = View.INVISIBLE
                        binding.btnRequested.visibility = View.INVISIBLE
                    }
                }
                else if (cerbungs[i].username != usernamelogin){
                    if (cerbungs[i].access == "Public") {
                        binding.txtAccessRead.visibility = View.INVISIBLE
                        binding.btnRequest.visibility = View.INVISIBLE
                        binding.btnRequested.visibility = View.INVISIBLE
                    }
                    else if (cerbungs[i].access == "Restricted") {
                        binding.txtAccessRead.text = cerbungs[i].access
                        binding.btnRequested.visibility = View.INVISIBLE

                        binding.lblContinue.visibility = View.INVISIBLE
                        binding.txtContinueParagraph.visibility = View.INVISIBLE
                        binding.txtCharacterCount.visibility = View.INVISIBLE
                        binding.btnSubmit.visibility = View.INVISIBLE
                        fetchRequest()
                    }
                }

                binding.txtWriterRead.text = "by " + cerbungs[i].username
                binding.txtDate.text = cerbungs[i].tanggal

                //display the data to the imageview
                val url = cerbungs[i].url
                val builder = Picasso.Builder(this)
                builder.listener { picasso, uri, exception ->
                    exception.printStackTrace() }
                builder.build().load(url).into(binding.imgCerbungRead)
                break
            }
        }
    }

    fun updateList() {
        //get the cerbung id and username
        val idcerbung = intent.getIntExtra(CerbungHomeAdapter.KEY_ID_CERBUNG, 0)
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        if (usernamelogin != null) {
            val lm = LinearLayoutManager(this)
            binding.recyclerViewRead.layoutManager = lm
            binding.recyclerViewRead.setHasFixedSize(true)
            binding.recyclerViewRead.adapter = ReadCerbungAdapter(paragrafs, idcerbung, usernamelogin, likesparagraf)
        }
        else
        {
            Log.e("Username Null", "usernamelogin is null")
        }
    }

    override fun onResume() {
        super.onResume()

        fetchParagraf()
    }

    private fun fetchParagraf() {
        val idcerbung = intent.getIntExtra(CerbungHomeAdapter.KEY_ID_CERBUNG, 0)

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/get_paragraf.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Paragraf>>() { }.type
                    paragrafs = Gson().fromJson(data.toString(), sType) as ArrayList<Paragraf>

                    binding.recyclerViewRead.adapter?.notifyDataSetChanged()
                    updateList()
                }
            },
            { Log.e("apierror", it.printStackTrace().toString()) }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["idcerbung"] = idcerbung.toString()
                return params
            }
        }

        q.add(stringRequest)
    }

    private fun fetchRequest() {
        val idcerbung = intent.getIntExtra(CerbungHomeAdapter.KEY_ID_CERBUNG, 0)
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160821016/get_request.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("apisuccess", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Requests>>() { }.type
                    requests = Gson().fromJson(data.toString(), sType) as ArrayList<Requests>

                    for (i in 0 until requests.size) {
                        if (requests[i].allow == "Pending") {
                            binding.btnRequest.visibility = View.INVISIBLE
                            binding.btnRequested.visibility = View.VISIBLE

                            binding.lblContinue.visibility = View.INVISIBLE
                            binding.txtContinueParagraph.visibility = View.INVISIBLE
                            binding.txtCharacterCount.visibility = View.INVISIBLE
                            binding.btnSubmit.visibility = View.INVISIBLE
                        }
                        else if (requests[i].allow == "No") {
                            binding.btnRequest.visibility = View.VISIBLE
                            binding.btnRequested.visibility = View.INVISIBLE

                            binding.lblContinue.visibility = View.INVISIBLE
                            binding.txtContinueParagraph.visibility = View.INVISIBLE
                            binding.txtCharacterCount.visibility = View.INVISIBLE
                            binding.btnSubmit.visibility = View.INVISIBLE
                        }
                        else if (requests[i].allow == "Yes") {
                            binding.btnRequest.visibility = View.INVISIBLE
                            binding.btnRequested.visibility = View.INVISIBLE
                        }
                    }
                }
            },
            { Log.e("apierror", it.printStackTrace().toString())
                binding.btnRequest.visibility = View.VISIBLE
                binding.btnRequested.visibility = View.INVISIBLE
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = usernamelogin.toString()
                params["idcerbung"] = idcerbung.toString()
                return params
            }
        }

        q.add(stringRequest)
    }
}