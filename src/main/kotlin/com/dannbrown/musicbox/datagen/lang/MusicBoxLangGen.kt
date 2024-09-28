package com.dannbrown.musicbox.datagen.lang

import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.init.MusicBoxCommands
import com.dannbrown.musicbox.init.MusicBoxCreativeTabs

object MusicBoxLangGen {
  fun addStaticLangs(doRun: Boolean) {
    if (!doRun) return // avoid running in the server-side
    // Creative tabs
    MusicBoxModule.REGISTRATE.addCreativeTabLang(MusicBoxCreativeTabs.CREATIVE_TAB_KEY, MusicBoxModule.NAME)

    // Tooltips
    MusicBoxModule.REGISTRATE.addRawLang(URLDiscItem.CUSTOM_DISC_TRANSLATION_KEY, "Custom Disc")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.LOCKED_DISC_TRANSLATION_KEY, "This disc is signed!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.DOWNLOADING_DISC_TRANSLATION_KEY, "Downloading music, please wait a moment...")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.DOWNLOADING_ERROR_DISC_TRANSLATION_KEY, "Failed to download music!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.DOWNLOADING_SUCCESS_DISC_TRANSLATION_KEY, "Download complete!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.NO_RECORD_TRANSLATION_KEY, "No custom record found!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.YOUTUBE_INVALID_TRANSLATION_KEY, "Song URL is not a valid YouTube link!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.URL_INVALID_TRANSLATION_KEY, "Song URL is invalid!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.URL_TOO_LONG_TRANSLATION_KEY, "Song URL is too long!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.RADIUS_TOO_SMALL_TRANSLATION_KEY, "Radius is too small!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.DISC_SAVED_TRANSLATION_KEY, "Song Details saved!")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.YOUTUBE_URL_TRANSLATION_KEY, "Youtube URL")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.DURATION_TRANSLATION_KEY, "Duration (In Seconds)")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.SONG_NAME_TRANSLATION_KEY, "Song Name")
    MusicBoxModule.REGISTRATE.addRawLang(MusicDiscScreen.RADIUS_TRANSLATION_KEY, "Radius")

    // Music Box Commands
    MusicBoxModule.REGISTRATE.addRawLang(MusicBoxCommands.DOWNLOADING_SONG, "Downloading song %s")
    MusicBoxModule.REGISTRATE.addRawLang(MusicBoxCommands.DOWNLOADING_SONG_ERROR, "Downloading song %s failed")
    MusicBoxModule.REGISTRATE.addRawLang(MusicBoxCommands.DOWNLOADING_SONG_SUCCESS, "Downloading song %s succeeded")
    MusicBoxModule.REGISTRATE.addRawLang(MusicBoxCommands.PLAYING_SONG, "Playing song for %s")
  }
}