package misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MiscMath {

    public static double clamp(double x, double min, double max) {
        return x > max ? max : (x < min ? min : x);
    }
    public static int secondsToTicks(double seconds) { return (int)(seconds * 20); }

    public static String nameOf(ChatColor color) {
        return color.name().toLowerCase();
    }

    public static String timeLeftAsString(long mills) {
        int minutes = (int)Math.ceil((mills/1000d/60d));
        int minutesModulo = minutes % 60;
        int hours = (int)(minutes/60d);
        String hoursDesc = hours+" hour"+(hours > 1 ? "s" : "");
        String minutesDesc = minutesModulo+" minute"+(minutesModulo > 1 ? "s" : "");
        String desc = (hours > 0 ? hoursDesc : "") + (minutesModulo > 0 ? (hours > 0 ? " and " : "")+minutesDesc : "");
        return desc;
    }

    public static String timeBlockToString(int count) {
        return timeLeftAsString(count*15*60*1000);
    }

}
