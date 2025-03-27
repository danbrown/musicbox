package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.init.ModContent.REGISTRATE
import net.minecraft.Util

object ModLang {

  var DOWNLOADING_SONG: String = Util.makeDescriptionId(
    "command",
    DeltaboxUtil.resourceLocation(ModContent.MOD_ID, ModContent.MOD_ID + ".downloading_song")
  )
  var DOWNLOADING_SONG_ERROR: String = Util.makeDescriptionId(
    "command",
    DeltaboxUtil.resourceLocation(ModContent.MOD_ID, ModContent.MOD_ID + ".downloading_song_error")
  )
  var DOWNLOADING_SONG_SUCCESS: String = Util.makeDescriptionId(
    "command",
    DeltaboxUtil.resourceLocation(ModContent.MOD_ID, ModContent.MOD_ID + ".downloading_song_success")
  )
  var PLAYING_SONG: String = Util.makeDescriptionId(
    "command",
    DeltaboxUtil.resourceLocation(ModContent.MOD_ID, ModContent.MOD_ID + ".playing_song")
  )

  init {

    // Tooltips
    REGISTRATE.langs().addRawLang(URLDiscItem.CUSTOM_DISC_TRANSLATION_KEY, "Custom Disc")
      .addRawLang(MusicDiscScreen.LOCKED_DISC_TRANSLATION_KEY, "This disc is signed!")
      .addRawLang(
        MusicDiscScreen.DOWNLOADING_DISC_TRANSLATION_KEY,
        "Downloading music, please wait a moment..."
      )
      .addRawLang(MusicDiscScreen.DOWNLOADING_ERROR_DISC_TRANSLATION_KEY, "Failed to download music!")
      .addRawLang(MusicDiscScreen.DOWNLOADING_SUCCESS_DISC_TRANSLATION_KEY, "Download complete!")
      .addRawLang(MusicDiscScreen.NO_RECORD_TRANSLATION_KEY, "No custom record found!")
      .addRawLang(MusicDiscScreen.YOUTUBE_INVALID_TRANSLATION_KEY, "Song URL is not a valid YouTube link!")
      .addRawLang(MusicDiscScreen.URL_INVALID_TRANSLATION_KEY, "Song URL is invalid!")
      .addRawLang(MusicDiscScreen.URL_TOO_LONG_TRANSLATION_KEY, "Song URL is too long!")
      .addRawLang(MusicDiscScreen.RADIUS_TOO_SMALL_TRANSLATION_KEY, "Radius is too small!")
      .addRawLang(MusicDiscScreen.RADIUS_TOO_BIG_TRANSLATION_KEY, "Radius is too big! Max is %s")
      .addRawLang(MusicDiscScreen.DISC_SAVED_TRANSLATION_KEY, "Song Details saved!")
      .addRawLang(MusicDiscScreen.YOUTUBE_URL_TRANSLATION_KEY, "Youtube URL")
      .addRawLang(MusicDiscScreen.DURATION_TRANSLATION_KEY, "Duration (In Seconds)")
      .addRawLang(MusicDiscScreen.SONG_NAME_TRANSLATION_KEY, "Song Name")
      .addRawLang(MusicDiscScreen.RADIUS_TRANSLATION_KEY, "Radius")

      // Music Box Commands
      .addRawLang(DOWNLOADING_SONG, "Downloading song %s")
      .addRawLang(DOWNLOADING_SONG_ERROR, "Downloading song %s failed")
      .addRawLang(DOWNLOADING_SONG_SUCCESS, "Downloading song %s succeeded")
      .addRawLang(PLAYING_SONG, "Playing song for %s")

      // Fields
      .addRawLang(MusicDiscScreen.URL_FIELD_KEY, "URL:")
      .addRawLang(MusicDiscScreen.DURATION_FIELD_KEY, "Duration:")
      .addRawLang(MusicDiscScreen.SONG_NAME_FIELD_KEY, "Name:")
      .addRawLang(MusicDiscScreen.RADIUS_FIELD_KEY, "Radius:")
  }


  fun register() {
    // init
  }
}