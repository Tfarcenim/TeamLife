package tfar.teamlife.network;


import org.spongepowered.asm.mixin.MixinEnvironment;
import tfar.teamlife.platform.Services;

public class PacketHandler {

    public static void registerPackets() {
        //Services.PLATFORM.registerServerPacket(C2SKeyActionPacket.class, C2SKeyActionPacket::new);
        if (Services.PLATFORM.getPlatformName().equals("Forge") || MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
            registerClientPackets();
        }
    }

    public static void registerClientPackets() {
        //Services.PLATFORM.registerClientPacket(S2CFakeEquipmentPacket.class,S2CFakeEquipmentPacket::new);
    }
}
