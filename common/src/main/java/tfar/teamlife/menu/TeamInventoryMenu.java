package tfar.teamlife.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.teamlife.FilteredSlot;
import tfar.teamlife.init.ModMenus;

public class TeamInventoryMenu extends AbstractContainerMenu {
    private static final int CONTAINER_SIZE = 54;
    private final Container container;

    public TeamInventoryMenu(int p_40188_, Inventory p_40189_) {
        this(p_40188_, p_40189_, new SimpleContainer(CONTAINER_SIZE));
    }

    public TeamInventoryMenu(int p_40191_, Inventory inventory, Container container) {
        super(ModMenus.TEAM_INVENTORY, p_40191_);
        checkContainerSize(container, 54);
        this.container = container;
        container.startOpen(inventory.player);
        int i = 6;
        int j = 9;

        for (int k = 0; k < i; k++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new FilteredSlot(container, l + k * j, 8 + l * 18, 18 + k * 18));
            }
        }

        for (int i1 = 0; i1 < 3; i1++) {
            for (int k1 = 0; k1 < 9; k1++) {
                this.addSlot(new Slot(inventory, k1 + i1 * 9 + 9, 8 + k1 * 18,56+ 84 + i1 * 18));
            }
        }

        for (int j1 = 0; j1 < 9; j1++) {
            this.addSlot(new Slot(inventory, j1, 8 + j1 * 18,56+ 142));
        }
    }

    @Override
    public boolean stillValid(Player p_40195_) {
        return this.container.stillValid(p_40195_);
    }

    @Override
    public ItemStack quickMoveStack(Player p_40199_, int p_40200_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_40200_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_40200_ < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void removed(Player p_40197_) {
        super.removed(p_40197_);
        this.container.stopOpen(p_40197_);
    }
}
