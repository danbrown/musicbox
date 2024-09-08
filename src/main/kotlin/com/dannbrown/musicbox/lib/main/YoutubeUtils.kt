package com.dannbrown.musicbox.lib.main

import java.util.regex.Pattern

object YoutubeUtils {
  private val YOUTUBE_VIDEO_REGEX: Pattern = Pattern.compile("^((?:https?:)?//)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$", Pattern.CASE_INSENSITIVE)
  fun isYoutubeVideo(url: String): Boolean {
    val matcher = YOUTUBE_VIDEO_REGEX.matcher(url)
    return matcher.find() && url.contains("v=")
  }

  fun removeUrlParameters(url: String): String {
    return if (url.contains("v=")) {
      url.split("&".toRegex())
        .dropLastWhile { it.isEmpty() }
        .toTypedArray()[0]
    }
    else {
      url.split("\\?".toRegex())
        .dropLastWhile { it.isEmpty() }
        .toTypedArray()[0]
    }
  }
}
