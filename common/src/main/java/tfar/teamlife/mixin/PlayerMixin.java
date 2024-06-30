package tfar.teamlife.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.teamlife.PlayerDuck;
import tfar.teamlife.TeamLife;
import tfar.teamlife.world.ModTeam;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {
    
    @Inject(method = "isHurt", at = @At("RETURN"),cancellable = true)
    private void init(CallbackInfoReturnable<Boolean> cir) {
        ModTeam modTeam = TeamLife.getTeamSideSafe((Player) (Object)this);
        if (!cir.getReturnValue() && modTeam != null && modTeam.maxHealth > modTeam.health) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "aiStep",at = @At("HEAD"))
    private void handle(CallbackInfo ci) {
        if (lastUsedBeacon > 0) {
            lastUsedBeacon--;
        }
    }

    @Unique
    int lastUsedBeacon;

    @Override
    public int lastUsedBeacon() {
        return lastUsedBeacon;
    }

    @Override
    public void setLastUsedBeacon(int cooldown) {
        lastUsedBeacon = cooldown;
    }
}