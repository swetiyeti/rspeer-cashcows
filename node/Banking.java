package CashCows.node;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.task.Task;
import CashCows.CashCows;
import org.rspeer.ui.Log;

import java.awt.*;

public class Banking extends Task implements RenderListener {
    @Override
    public boolean validate() {
        return Inventory.isFull() && CashCows.BANK_AREA.contains(Players.getLocal());
    }

    private int cowhideCount = 0;

    @Override
    public int execute() {
        Log.info("Attempting to bank.");
        if (Bank.isOpen() && Inventory.isFull()){
            if (Inventory.contains("Cowhide")){
                cowhideCount = cowhideCount + Inventory.getCount("Cowhide");
                Time.sleepUntil(()->Bank.depositAll("Cowhide"),2400);
                Log.info("Deposited Cowhide.");
            } else{
                Log.info("You don't have any cowhides but your inventory is full!");
                Time.sleepUntil(Bank::depositInventory,2400);
            }
        } else{
            Time.sleepUntil(Bank::open,2000);
        }

        return 850;
    }

    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics g = renderEvent.getSource();
        String cowhidesHour = String.format("%.2f", CashCows.timer.getHourlyRate(cowhideCount));
        g.drawString("Cowhides collected: " + cowhideCount, 30, 50);
        g.drawString("Cowhides collected per hour: " + cowhidesHour, 30, 70);
    }
}
