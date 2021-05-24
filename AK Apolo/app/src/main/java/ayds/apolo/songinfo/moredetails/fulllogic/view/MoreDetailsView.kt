package ayds.apolo.songinfo.moredetails.fulllogic.view

import androidx.appcompat.app.AppCompatActivity
import ayds.apolo.songinfo.home.view.HomeUiEvent
import ayds.apolo.songinfo.home.view.HomeUiState
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ayds.apolo.songinfo.R
import ayds.apolo.songinfo.moredetails.fulllogic.IMAGE_URL
import ayds.observer.Observable
import ayds.observer.Subject
import com.squareup.picasso.Picasso

interface MoreDetailsView{
    val uiEventObservable : Observable<MoreDetailsUiEvent>
    val uiState : HomeUiState

    fun navigate()
    fun open()
}

class MoreDetailsViewActivity : AppCompatActivity() , MoreDetailsView{

    private val onActionSubject = Subject<MoreDetailsUiEvent>()

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override val uiState: HomeUiState = HomeUiState()

    private lateinit var moreDetailsPane: TextView
    private lateinit var buttonView: View
    private lateinit var imageView: ImageView

    override fun navigate() {
        TODO("Not yet implemented")
    }

    override fun open(){
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initMoreDetailsPane()
        initProperties()
    }

    private fun initProperties(){
        initButtonView()
        initImageView()
        initListeners()
    }
    private fun initButtonView() {
        buttonView = findViewById(R.id.openUrlButton)
    }

    private fun initImageView() {
        imageView = findViewById(R.id.imageView)
    }

    private fun initMoreDetailsPane() {
        moreDetailsPane = findViewById(R.id.moreDetailsPane)
    }

    private fun initListeners() {
        initURLButtonListener()
    }

    private fun initURLButtonListener() =
        buttonView.setOnClickListener {
            openURLActivity()
        }


    private fun loadArtistImage() {
        Picasso.get().load(IMAGE_URL).into(imageView)
    }

    private fun loadArtistText() {
        moreDetailsPane.text = Html.fromHtml(artistInfo)
    }

    private fun initArtistThread() {
        Thread {
            updateArtistInfo()
            updateArtistInfoUI()
        }.start()
    }

    //Los update los puse aca porque segun dice el grafico van aca, pero
    //habria los get se le hacen al model
    private fun updateArtistInfo() {
        artistInfo = getArtistFromDatabase()
        if(artistInfo != null)
            addStorePrefix()
        else
        {
            artistInfo = getArtistInfoFromLastFM()
            saveArtistInDatabase()
        }
    }

    private fun updateArtistInfoUI() {
        runOnUiThread {
            loadArtistImage()
            loadArtistText()
        }
    }

    private fun openURLActivity() {
        val openUrlAction = Intent(Intent.ACTION_VIEW)
       // urlString creo que corresponde a model, pero
        //una nueva activity creo es view, por ende
        //lo dejo comentado
        // openUrlAction.data = Uri.parse(urlString)
        startActivity(openUrlAction)
    }

}