package com.example.topdownloader

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class ParseData {
    val data = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xmlPullParser = factory.newPullParser()
            xmlPullParser.setInput(xmlData.reader())
            var eventType = xmlPullParser.eventType
            var currentRecord = FeedEntry()

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xmlPullParser.name?.toLowerCase()

                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xmlPullParser.text

                    XmlPullParser.END_TAG -> {
                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    data.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()
                                }

                                "title" -> currentRecord.title = textValue
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "releasedate" -> currentRecord.releaseDate = textValue
                                "summary" -> currentRecord.summary = textValue
                                "image" -> currentRecord.imageURL = textValue
                            }
                        }
                    }
                }
                eventType = xmlPullParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return status
    }
}