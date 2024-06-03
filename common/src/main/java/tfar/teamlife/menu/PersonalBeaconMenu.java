package tfar.teamlife.menu;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.teamlife.init.ModItems;
import tfar.teamlife.init.ModMenus;

public class PersonalBeaconMenu extends AbstractContainerMenu {

    final ItemStack beaconStack;
    private final PaymentSlot paymentSlot;

    private final Container beacon = new SimpleContainer(1) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return stack.is(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    public PersonalBeaconMenu(int id,Inventory inventory) {
        this(id,inventory,new SimpleContainerData(3));
    }



    public PersonalBeaconMenu(int id, Inventory inventory, ContainerData containerData) {
        super(ModMenus.PERSONAL_BEACON,id);
        Player player = inventory.player;
        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.PERSONAL_BEACON)) {
            beaconStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        } else {
            beaconStack = player.getItemInHand(InteractionHand.OFF_HAND);
        }
        this.paymentSlot = new PaymentSlot(this.beacon, 0, 136, 110);
        this.addSlot(this.paymentSlot);
        addDataSlots(containerData);

        int i = 36;
        int j = 137;

        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(inventory, l + k * 9 + 9, 36 + l * 18, i + k * 18));
            }
        }

        for (int i1 = 0; i1 < 9; i1++) {
            this.addSlot(new Slot(inventory, i1, 36 + i1 * 18, 195));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotId == 0) {
                if (!this.moveItemStackTo(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (this.moveItemStackTo(itemstack1, 0, 1, false)) { //Forge Fix Shift Clicking in beacons with stacks larger then 1.
                return ItemStack.EMPTY;
            } else if (slotId >= 1 && slotId < 28) {
                if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotId >= 28 && slotId < 37) {
                if (!this.moveItemStackTo(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return beaconStack.is(ModItems.PERSONAL_BEACON);
    }


    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            ItemStack itemstack = this.paymentSlot.remove(this.paymentSlot.getMaxStackSize());
            if (!itemstack.isEmpty()) {
                player.drop(itemstack, false);
            }
        }
    }

    static class PaymentSlot extends Slot {
        public PaymentSlot(Container p_39071_, int p_39072_, int p_39073_, int p_39074_) {
            super(p_39071_, p_39072_, p_39073_, p_39074_);
        }

        @Override
        public boolean mayPlace(ItemStack p_39077_) {
            return p_39077_.is(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
