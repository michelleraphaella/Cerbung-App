package com.ubaya.kelompok3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.kelompok3.databinding.ActivitySignUpBinding
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            if (binding.txtUsernameSignUp.text.isEmpty() || binding.txtPasswordSignUp.text.isEmpty() ||
                binding.txtPasswordConfirm.text.isEmpty() || binding.txtProfilePicture.text.isEmpty()) {
                Toast.makeText(this, "Please fill all the data needed", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if (binding.txtPasswordSignUp.text.toString() != binding.txtPasswordConfirm.text.toString()) {
                    Toast.makeText(this, "Password and password confirmation don't match", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val newUsername = binding.txtUsernameSignUp.text.toString()
                    val newImg = binding.txtProfilePicture.text.toString()
                    val newPassword = binding.txtPasswordSignUp.text.toString()
                    val confirmPassword = binding.txtPasswordConfirm.text.toString()

                    val q = Volley.newRequestQueue(this)
                    val url = "https://ubaya.me/native/160821016/insert_user.php"
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        {
                            Log.d("apisuccess", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK") {
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)

                                Toast.makeText(this, "Your new account is successfully created. Please sign in", Toast.LENGTH_SHORT).show()

                                finish()
                            }
                        },
                        { Log.e("apierror", it.printStackTrace().toString()) }
                    ) {
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["username"] = newUsername
                            params["url"] = newImg
                            params["password"] = newPassword
                            params["confirm_password"] = confirmPassword
                            return params
                        }
                    }

                    q.add(stringRequest)
                }
            }
        }

        binding.btnToSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}