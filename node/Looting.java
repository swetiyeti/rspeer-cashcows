package CashCows.node;

import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class Looting extends Task {


    @Override
    public boolean validate() {
        return canLoot() && shouldLoot();
    }

    @Override
    public int execute() {
        final Pickable cowhide = Pickables.getNearest("Cowhide");
        //enable run
        if (Movement.getRunEnergy() > 25 && !Movement.isRunEnabled()) {
            Movement.toggleRun(!Movement.isRunEnabled());
        }
        if ((cowhide != null) && Movement.isWalkable(cowhide.getPosition())){
            Log.info("Looting Cowhide.");
            Log.info("Cowhide at x:" + cowhide.getSceneX() + " y: " + cowhide.getSceneY());
            //don't do anything until you have the cowhide
            int invNum = Inventory.getCount("Cowhide");
            Time.sleepUntil(()->cowhide.interact("Take"), 10000);
            Time.sleepUntil(() -> (Inventory.getCount() > invNum), 5000);
        }
        return 500;
    }

    private boolean canLoot(){
        return (Pickables.getNearest("Cowhide") != null) && Movement.isWalkable(Pickables.getNearest("Cowhide"))
                    && !Inventory.isFull();
    }

    private boolean shouldLoot(){
        Player local = Players.getLocal();
        return (local.getTargetIndex() == -1);
    }
}
