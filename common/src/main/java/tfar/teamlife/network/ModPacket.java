package tfar.teamlife.network;

import net.minecraft.network.FriendlyByteBuf;

@FunctionalInterface
public interface ModPacket {
    void write(FriendlyByteBuf to);
}
