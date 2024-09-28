package com.dannbrown.musicbox.content.items

enum class DiscVariant {
  LIGHT_BLUE,
  CYAN,
  BLUE,
  PURPLE,
  MAGENTA,
  RED,
  ORANGE,
  YELLOW,
  LIME,
  GREEN,
  AMETHYST,
  DIAMOND,
  EMERALD,
  GLIDED,
  LAVA,
  COOKIE,
  CHORUS,
  LAPIS,
  ENDER_EYE,
  ENDER_PEARL,
  MAGMA,
  OCEAN,
  HONEY,
  PETRIFIED,
  MAGMA_CREAM,
  MELON,
  NAUTILUS,
  DUSTY,
  HORNED,
  SHREK,
  GOLDEN,
  COPPER,
  OXIDIZED,
  PRECIPICE,
  MOSSY,
  DRIPSTONE,
  MINECRAFT,
  CREEPER,
  PEWDIEPIE,
  PURPUR;
  fun toInt(): Int {
    return this.ordinal + 1
  }
  companion object {
    fun random(): DiscVariant {
      return values().random()
    }
    fun fromInt(value: Int): DiscVariant {
      return values().getOrElse(value - 1) { RED }
    }
    fun maxVariants(): Int {
      return values().size
    }
  }
}