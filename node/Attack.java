package CashCows.node;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import CashCows.CashCows;
import org.rspeer.ui.Log;

import java.util.function.Predicate;

public class Attack extends Task {

    private static final String ATTACK_ACTION = "Attack";
    private static final String NPC_NAME = "Cow";

    @Override
    public boolean validate() {
        return shouldAttack() || notLooting();
    }

    @Override
    public int execute() {
        Log.info("Attacking");
        Player local = Players.getLocal();

        final Predicate<Npc> npcPred = x -> x.getName().equals(NPC_NAME)
                && ((x.getTarget() != null && x.getTarget().equals(local)) || x.getTargetIndex() == -1)
                && x.getHealthPercent() > 0;
        Npc targetNpc = Npcs.getNearest(npcPred);

        if (Dialog.canContinue()) {
            Dialog.processContinue();
        }

        //enable run
        if (Movement.getRunEnergy() > 25 && !Movement.isRunEnabled()) {
            Movement.toggleRun(!Movement.isRunEnabled());
        }

        if (local.getTargetIndex() == -1) {
            if (targetNpc != null && !local.isMoving() && targetNpc.interact(ATTACK_ACTION)) {
                Time.sleepUntil(() -> local.getTargetIndex() != -1, 5000);
            }
        }
        Time.sleepUntil(() -> (Pickables.getNearest("Cowhide") != null),5000);

        return 250;
    }

    private boolean shouldAttack(){
        return !Inventory.isFull() && CashCows.COW_FIGHT_AREA.contains(Players.getLocal()) && (Pickables.getNearest("Cowhide") == null);
    }

    private boolean notLooting(){
        Player local = Players.getLocal();
        return !local.isMoving() && !local.isAnimating() && CashCows.COW_FIGHT_AREA.contains(Players.getLocal()) && (Pickables.getNearest("Cowhide") == null);
    }

}
