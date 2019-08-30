package CashCows.node;

import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import CashCows.CashCows;
import org.rspeer.ui.Log;

public class Traverse extends Task {
    @Override
    public boolean validate() {
        return traverseToBank() || traverseToCows();
    }

    @Override
    public int execute() {

        Log.info("Traversing");
        if (Movement.getRunEnergy() > 25 && !Movement.isRunEnabled()) {
            Movement.toggleRun(!Movement.isRunEnabled());
        }

        Movement.walkTo(traverseToBank() ? CashCows.BANK_AREA.getCenter() : CashCows.COW_AREA.getCenter() );
        return 1000;
    }

    private boolean traverseToBank() {
    return Inventory.isFull() && !CashCows.BANK_AREA.contains(Players.getLocal());
    }

    private boolean traverseToCows() {
        return !Inventory.isFull() && !CashCows.COW_AREA.contains(Players.getLocal())
                && !CashCows.COW_FIGHT_AREA.contains(Players.getLocal());
    }

}
