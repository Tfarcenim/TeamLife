package tfar.teamlife.menu;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import tfar.teamlife.init.ModItems;

public class PersonalBeaconMenu extends BeaconMenu {

    ItemStack beaconStack;

    public PersonalBeaconMenu(int id, Inventory inventory, ContainerData containerData) {
        super(id,inventory,containerData, ContainerLevelAccess.NULL);
        Player player = inventory.player;

        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.PERSONAL_BEACON)) {

            beaconStack = player.getItemInHand(InteractionHand.MAIN_HAND);

        } else {

            beaconStack = player.getItemInHand(InteractionHand.OFF_HAND);

        }
    }

    @Override
    public boolean stillValid(Player $$0) {
        return beaconStack.is(ModItems.PERSONAL_BEACON);
    }
}
