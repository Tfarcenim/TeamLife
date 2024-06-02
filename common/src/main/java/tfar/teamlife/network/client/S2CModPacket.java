package tfar.teamlife.network.client;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface S2CModPacket extends CustomPacketPayload {

    void handleClient();

}
