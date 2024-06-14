package com.dannbrown.musicbox.datagen.lang

import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.init.MusicBoxCreativeTabs
import com.dannbrown.musicbox.main.MusicBoxCommands

object AddonLangGen {
  fun addStaticLangs(doRun: Boolean) {
    if (!doRun) return // avoid running in the server-side
    // Creative tabs
    MusicBoxModule.REGISTRATE.addCreativeTabLang(MusicBoxCreativeTabs.CREATIVE_TAB_KEY, MusicBoxModule.NAME)

    // Music Box Commands
    MusicBoxModule.REGISTRATE.addRawLang(MusicBoxCommands.DOWNLOADING_SONG, "Downloading song %s")
    MusicBoxModule.REGISTRATE.addRawLang(MusicBoxCommands.DOWNLOADING_SONG_ERROR, "Downloading song %s failed")
    MusicBoxModule.REGISTRATE.addRawLang(MusicBoxCommands.DOWNLOADING_SONG_SUCCESS, "Downloading song %s succeeded")
    MusicBoxModule.REGISTRATE.addRawLang(MusicBoxCommands.PLAYING_SONG, "Playing song for  %s")
  }
}