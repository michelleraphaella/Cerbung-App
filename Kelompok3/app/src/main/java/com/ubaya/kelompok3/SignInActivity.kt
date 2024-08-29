package com.ubaya.kelompok3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.kelompok3.databinding.ActivitySignInBinding
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {
    private var users:ArrayList<User> = arrayListOf()
    private lateinit var binding: ActivitySignInBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var username: String? = null
    private var password: String? = null

    companion object {
        val SHARED_PREFS = "sharedprefs"
        val KEY_USERNAME = "username"
        val KEY_PASSWORD = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        username = sharedPreferences.getString(KEY_USERNAME, null)
        password = sharedPreferences.getString(KEY_PASSWORD, null)

        binding.btnSignIn.setOnClickListener {
            var isUserFound = false
            var isPasswordRight = false

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

                        for (user in users) {
                            if (binding.txtUsernameSignIn.text.toString() == user.username &&
                                binding.txtPasswordSignIn.text.toString() == user.password) {
                                val editor = sharedPreferences.edit()
                                editor.putString(KEY_USERNAME, binding.txtUsernameSignIn.text.toString())
                                editor.putString(KEY_PASSWORD, binding.txtPasswordSignIn.text.toString())
                                editor.apply()

                                val intent = Intent(this, HomeActivity::class.java)
                                //intent utk simpan user yg login
                                intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, user.username)
                                startActivity(intent)
                                isUserFound = true
                                isPasswordRight = true

                                finish()
                            }
                        }

                        if (isUserFound == false || isPasswordRight == false) {
                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                Response.ErrorListener {
                    Log.e("apiresult", it.message.toString())
                })
            q.add(stringRequest)
        }

        binding.btnToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        if (username != null && password != null) {
            val intent = Intent(this, HomeActivity::class.java)
            //intent utk simpan user yg login
            intent.putExtra(CreateCerbung1Activity.KEY_USERNAME, username)
            startActivity(intent)
            finish()
        }
    }
}