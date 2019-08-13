package CashCows.node;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import CashCows.CashCows;
import org.rspeer.ui.Log;

public class Banking extends Task {
    @Override
    public boolean validate() {
        return Inventory.isFull() && CashCows.BANK_AREA.contains(Players.getLocal());
    }

    @Override
    public int execute() {

        Log.info("Attempting to bank.");
        if (Bank.isOpen() && Inventory.isFull()){
            Bank.depositAll("Cowhide");
            Log.info("Deposited Cowhide.");
        } else{
            final SceneObject Bank_Booth = SceneObjects.getNearest("Bank booth");
            if (Bank_Booth != null) {
                Bank_Booth.interact(a -> true);
            }
            //deposit all? check if bank is open and then deposit?
        }

        return 650;
    }
}
