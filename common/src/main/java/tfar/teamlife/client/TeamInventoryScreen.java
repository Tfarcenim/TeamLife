package tfar.teamlife.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tfar.teamlife.menu.TeamInventoryMenu;

public class TeamInventoryScreen extends AbstractContainerScreen<TeamInventoryMenu> {
    private static final ResourceLocation CONTAINER_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    public TeamInventoryScreen(TeamInventoryMenu p_99240_, Inventory p_99241_, Component p_99242_) {
        super(p_99240_, p_99241_, p_99242_);
        this.imageHeight++;
        imageHeight+=54;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics p_281745_, int p_282145_, int p_282358_, float p_283566_) {
        super.render(p_281745_, p_282145_, p_282358_, p_283566_);
        this.renderTooltip(p_281745_, p_282145_, p_282358_);
    }

    @Override
    protected void renderBg(GuiGraphics p_281362_, float p_283080_, int p_281303_, int p_283275_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        p_281362_.blit(CONTAINER_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
