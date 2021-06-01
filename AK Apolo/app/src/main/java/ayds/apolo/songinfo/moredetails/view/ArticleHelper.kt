package ayds.apolo.songinfo.moredetails.view

import java.util.*

interface ArticleHelper {
    fun getTextToHtml(text: String, term: String): String
}

private const val START_HTML = "<html><div width=400>"
private const val FONT_HTML = "<font face=\"arial\">"
private const val END_HTML = "</font></div></html>"

internal class ArticleHelperImpl : ArticleHelper {

    override fun getTextToHtml(text: String, term: String) =
        StringBuilder().apply{
             append(START_HTML)
             append(FONT_HTML)
             append(formatText(term, text))
             append(END_HTML)
        }.toString()



    private fun formatText(term: String, text: String): String {
        return text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)" + term.toRegex(),
                "<b>" + term.toUpperCase(Locale.getDefault()) + "</b>"
            )
    }

}