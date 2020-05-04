package com.example.topdownloader

import android.content.Context
import android.os.AsyncTask
import android.widget.ListView
import kotlin.properties.Delegates

class DownloadData(context: Context, listView: ListView) :
    AsyncTask<String, Void, String>() {
    private var propContext: Context by Delegates.notNull()
    private var propListView: ListView by Delegates.notNull()

    init {
        propContext = context
        propListView = listView
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        val parseData = ParseData()
        parseData.parse(result)

        val feedAdapter = FeedAdapter(propContext, R.layout.list_record, parseData.data)
        propListView.adapter = feedAdapter
    }

    override fun doInBackground(vararg params: String?): String {
        return downloadXML(params[0])
    }

    private fun downloadXML(urlPath: String?): String {
        return java.net.URL(urlPath).readText()
    }
}