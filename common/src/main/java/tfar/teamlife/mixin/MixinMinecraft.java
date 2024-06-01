package tfar.teamlife.mixin;

import tfar.teamlife.TeamLife;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        
        TeamLife.LOG.info("This line is printed by an example mod common mixin!");
        TeamLife.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}