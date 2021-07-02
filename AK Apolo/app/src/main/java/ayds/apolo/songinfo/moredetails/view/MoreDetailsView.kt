package ayds.apolo.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import ayds.apolo.songinfo.R
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.NoResultsCard
import ayds.apolo.songinfo.utils.navigation.openExternalUrl
import ayds.apolo.songinfo.utils.UtilsModule
import ayds.observer.Observable
import ayds.observer.Subject

private const val ARTIST_NAME = "artistName"

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiStateService: MoreDetailsUiState

    fun updateCard(cards: List<Card>)
    fun openCardURLActivity()
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var moreDetailsModel: MoreDetailsModel
    private val helperCardInfo = MoreDetailsViewModule.helperCardInfo
    private lateinit var listOfCards: List<Card>

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiStateService: MoreDetailsUiState = MoreDetailsUiState()

    private lateinit var moreDetailsPane: TextView
    private lateinit var openURLButton: Button
    private lateinit var imageView: ImageView
    private lateinit var spinnerSource: Spinner
    private lateinit var sourceInfoPane: TextView
    private var indexSpinner = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        initArtistName()
        initObservers()
        initListeners()
        notifyCreated()
    }

    private fun initListeners() {
        initURLButtonListener()
        initSpinnerListener()
    }

    private fun notifyCreated() {
        onActionSubject.notify(MoreDetailsUiEvent.OnCreated)
    }

    private fun initModule() {
        MoreDetailsViewModule.init(this)
        moreDetailsModel = MoreDetailsModelModule.getMoreDetailsModel()
    }

    private fun initProperties() {
        moreDetailsPane = findViewById(R.id.moreDetailsPane)
        openURLButton = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
        sourceInfoPane = findViewById(R.id.sourceLabel)
        spinnerSource = findViewById(R.id.spinner)
    }

    private fun initArtistName() {
        uiStateService =
            uiStateService.copy(artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString())
    }

    private fun initSpinner(list: List<Card>) {
        listOfCards = list
        val spinnerNames: MutableList<String> = mutableListOf()
        for (card in listOfCards) {
            spinnerNames.add(card.source.service)
        }
        if (spinnerNames.isEmpty()) {
            addNoResultsCard(spinnerNames)
            setDisabledAction()
            updateMoreDetailsState()
        }

        runOnUiThread {
            spinnerSource.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerNames)
        }
    }

    private fun addNoResultsCard(spinnerNames: MutableList<String>){
        listOfCards = mutableListOf(NoResultsCard)
        spinnerNames.add(NoResultsCard.source.service)
    }

    private fun setDisabledAction() {
        uiStateService = uiStateService.copy(actionsEnabled = false)
    }

    private fun updateMoreDetailsState() {
        enableActions(uiStateService.actionsEnabled)
    }

    private fun enableActions(enable: Boolean) {
        runOnUiThread {
            openURLButton.isEnabled = enable
            spinnerSource.isEnabled = enable
        }
    }

    private fun initSpinnerListener() {
        spinnerSource.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                indexSpinner = position
                updateCard(listOfCards)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                updateCard(listOfCards)
            }
        }
    }

    private fun initURLButtonListener() {
        openURLButton.setOnClickListener {
            notifyFullCardAction()
        }
    }

    override fun openCardURLActivity() {
        openExternalUrl(uiStateService.cards[indexSpinner].infoURL)
    }

    private fun initObservers() {
        moreDetailsModel.cardObservable()
            .subscribe { value ->
                initSpinner(value)
            }
    }

    private fun notifyFullCardAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ViewFullCard)
    }

    override fun updateCard(cards: List<Card>) {
        updateUiState(cards)
        updateArtistInfoUI()
    }

    private fun updateUiState(cards: List<Card>) {
        when {
            cards.isEmpty() -> updateNoResultsUiState()
            else -> updateResultsUiState(cards)
        }
    }

    private fun updateResultsUiState(cards: List<Card>) {
        uiStateService.cards = cards
    }

    private fun updateNoResultsUiState() {
        uiStateService.cards = listOf()
    }

    private fun updateArtistInfoUI() {
        runOnUiThread {
            loadLastImage()
            loadArtistInfo()
            loadSourceInfo()
        }
    }

    private fun loadLastImage() {
        UtilsModule.imageLoader.loadImageIntoView(
            uiStateService.cards[indexSpinner].sourceLogoURL,
            imageView
        )
    }

    private fun loadArtistInfo() {
        moreDetailsPane.text = Html.fromHtml(
            helperCardInfo.getTextToHtml(
                uiStateService.cards[indexSpinner].description,
                uiStateService.artistName
            )
        )
    }

    private fun loadSourceInfo() {
        sourceInfoPane.text = "FROM: " + uiStateService.cards[indexSpinner].source.service
    }

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME
    }
}