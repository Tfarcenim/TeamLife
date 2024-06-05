package tfar.teamlife.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import tfar.teamlife.init.ModItems;

public class ChestplateElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T,M> {
    public ChestplateElytraLayer(RenderLayerParent<T, M> $$0, EntityModelSet entityModelSet) {
        super($$0,entityModelSet);
    }

    //forge patch
    /**
     * Determines if the ElytraLayer should render.
     * ItemStack and Entity are provided for modder convenience,
     * For example, using the same ElytraLayer for multiple custom Elytra.
     *
     * @param stack  The Elytra ItemStack
     * @param entity The entity being rendered.
     * @return If the ElytraLayer should render.
     */
    public boolean shouldRender(ItemStack stack, T entity) {
        return stack.getItem() == ModItems.NETHERITE_CHESTPLATE_WITH_ELYTRA;
    }

}