package tfar.teamlife.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tfar.parties.client.PartiesClient;
import tfar.teamlife.TeamLife;
import tfar.teamlife.world.ModTeam;

import java.util.Locale;

public record S2CModTeamPacket(ModTeam team) implements CustomPacketPayload {

    public S2CModTeamPacket(FriendlyByteBuf buf) {
        this(ModTeam.fromPacket(buf));
    }


    public static final Type<S2CModTeamPacket> TYPE = new Type<>(packet(S2CModTeamPacket.class));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CModTeamPacket> STREAM_CODEC = StreamCodec.composite(
            ModTeam.STREAM_CODEC,
            S2CModTeamPacket::team,
            S2CModTeamPacket::new);

    @Override
    public void handleClient() {
        PartiesClient.setParty(team);
        PartiesClient.updatePartyScreen();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        team.toPacket(buf);
    }

    @Override
    public Type<S2CModTeamPacket> type() {
        return TYPE;
    }

    static ResourceLocation packet(Class<?> clazz) {
        return TeamLife.id(clazz.getName().toLowerCase(Locale.ROOT));
    }


}