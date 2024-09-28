package com.dannbrown.musicbox.lib.main

import java.util.regex.Pattern

object YoutubeUtils {
  private val YOUTUBE_VIDEO_REGEX: Pattern = Pattern.compile(
    "^((?:https?:)?//)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/|watch\\?v=)?)([\\w\\-]+)(\\S+)?$",
    Pattern.CASE_INSENSITIVE
  )

  fun isYoutubeVideo(url: String): Boolean {
    val matcher = YOUTUBE_VIDEO_REGEX.matcher(url)
    // Match both "v=" from regular URL and the youtu.be short URLs
    return matcher.find()
  }

  fun removeUrlParameters(url: String): String {
    val matcher = YOUTUBE_VIDEO_REGEX.matcher(url)
    if (matcher.find()) {
      val videoId = matcher.group(6) // This captures the video ID
      // Return in the old format for compatibility (always in full URL form)
      return "https://www.youtube.com/watch?v=$videoId"
    }
    return url // If no match, return the original URL
  }
}
