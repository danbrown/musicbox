package com.dannbrown.musicbox.init

import com.dannbrown.musicbox.content.gui.MusicDiscMenu
import com.dannbrown.musicbox.init.ModContent.REGISTRATE

object ModScreens {
  val MUSIC_DISC_MENU = REGISTRATE.menu<MusicDiscMenu>("music_disc_menu") { id, inventory, et -> MusicDiscMenu(id) }

  fun register() {
    REGISTRATE.buildMenus()
  }
}