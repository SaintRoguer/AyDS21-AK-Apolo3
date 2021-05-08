package ayds.apolo.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.apolo.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

class OtherInfoWindow : AppCompatActivity() {
    private var moreDetailsPane: TextView? = null
    private var dataBase: DataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        moreDetailsPane = findViewById(R.id.moreDetailsPane)
        getInfoMoreDetailsPane(intent.getStringExtra("artistName"))
    }

    private fun getInfoMoreDetailsPane(artistName: String?) {
        dataBase = DataBase(this)
        getArtistInfo(artistName)
    }

    private fun adaptJInterfaceToHTTP() =
        Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(LastFMAPI::class.java)

    private fun extractToHTML(extract: JsonElement, artistName: String) : String {
        var moreDetailsDescription: String
        if (extract == null) {
            moreDetailsDescription = "No Results"
        } else {
            moreDetailsDescription = extract.asString.replace("\\n", "\n")
            moreDetailsDescription = textToHtml(moreDetailsDescription, artistName)
            // save to DB  <o/
            DataBase.saveArtist(dataBase!!, artistName, moreDetailsDescription)
        }
        return moreDetailsDescription
    }

    private fun linkToURLButton(url: JsonElement) {
        val urlString = url.asString
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun getInfoFromService(lastFMAPI: LastFMAPI, artistName: String) : List<JsonElement>{
        val callResponse: Response<String>
        val infoFromJsonExtractAndUrl = mutableListOf<JsonElement>()
        try {
            callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            Log.e("TAG", "JSON " + callResponse.body())
            val gson = Gson()
            val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val artist = jobj["artist"].asJsonObject
            val bio = artist["bio"].asJsonObject
            val extract = bio["content"]
            val url = artist["url"]
            infoFromJsonExtractAndUrl.add(extract)
            infoFromJsonExtractAndUrl.add(url)

        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }

        return infoFromJsonExtractAndUrl
    }

    private fun getArtistInfo(artistName: String?) {
        val lastFMAPI = adaptJInterfaceToHTTP()

        Log.e("TAG", "artistName $artistName")

        Thread {
            var moreDetailsDescription = DataBase.getInfo(dataBase!!, artistName!!)

            if (moreDetailsDescription != null)// exists in db
                moreDetailsDescription = "[*]$moreDetailsDescription"
            else { // get from service
                val extractAndUrl = getInfoFromService(lastFMAPI, artistName)
                val extract= extractAndUrl[0]
                val url= extractAndUrl[1]
                extractToHTML(extract, artistName)
                linkToURLButton(url)

            }
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Log.e("TAG", "Get Image from $imageUrl")
            val finalText = moreDetailsDescription
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
                moreDetailsPane!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace("(?i)" + term!!.toRegex(), "<b>" + term.toUpperCase() + "</b>")
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}