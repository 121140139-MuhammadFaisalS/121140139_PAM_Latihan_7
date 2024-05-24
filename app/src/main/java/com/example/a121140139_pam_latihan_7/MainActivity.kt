package com.example.a121140139_pam_latihan_7

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(emptyList()) // Tampilkan daftar kosong
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getUsers()

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val userList = response.body()?.data ?: emptyList()
                    adapter.setData(userList)
                } else {
                    // Tangani kasus jika respons tidak berhasil
                    Toast.makeText(this@MainActivity, "Gagal mendapatkan data: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", "Gagal mendapatkan data: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // Tangani kasus jika terjadi kesalahan jaringan
                Toast.makeText(this@MainActivity, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Kesalahan jaringan", t)
            }
        })
    }
}
