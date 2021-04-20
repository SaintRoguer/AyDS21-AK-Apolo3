package ayds.apolo.songinfo.utils

import ayds.apolo.songinfo.utils.view.ImageLoader
import ayds.apolo.songinfo.utils.view.ImageLoaderImpl
import com.squareup.picasso.Picasso

object UtilsModule {

    val imageLoader: ImageLoader = ImageLoaderImpl(Picasso.get())
}