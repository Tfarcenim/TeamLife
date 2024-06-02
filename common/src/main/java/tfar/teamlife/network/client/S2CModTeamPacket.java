package tfar.teamlife.network.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import tfar.teamlife.client.TeamLifeClient;
import tfar.teamlife.network.PacketHandler;
import tfar.teamlife.world.ModTeam;

public record S2CModTeamPacket(ModTeam team) implements S2CModPacket {


    public static final Type<S2CModTeamPacket> TYPE = new Type<>(PacketHandler.packet(S2CModTeamPacket.class));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CModTeamPacket> STREAM_CODEC = StreamCodec.composite(
            ModTeam.STREAM_CODEC,
            S2CModTeamPacket::team,
            S2CModTeamPacket::new);

    @Override
    public void handleClient() {
        TeamLifeClient.setTeam(team);
    }

    @Override
    public Type<S2CModTeamPacket> type() {
        return TYPE;
    }


}