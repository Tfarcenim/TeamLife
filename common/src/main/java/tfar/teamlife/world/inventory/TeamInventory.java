package tfar.teamlife.world.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tfar.teamlife.init.ModItems;
import tfar.teamlife.item.TeamInventoryItem;

public class TeamInventory extends SimpleContainer {

    public TeamInventory() {
        super(54);
    }

    @Override
    public void fromTag(ListTag p_40108_, HolderLookup.Provider p_330977_) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setItem(i, ItemStack.EMPTY);
        }

        for (int k = 0; k < p_40108_.size(); k++) {
            CompoundTag compoundtag = p_40108_.getCompound(k);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 0 && j < this.getContainerSize()) {
                this.setItem(j, ItemStack.parse(p_330977_, compoundtag).orElse(ItemStack.EMPTY));
            }
        }
    }

    @Override
    public ListTag createTag(HolderLookup.Provider provider) {
        ListTag listtag = new ListTag();

        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                listtag.add(itemstack.save(provider, compoundtag));
            }
        }
        return listtag;
    }

    @Override
    public boolean canPlaceItem(int $$0, ItemStack stack) {
        return !(stack.getItem()instanceof TeamInventoryItem) && super.canPlaceItem($$0, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.TEAM_INVENTORY_POUCH) || player.getItemInHand(InteractionHand.OFF_HAND).is(ModItems.TEAM_INVENTORY_POUCH);
    }

}
