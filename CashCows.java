package CashCows;

import CashCows.node.Attack;
import CashCows.node.Banking;
import CashCows.node.Looting;
import CashCows.node.Traverse;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;

import java.awt.*;

@ScriptMeta(name = "Sweti's Cash Cows",  desc = "Kills cows in east Lumby, banks hides", developer = "Sweti Yeti", category = ScriptCategory.COMBAT)
public class CashCows extends TaskScript implements RenderListener {

    private static final Task[] TASKS = { new Traverse(), new Attack(), new Banking(), new Looting()};

    public static final Area BANK_AREA = Area.rectangular(3207, 3221, 3210, 3215, 2); //actual bank area
    public static final Area COW_AREA = Area.rectangular(3265, 3255, 3255, 3296);
    public static final Area COW_FIGHT_AREA = Area.polygonal(
            new Position[] {
                    new Position(3253, 3255, 0),
                    new Position(3266, 3255, 0),
                    new Position(3266, 3297, 0),
                    new Position(3261, 3299, 0),
                    new Position(3241, 3299, 0),
                    new Position(3240, 3296, 0),
                    new Position(3242, 3294, 0),
                    new Position(3242, 3289, 0),
                    new Position(3240, 3286, 0),
                    new Position(3244, 3281, 0),
                    new Position(3248, 3278, 0),
                    new Position(3251, 3278, 0),
                    new Position(3251, 3275, 0),
                    new Position(3253, 3272, 0),
                    new Position(3253, 3255, 0)
            }
    );

    public static StopWatch timer;

    @Override
    public void onStart() {
        timer = StopWatch.start();
        submit(TASKS);
    }

    @Override
    public void onStop() {
        //When the script is stopped the segment of code in this method will be ran once.   
    }

    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics g = renderEvent.getSource();
        g.drawString("Time elapsed: " + timer.toElapsedString(), 30, 30);
    }
}