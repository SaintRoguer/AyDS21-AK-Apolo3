package ayds.jkhera2.nytimes

import ayds.jkhera2.nytimes.entities.Article

interface NYTArticleService {
    fun getArticleInfo(nameOfArtist: String): Article?
}