package CashCows.node;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Pickable;
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

    public static final String ATTACK_ACTION = "Attack";
    public static final String NPC_NAME = "Cow";

    @Override
    public boolean validate() {
        return !Inventory.isFull() && CashCows.COW_FIGHT_AREA.contains(Players.getLocal());
    }

    @Override
    public int execute() {
        Log.info("The loop started.");
        Player local = Players.getLocal();
        boolean fightStarted;
        boolean fightOver = false;
        final long sleepMin = 750;
        final long sleepMax = 1000;

        final Predicate<Npc> npcPred = x -> x.getName().equals(NPC_NAME)
                && ((x.getTarget() != null && x.getTarget().equals(local)) || x.getTargetIndex() == -1)
                && x.getHealthPercent() > 0;
        Npc targetNpc = Npcs.getNearest(npcPred);

        if (Dialog.canContinue()) {
            Dialog.processContinue();
        }

        //enable run
        if (Movement.getRunEnergy() > 25 && !Movement.isRunEnabled())
        {
            Movement.toggleRun(!Movement.isRunEnabled());
        }

        //find near cowhide
        final Pickable cowhide = Pickables.getNearest("Cowhide");
        final int invNum = Inventory.getCount();
        //determine if cowhide is close enough to take
        if (cowhide != null && !Inventory.isFull() && !local.isAnimating() && !local.isMoving() ){
            if (Movement.isWalkable(cowhide)) {
                Log.info("Looting Cowhide.");
                cowhide.interact("Take");
                //don't do anything until you have the cowhide
                Time.sleepUntil(() -> (Inventory.getCount() > invNum), 2500);
            }
        }



        if (local.getTargetIndex() == -1) {
            Log.info("The if statement started executing.");
            if (targetNpc != null && !local.isMoving() && targetNpc.interact(ATTACK_ACTION)) {
                Time.sleep(sleepMin, sleepMax);
                /*
                //used to determine if target NPC is dead
                if (targetNpc.getAnimation() == 5851) {
                    fightOver = true;
                }
                */

                Time.sleepUntil(() -> local.getTargetIndex() != -1, 2500);
                Log.info("The if statement finished executing.");
            }
        }


        return 600;
    }

    /*// used to determine if cowhide is within reach.
    private boolean canLoot(Positionable a) {
        Player local = Players.getLocal();
        if ((Distance.between(local.getPosition(), a)) < 2){
            return true;
        } else return false;
    }
    */


    /*
    //used too loot. pass cowhide as the Pickable.
    private boolean looting(Pickable a){
        int invNum = 0;
        invNum = Inventory.getCount();
        if (a != null && !Inventory.isFull()) {
            if (Movement.isWalkable(a)) {
                Log.info("Looting Cowhide.");
                a.interact("Take");
                if (Inventory.getCount() > invNum){
                    return true;
                }
            }
        }
        return false;
    }
    */

}
