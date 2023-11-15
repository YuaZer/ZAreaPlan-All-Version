package io.github.yuazer.zareaplan.utils;


import io.github.yuazer.zareaplan.Main;
import java.util.List;

public class YamlUtils {
    public static String getConfigMessage(String path) {
        try {
            return Main.getInstance().getConfig().getString(path).replace("&", "§");
        } catch (NullPointerException e) {
            return "Null Message";
        }
    }

    public static List<String> getConfigStringList(String path) {
        return Main.getInstance().getConfig().getStringList(path);
    }

    public static boolean getConfigBoolean(String path) {
        return Main.getInstance().getConfig().getBoolean(path);
    }

    public static int getConfigInt(String path) {
        return Main.getInstance().getConfig().getInt(path);
    }

    public static double getConfigDouble(String path) {
        return Main.getInstance().getConfig().getDouble(path);
    }
}
