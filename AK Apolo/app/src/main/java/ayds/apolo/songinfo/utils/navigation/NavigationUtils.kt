package ayds.apolo.songinfo.utils.navigation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.openExternalUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
}