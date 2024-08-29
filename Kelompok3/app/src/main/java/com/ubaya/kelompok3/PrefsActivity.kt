package com.ubaya.kelompok3

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ubaya.kelompok3.databinding.ActivityPrefsBinding
import org.json.JSONObject

class PrefsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrefsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var users:ArrayList<User> = arrayListOf()
    private var username: String? = null
    private var password: String? = null

    companion object {
        val SHARED_PREFS = "sharedprefs"
        val KEY_USERNAME = "username"
        val KEY_PASSWORD = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrefsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        username = sharedPreferences.getString(KEY_USERNAME, null)
        password = sharedPreferences.getString(KEY_PASSWORD, null)

        //ambil intent
        val usernamelogin = intent.getStringExtra(CreateCerbung1Activity.KEY_USERNAME)

        val gsoncerbung = Gson()
        val arrayCerbung = intent.getStringExtra(CreateCerbung1Activity.ARRAY_CERBUNG)
        val type = object : TypeToken<ArrayList<Cerbung>>() {}.type
        val cerbungs = gsoncerbung.fromJson<ArrayList<Cerbung>>(arrayCerbung, type)

        //selected button
        binding.bottomNav.selectedItemId = R.id.itemsPrefs

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

                    //update password with the newest data
                    password = getPassword(users, usernamelogin)

                    setupUI(users, usernamelogin)
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)

        //cek night mode
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        binding.btnDarkMode.isChecked = currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES

        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            binding.btnDarkMode.isChecked = true
        }

        binding.btnDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
            var theme = 0

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                theme = 1
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.me/native/160821016/update_theme.php"
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
                    params["username"] = usernamelogin.toString()
                    params["theme"] = theme.toString()
                    return params
                }
            }

            q.add(stringRequest)
        }

        binding.btnToSignOut.setOnClickListener {
            val dialog = AlertDialog.Builder(it.context)
            dialog.setMessage("Are you sure you want to sign out?")
            dialog.setPositiveButton("SIGN OUT", DialogInterface.OnClickListener { dialogInterface, i ->
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()

                Toast.makeText(this, "Successfully signed out.", Toast.LENGTH_SHORT).show()
            })
            dialog.setNegativeButton("CANCEL", null)
            dialog.create().show()
        }

        binding.btnChange.setOnClickListener {
            if (binding.txtOldPassword.text.isEmpty() || binding.txtNewPassword.text.isEmpty() ||
                binding.txtRetypeNewPassword.text.isEmpty()) {
                Toast.makeText(this, "Please fill all the data needed", Toast.LENGTH_SHORT).show()
            }
            else {
                if (binding.txtNewPassword.text.toString() != binding.txtRetypeNewPassword.text.toString()) {
                    Toast.makeText(this, "The new password and retyped new password don't match", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (binding.txtOldPassword.text.toString() == binding.txtNewPassword.text.toString() ||
                        binding.txtOldPassword.text.toString() == binding.txtRetypeNewPassword.text.toString()) {
                        Toast.makeText(this, "Old password and new password are the same", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val oldpass = getPassword(users, usernamelogin)

                        if (binding.txtOldPassword.text.toString() != oldpass) {
                            Toast.makeText(this, "Old password is wrong. Please try again", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            val dialog = AlertDialog.Builder(it.context)
                            dialog.setMessage("Change password?")
                            dialog.setPositiveButton("CHANGE", DialogInterface.OnClickListener { dialogInterface, i ->
                                val q = Volley.newRequestQueue(this)
                                val url = "https://ubaya.me/native/160821016/update_password.php"
                                val stringRequest = object : StringRequest(
                                    Request.Method.POST,
                                    url,
                                    {
                                        Log.d("apisuccess", it)
                                        val obj = JSONObject(it)
                                        if (obj.getString("result") == "OK") {
                                            Toast.makeText(this, "Password successfully changed.", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, PrefsActivity::class.java)
                                            intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, usernamelogin)
                                            val gsoncerbung = Gson()
                                            val arrayCerbung = gsoncerbung.toJson(cerbungs)
                                            intent.putExtra(CreateCerbung1Activity.ARRAY_CERBUNG, arrayCerbung)
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
                                        params["password"] = binding.txtNewPassword.text.toString()
                                        params["confirm_password"] = binding.txtRetypeNewPassword.text.toString()
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
                }
            }
        }
    }

    private fun setupUI(users: ArrayList<User>, usernamelogin: String?) {
        //display to the respective textviews
        for (i in 0 until users.size) {
            if (users[i].username == usernamelogin) {
                binding.txtUsername.setText(users[i].username)

                //display the data to the imageview
                val url = users[i].url
                val builder = Picasso.Builder(this)
                builder.listener { picasso, uri, exception ->
                    exception.printStackTrace() }
                builder.build().load(url).into(binding.imgCerbungPrefs)
                break
            }
        }
    }

    private fun getPassword(users: ArrayList<User>, usernamelogin: String?): String {
        for (i in 0 until users.size) {
            if (users[i].username == usernamelogin) {
                return users[i].password
            }
        }
        return ""
    }
}