package com.example.myapplication

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL


class MainActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val apiResponse = URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json").readText()

       // Log.d("apitest","apires"+apiResponse) // printing payload from api url


        val jsonObject = JSONTokener(apiResponse).nextValue() as JSONObject // parsing the json value
        val jsonArray = jsonObject.getJSONArray("articles") // reads the articles data using jsonArray

        for (i in 0 until jsonArray.length()) {
            //fetching all the source id and names data
            val source = jsonArray.getJSONObject(i).getJSONObject("source")
            val sourceid = source.getString("id")
            val sourcename = source.getString("name")

            Log.i("apitest", "sourceid"+sourceid +"sourcename"+sourcename )

        }
    }
}


