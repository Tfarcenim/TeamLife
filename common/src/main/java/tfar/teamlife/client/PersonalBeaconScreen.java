package tfar.teamlife.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import tfar.teamlife.menu.PersonalBeaconMenu;

public class PersonalBeaconScreen extends AbstractContainerScreen<PersonalBeaconMenu> {
    public PersonalBeaconScreen(PersonalBeaconMenu menu, Inventory inventory, Component $$2) {
        super(menu, inventory, $$2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }
}
