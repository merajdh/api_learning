package com.example.myapplicationtest.Ui.market

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationtest.ApiManager.ApiManager
import com.example.myapplicationtest.Model.TopCoin
import com.example.myapplicationtest.R
import com.example.myapplicationtest.Ui.CoinActivity.CoinActivity
import com.example.myapplicationtest.databinding.FragmentMarketBinding

class MainActivityMarket : AppCompatActivity() , MareketAdapter.onClick{
    lateinit var binding: FragmentMarketBinding
    val apimanager = ApiManager()
    lateinit var dataNews : ArrayList< Pair < String , String> >
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InitUi()

    }

    private fun InitUi() {

        getNews()
        getTopCoins()

    }


    private fun getNews() {

        apimanager.getNews(object :ApiManager.ApiCallback <ArrayList<Pair<String,String>>>{
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews = data
                refreshNews()

            }

            override fun onError(errorManager: String) {
                Toast.makeText(this@MainActivityMarket, "error is -> " + errorManager, Toast.LENGTH_SHORT).show()
            }

        })

    }
    private fun refreshNews() {

        val randomAccess = (0..49).random()
        binding.mouduleOneMarket.txtNews.text = dataNews[randomAccess].first
        Log.v("teest" , dataNews[randomAccess].first.toString())
        binding.mouduleOneMarket.txtNews.setOnClickListener{
            val anim = AlphaAnimation(0f , 2f )
            anim.duration = 1200
            anim.fillAfter = true
            binding.mouduleOneMarket.txtNews.startAnimation(anim)
            refreshNews()
        }
        binding.mouduleOneMarket.btnNews.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW , Uri.parse(dataNews[randomAccess].second))
            Log.v("teste" , dataNews[randomAccess].second)
            startActivity(intent)
        }

    }

    private fun getTopCoins() {


        apimanager.getCoin( object : ApiManager.ApiCallback<List<TopCoin.Data>>{
            override fun onSuccess(datas: List<TopCoin.Data>) {
                showDataRec(datas)
            }

            override fun onError(errorManager: String) {
                Toast.makeText(this@MainActivityMarket, "error" + errorManager , Toast.LENGTH_SHORT).show()
                Log.v("00" , errorManager)
            }

        })
    }

    private fun showDataRec(data : List<TopCoin.Data>){

        val  marketAdapter =MareketAdapter(ArrayList(data)  , this )
        binding.mouduleTwoMarket.recMain.adapter = marketAdapter
        binding.mouduleTwoMarket.recMain.layoutManager = LinearLayoutManager(this , RecyclerView.VERTICAL , false)

        binding.mouduleTwoMarket.btnMore.setOnClickListener {
            val intent = Intent( Intent.ACTION_VIEW , Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)
        }
    }

    override fun onItemClick(dataCoin: TopCoin.Data) {
        val intent = Intent(this , CoinActivity::class.java)
        intent.putExtra("dataSend" , dataCoin)
        startActivity(intent)
    }

}