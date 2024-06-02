package tfar.teamlife.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tfar.teamlife.network.client.S2CModTeamPacket;
import tfar.teamlife.platform.NeoForgePlatformHelper;

public class PacketHandlerForge {

    public static void register(RegisterPayloadHandlersEvent event) {
        NeoForgePlatformHelper.registrar = event.registrar("packets");
        modid.playToClient(S2CModTeamPacket.TYPE, S2CModTeamPacket.STREAM_CODEC, (p,t) -> p.handleClient());
    }


    public static void sendToClient(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player,packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }
}
