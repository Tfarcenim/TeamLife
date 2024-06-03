package tfar.teamlife.network;


import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.MixinEnvironment;
import tfar.teamlife.TeamLife;
import tfar.teamlife.network.client.S2CClearModTeamPacket;
import tfar.teamlife.network.client.S2CModTeamPacket;
import tfar.teamlife.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        //Services.PLATFORM.registerServerPacket(C2SKeyActionPacket.class, C2SKeyActionPacket::new);
        if (Services.PLATFORM.getPlatformName().equals("Forge") || MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
            registerClientPackets();
        }
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientPacket(S2CModTeamPacket.TYPE,S2CModTeamPacket.STREAM_CODEC);
        Services.PLATFORM.registerClientPacket(S2CClearModTeamPacket.TYPE,S2CClearModTeamPacket.STREAM_CODEC);

    }

    public static ResourceLocation packet(Class<?> clazz) {
        return TeamLife.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
