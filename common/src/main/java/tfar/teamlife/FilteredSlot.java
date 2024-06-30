package tfar.teamlife;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.teamlife.item.TeamInventoryItem;

public class FilteredSlot extends Slot {
    public FilteredSlot(Container $$0, int $$1, int $$2, int $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Override
    public boolean mayPlace(ItemStack $$0) {
        return !($$0.getItem() instanceof TeamInventoryItem);
    }
}
