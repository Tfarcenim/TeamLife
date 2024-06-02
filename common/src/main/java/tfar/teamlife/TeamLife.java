package tfar.teamlife;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.teamlife.init.ModItems;
import tfar.teamlife.item.PersonalHeartItem;
import tfar.teamlife.platform.Services;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class TeamLife {

    public static final String MOD_ID = "teamlife";
    public static final String MOD_NAME = "TeamLife";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        Services.PLATFORM.registerAll(ModItems.class, BuiltInRegistries.ITEM, Item.class);
    }

    //copy personal hearts attribute
    public static void playerClone(Player oldPlayer, Player newPlayer, boolean wasDeath) {
        AttributeInstance attributeInstanceOld = oldPlayer.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance attributeInstanceNew = newPlayer.getAttribute(Attributes.MAX_HEALTH);
        if (attributeInstanceOld != null && attributeInstanceNew != null) {
            AttributeModifier attributeModifierOld = attributeInstanceOld.getModifier(PersonalHeartItem.uuid);
            if (attributeModifierOld != null) {
                attributeInstanceNew.addPermanentModifier(attributeModifierOld);
            }
        }
    }

    public static void onDamageEvent(LivingEntity living, DamageSource damageSource,float amount) {

    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

}
//1 new feature
//11 new items
//1 new block
//
//I am looking for a mod to make teams and a central health bar. It should lets me have four teams with one health bar each. Each teams health bar replaces the normal health bar each player has so if one person takes damage all people on the team take damage.
//
//New items/blocks: personal hearts, team hearts, chest plate with elytra, portable beacon, team inventory, enchant multiplier 1.5, infant rockets with no fall damage, Nether core, Overworld core, End core, Center team peace, and artifact pedestal