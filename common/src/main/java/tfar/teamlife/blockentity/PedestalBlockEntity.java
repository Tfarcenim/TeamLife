package tfar.teamlife.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tfar.teamlife.init.ModBlockEntities;
import tfar.teamlife.item.Artifact;
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

import java.util.UUID;

public class PedestalBlockEntity extends BlockEntity {

    public ItemStack item = ItemStack.EMPTY;
    @Nullable public UUID owners;

    public PedestalBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    public PedestalBlockEntity(BlockPos $$1, BlockState $$2) {
        this(ModBlockEntities.PEDESTAL, $$1, $$2);
    }

    public void setOwners(@Nullable UUID owners) {
        this.owners = owners;
        setChanged();
    }

    public void setItem(ItemStack item) {
        onItemChange(this.item.getItem(),item.getItem());
        this.item = item;
        setChanged();
    }

    public void onItemChange(Item oldItem, Item newItem) {
        if (oldItem != newItem) {
            ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(level.getServer());
            if (modTeamsServer != null) {
                ModTeam modTeam = modTeamsServer.findTeamByUUID(owners);
                if (modTeam != null) {
                    if (oldItem instanceof Artifact artifact) {
                        modTeam.removeArtifact(artifact);
                    }

                    if (newItem instanceof Artifact artifact) {
                        modTeam.addArtifact(artifact);
                    }
                    modTeamsServer.updateTeam(modTeam);
                    modTeamsServer.setDirty();
                }
            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void setChanged() {
        super.setChanged();
        //let the client know the block changed
        level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider $$0) {
        return saveCustomOnly($$0);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!item.isEmpty()) {
            tag.put("Item", item.save(provider));
        }
        if (owners != null) {
            tag.putUUID("Owners",owners);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Item", Tag.TAG_COMPOUND)) {
            CompoundTag compoundtag = tag.getCompound("Item");
            item = ItemStack.parse(provider, compoundtag).orElse(ItemStack.EMPTY);
        } else {
            item = ItemStack.EMPTY;
        }
        if (tag.hasUUID("Owners")) {
            owners = tag.getUUID("Owners");
        }

    }


    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        CompoundTag compoundtag = pkt.getTag();
        loadWithComponents(compoundtag, lookupProvider);
    }
}
