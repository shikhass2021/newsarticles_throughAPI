package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

class MainActivity3 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    var itemsArray: ArrayList<Property> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = LinearLayoutManager(this)
        getAllData() // calling getAllData function to load the data from API end point.

    }
    fun getAllData(){

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val apiResponse = URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json").readText()

        Log.d("apitest","apires"+apiResponse)


        val jsonObject = JSONTokener(apiResponse).nextValue() as JSONObject // parsing the json value

        val jsonArray = jsonObject.getJSONArray("articles") // reads the articles data using jsonArray

        for (i in 0 until jsonArray.length()) {


            val source = jsonArray.getJSONObject(i).getJSONObject("source")


            val sourceid = source.getString("id")
            val sourcename = source.getString("name")
            val title = jsonArray.getJSONObject(i).getString("title")
            val description = jsonArray.getJSONObject(i).getString("description")
            val urlToImage = jsonArray.getJSONObject(i).getString("urlToImage")


            Log.i("apitest", "sourceid"+sourceid +"sourcename"+sourcename )


            val model = Property(
                sourceid,title,description,urlToImage
            )
            itemsArray.add(model)
        }
                    recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply{
                        myAdapter = MyAdapter(itemsArray)
                        layoutManager = manager
                        adapter = myAdapter
                    }
                }





    }
