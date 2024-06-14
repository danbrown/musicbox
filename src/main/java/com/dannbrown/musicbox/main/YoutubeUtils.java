package com.dannbrown.musicbox.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeUtils {
  private static final Pattern YOUTUBE_VIDEO_REGEX = Pattern.compile("^((?:https?:)?//)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$", Pattern.CASE_INSENSITIVE);

  public static boolean isYoutubeVideo(String url) {
    Matcher matcher = YOUTUBE_VIDEO_REGEX.matcher(url);
    return matcher.find() && url.contains("v=");
  }

  public static String removeUrlParameters(String url) {
    if (url.contains("v=")) {
      return url.split("&")[0];
    }
    else{
      return url.split("\\?")[0];
    }
  }
}
