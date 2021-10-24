package com.example.imgurapp

//import com.example.demoapp.databinding.ActivityMainBinding

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.imgurapp.dataClass.data
import com.example.imgurapp.databinding.ActivityMainBinding
import com.example.imgurapp.network.ApiClient
import com.example.imgurapp.network.MyApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var list: MutableList<String>? = ArrayList()
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
            calApi()
    }



    private fun sliderItems() {

        binding.viewPager.apply {
            sliderAdapter = SliderAdapter(this@MainActivity, binding.viewPager, list)
            this.adapter = sliderAdapter

            progress()
            val comPosPageTrans = CompositePageTransformer()
            comPosPageTrans.addTransformer(MarginPageTransformer(40))
            comPosPageTrans.addTransformer { page, position ->
                val r: Float = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f

            }
            this.setPageTransformer(comPosPageTrans)

            sliderHandler = Handler(Looper.myLooper()!!)
            sliderRunnable = Runnable {
                this.currentItem = this.currentItem + 1
                progress()
            }
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 5000)
                }
            })

        }


    }
    fun progress() {

        val oneMin = 5100
        object : CountDownTimer(
            oneMin.toLong(), 100
        ) {
            override fun onTick(millisUntilFinished: Long) {

                //forward progress
                val finishedSeconds = oneMin - millisUntilFinished
                val total = (finishedSeconds.toFloat() / oneMin.toFloat() * 100.0).toInt()
                binding.bar.setProgress(total)


            }

            override fun onFinish() {


            }
        }.start()
    }

     fun calApi() {
        var apiInterface = ApiClient.client!!.create(MyApi::class.java)
        var result = apiInterface.getList("Client-ID 546c25a59c58ad7")
        result.enqueue(object : Callback<data> {
            override fun onResponse(call: Call<data>, response: Response<data>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        for (i in 0 until resp.data.size) {
                            list!!.add(resp.data[i].link)
                        }
                        Toast.makeText(
                            this@MainActivity,
                            "" + list!!.size.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        sliderItems()
                    }
                }
            }

            override fun onFailure(call: Call<data>, t: Throwable) {
                Toast.makeText(this@MainActivity, "" + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }

}