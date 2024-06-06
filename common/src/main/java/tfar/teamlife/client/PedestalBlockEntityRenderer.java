package tfar.teamlife.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import tfar.teamlife.blockentity.PedestalBlockEntity;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

    ItemRenderer itemRenderer;

    public PedestalBlockEntityRenderer(BlockEntityRendererProvider.Context c) {
        itemRenderer = c.getItemRenderer();
    }

    @Override
    public void render(PedestalBlockEntity pedestalBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (!pedestalBlockEntity.item.isEmpty()) {
            poseStack.translate(.5,1.5,.5);
            float rotationspeed = 1;
            long ticks = pedestalBlockEntity.getLevel().getGameTime();


            poseStack.mulPose(Axis.YP.rotation(-rotationspeed* (ticks + v )));
            itemRenderer.renderStatic(pedestalBlockEntity.item, ItemDisplayContext.FIXED, 0xffffff, NO_OVERLAY, poseStack, multiBufferSource, pedestalBlockEntity.getLevel(), 1);
        }
    }
}
