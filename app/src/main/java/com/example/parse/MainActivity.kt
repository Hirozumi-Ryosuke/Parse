package com.example.parse

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var loader: AsyncTask<Void, Void, ArrayList<New>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loader = LoadInitBooks(this)
        loader!!.execute()
    }

    internal class LoadInitBooks(var activity: AppCompatActivity?): AsyncTask<Void, Void, ArrayList<New>>() {

        private var rakuten: ArrayList<New> = ArrayList()

        override fun doInBackground(vararg params: Void?): ArrayList<New> {
            try {
                val url = "https://ranking.rakuten.co.jp/daily/200162/?scid=s_kwo_2014test_test"
                val doc: Document = Jsoup.connect(url).get()
                // get images inside of the div
                val images: Elements = doc.select("div.rnkRanking_image")
                // get tagHeading inside of the div
                val tagHeading: Elements = doc.select("div.rnkRanking_itemName")

                val size: Int = images.size
                for (index in 0..size) {
                    // get image link inside tag "img" with attribute src
                    val imgUrl: String = images.select("img").eq(index).attr("src")
                    // get text title inside tag "a"
                    val title: String = tagHeading.select("a").eq(index).text()
                    // get details link inside tag "a" with attribute "href"
                    val details: String = tagHeading.select("a").attr("href")

                    Log.i("Result", imgUrl + " " + " " + details)
                    rakuten.add(New("https://ranking.rakuten.co.jp" + imgUrl, title, details))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return rakuten
        }

    }
}
