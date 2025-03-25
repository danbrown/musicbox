package com.dannbrown.musicbox.content.gui

import com.dannbrown.musicbox.init.MusicBoxScreens
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class MusicDiscMenu(containerId: Int) : AbstractContainerMenu(MusicBoxScreens.MUSIC_DISC_MENU.get(), containerId) {
  var stack: ItemStack? = null

  constructor(containerId: Int, stack: ItemStack) : this(containerId) {
    this.stack = stack
  }

  override fun quickMoveStack(p0: Player, p1: Int): ItemStack {
    return ItemStack.EMPTY
  }

  override fun stillValid(player: Player): Boolean {
    return true
  }
}