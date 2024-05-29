package tfar.mineanything.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import tfar.mineanything.entity.ClonePlayerEntity;

import java.util.EnumSet;

public class CloneOwnerHurtTargetGoal extends TargetGoal {
    private final ClonePlayerEntity clonePlayer;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public CloneOwnerHurtTargetGoal(ClonePlayerEntity clonePlayer) {
        super(clonePlayer, false);
        this.clonePlayer = clonePlayer;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        if (/* !this.clonePlayer.isOrderedToSit()*/true) {
            LivingEntity livingentity = this.clonePlayer.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.ownerLastHurt = livingentity.getLastHurtMob();
                int i = livingentity.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) /*&& this.clonePlayer.wantsToAttack(this.ownerLastHurt, livingentity)*/;
            }
        } else {
            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        LivingEntity livingentity = this.clonePlayer.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtMobTimestamp();
        }

        super.start();
    }
}
