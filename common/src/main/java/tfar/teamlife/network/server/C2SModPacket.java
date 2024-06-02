package tfar.teamlife.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.teamlife.network.ModPacket;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
