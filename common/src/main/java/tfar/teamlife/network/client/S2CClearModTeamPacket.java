package tfar.teamlife.network.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import tfar.teamlife.client.TeamLifeClient;
import tfar.teamlife.network.PacketHandler;

public record S2CClearModTeamPacket() implements S2CModPacket {

    public static final S2CClearModTeamPacket INSTANCE = new S2CClearModTeamPacket();

    public static final Type<S2CClearModTeamPacket> TYPE = new Type<>(PacketHandler.packet(S2CClearModTeamPacket.class));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CClearModTeamPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public void handleClient() {
        TeamLifeClient.setTeam(null);
    }

    @Override
    public Type<S2CClearModTeamPacket> type() {
        return TYPE;
    }


}