package com.example.topdownloader

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val BASE_URL: String =
    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/"
private const val FREE_APPS_URL: String = "topfreeapplications/limit=%d/xml"
private const val PAID_APPS_URL: String = "toppaidapplications/limit=%d/xml"
private const val SONGS_URL: String = "topsongs/limit=%d/xml"


class MainActivity : AppCompatActivity() {
    private var feedLimit = 10
    private var path = ""
    private var feedSavedPath = "INVALIDATED"
    private val STATE_URL = "feedUrl"
    private val STATE_LIMIT = "feedLimit"
    private var downloadData: DownloadData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            path = savedInstanceState.getString(STATE_URL).toString()
            feedLimit = savedInstanceState.getInt(STATE_LIMIT)
            downloadUrl(path.format(feedLimit))
        } else {
            downloadUrl(FREE_APPS_URL.format(feedLimit))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (feedLimit == 10) {
            menu?.findItem(R.id.menuTop10)?.isChecked = true
        } else {
            menu?.findItem(R.id.menuTop25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuFree -> path = FREE_APPS_URL
            R.id.menuPaid -> path = PAID_APPS_URL
            R.id.menuSongs -> path = SONGS_URL
            R.id.menuTop10, R.id.menuTop25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                    Log.d(null, "${item.title} changed to $feedLimit")
                } else {
                    Log.d(null, "${item.title} unchanged")
                }
            }
            R.id.menuRefresh -> feedSavedPath = "INVALIDATED"
            else -> return super.onOptionsItemSelected(item)
        }
        downloadUrl(path.format(feedLimit))
        return true
    }

    private fun downloadUrl(path: String) {
        //Download new feed data
        downloadData = DownloadData(this, xmlListView)
        if (path != feedSavedPath) {
            downloadData?.execute(BASE_URL + path)
            feedSavedPath = path
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (path == "") {
            path = FREE_APPS_URL
        }
        outState.putString(STATE_URL, path)
        outState.putInt(STATE_LIMIT, feedLimit)
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData?.cancel(true)
    }

}
