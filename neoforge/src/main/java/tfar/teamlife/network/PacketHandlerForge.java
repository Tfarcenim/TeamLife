package tfar.teamlife.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import tfar.teamlife.platform.NeoForgePlatformHelper;

public class PacketHandlerForge {

    public static void register(RegisterPayloadHandlersEvent event) {
        NeoForgePlatformHelper.registrar = event.registrar("packets");
        PacketHandler.registerPackets();
    }


    public static void sendToClient(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player,packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }
}
