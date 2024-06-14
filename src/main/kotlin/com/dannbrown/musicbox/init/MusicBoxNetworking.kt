package com.dannbrown.musicbox

import com.dannbrown.databoxlib.content.networking.NetworkPacketBase
import com.dannbrown.musicbox.content.networking.OpenDiscScreenS2CPacket
import com.dannbrown.musicbox.content.networking.PlayCustomDiscS2CPacket
import com.dannbrown.musicbox.content.networking.SaveDiscUrlC2SPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent.Context
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

object MusicBoxNetworking {
  private var INSTANCE: SimpleChannel? = null
  private var packetId = 0
  private fun id(): Int {
    return packetId++
  }

  fun register() {
    val net = NetworkRegistry.ChannelBuilder
      .named(ResourceLocation(MusicBoxModule.MOD_ID, "main"))
      .networkProtocolVersion { "1.0" }
      .clientAcceptedVersions { s: String? -> true }
      .serverAcceptedVersions { s: String? -> true }
      .simpleChannel()
    INSTANCE = net
    // register messages
    PacketType(net, SaveDiscUrlC2SPacket::class.java, ::SaveDiscUrlC2SPacket, NetworkDirection.PLAY_TO_SERVER).register()
    PacketType(net, OpenDiscScreenS2CPacket::class.java, ::OpenDiscScreenS2CPacket, NetworkDirection.PLAY_TO_CLIENT).register()
    PacketType(net, PlayCustomDiscS2CPacket::class.java, ::PlayCustomDiscS2CPacket, NetworkDirection.PLAY_TO_CLIENT).register()
  }

  @JvmStatic
  fun <MSG> sendToServer(message: MSG) {
    INSTANCE!!.sendToServer(message)
  }

  @JvmStatic
  fun <MSG> sendToPlayer(message: MSG, player: ServerPlayer?) {
    INSTANCE!!.send(PacketDistributor.PLAYER.with { player }, message)
  }

  @JvmStatic
  fun <MSG> sendToAllClients(message: MSG) {
    INSTANCE!!.send(PacketDistributor.ALL.noArg(), message)
  }

  private class PacketType<T : NetworkPacketBase>(private val channel: SimpleChannel, private val type: Class<T>, private val factory: Function<FriendlyByteBuf, T>, private val direction: NetworkDirection) {
    private val encoder: BiConsumer<T, FriendlyByteBuf>
    private val decoder: Function<FriendlyByteBuf, T>
    private val handler: BiConsumer<T, Supplier<Context>>

    init {
      encoder = BiConsumer<T, FriendlyByteBuf> { obj: T, buffer: FriendlyByteBuf -> obj.write(buffer) }
      decoder = factory
      handler = BiConsumer<T, Supplier<Context>> { packet, contextSupplier ->
        val context: Context = contextSupplier.get()
        if (packet.handle(context)) {
          context.packetHandled = true
        }
      }
    }

    fun register() {
      channel.messageBuilder(type, index++, direction)
        .encoder(encoder)
        .decoder(decoder)
        .consumerNetworkThread(handler)
        .add()
    }

    companion object {
      private var index = 0
    }
  }
}