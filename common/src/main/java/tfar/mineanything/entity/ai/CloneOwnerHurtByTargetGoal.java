package tfar.mineanything.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import tfar.mineanything.entity.ClonePlayerEntity;

import java.util.EnumSet;

public class CloneOwnerHurtByTargetGoal extends TargetGoal {
    private final ClonePlayerEntity clonePlayer;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public CloneOwnerHurtByTargetGoal(ClonePlayerEntity clonePlayer) {
        super(clonePlayer, false);
        this.clonePlayer = clonePlayer;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        if (true/*!this.clonePlayer.isOrderedToSit()*/) {
            LivingEntity owner = this.clonePlayer.getOwner();
            if (owner == null) {
                return false;
            } else {
                this.ownerLastHurtBy = owner.getLastHurtByMob();
                int $$1 = owner.getLastHurtByMobTimestamp();
                return $$1 != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) /*&& this.clonePlayer.wantsToAttack(this.ownerLastHurtBy, owner)*/;
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        LivingEntity $$0 = this.clonePlayer.getOwner();
        if ($$0 != null) {
            this.timestamp = $$0.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}
