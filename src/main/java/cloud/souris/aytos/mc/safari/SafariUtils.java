package cloud.souris.aytos.mc.safari;

public class SafariUtils {
    public static String formatMinecraftWorldTime(int levelTime) {
        int minutes = (levelTime % 1000) * 60 / 1000;
        int hours = (levelTime / 1000) + 6;
        if (hours == 24) {
            hours = 0;
        }

        return String.format("%02d:%02d", hours, minutes); // 4609:22   ????
    }
}
