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

  fun getPitchFromUrl(url: String): Float? {
    // look for pitch= in the url, the value should be a float
    // if not found, can't be lower than 0.5f or higher than 2.0f
    val pitchRegex = Pattern.compile("pitch=([0-9]*\\.?[0-9]+)")
    val matcher = pitchRegex.matcher(url)
    if (matcher.find()) {
      val pitch = matcher.group(1).toFloat()
      return when {
        pitch > 2.0f -> 2.0f
        pitch < 0.5f -> 0.5f
        else -> pitch
      }
    }
    return null
  }
}
