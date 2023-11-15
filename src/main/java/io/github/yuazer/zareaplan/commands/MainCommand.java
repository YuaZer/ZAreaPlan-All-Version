package io.github.yuazer.zareaplan.commands;

import io.github.yuazer.zareaplan.Main;
import io.github.yuazer.zareaplan.runnable.PlanRunnable;
import io.github.yuazer.zareaplan.utils.DataLoader;
import io.github.yuazer.zareaplan.utils.YamlUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String abcde, String[] args) {
        if (command.getName().equalsIgnoreCase("zareaplan")) {
            if (args.length == 0 && sender.isOp()) {
                return true;
            }
            if (args[0].equalsIgnoreCase("help")&&sender.isOp()){
                sender.sendMessage("§b/zareaplan §e-> §b/zap");
                sender.sendMessage("§a/zareaplan reloadAll §7- §b重载所有区域方案");
                sender.sendMessage("§a/zareaplan reload [方案名] §7- §b重载指定区域方案");
                sender.sendMessage("§a/zareaplan start [方案名] §7- §b启动指定区域方案");
                sender.sendMessage("§a/zareaplan stop [方案名] §7- §b停止指定区域方案");
                sender.sendMessage("§a[方案名]就是plans文件夹里的文件名§c(不包括.yml)");
                return true;
            }
            if (args[0].equalsIgnoreCase("reloadAll") && sender.isOp()) {
                Main.getInstance().reloadConfig();
                DataLoader.reloadAllPlanAsync();
                sender.sendMessage(YamlUtils.getConfigMessage("Message.reload"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload") && sender.isOp() && args.length == 2) {
                String planName = args[1];
                DataLoader.reloadPlanAsync(planName);
                sender.sendMessage(YamlUtils.getConfigMessage("Message.reload"));
                return true;
            }
            if (args[0].equalsIgnoreCase("start") && sender.isOp() && args.length == 2) {
                String planName = args[1];
                if (!Main.getRunnableManager().getRunnables().containsKey(planName)) {
                    sender.sendMessage(YamlUtils.getConfigMessage("Message.noPlan"));
                    return true;
                }
                if (Main.getRunnableManager().getRunningRunnables().contains(planName)) {
                    sender.sendMessage(YamlUtils.getConfigMessage("Message.alreadyRunning"));
                    return true;
                }
                File file = new File("plugins/ZAreaPlan/plans/" + planName + ".yml");
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
                if (conf.getBoolean("async")) {
                    Main.getRunnableManager().startRunnable(planName, Long.parseLong(conf.getString("delay")), Long.parseLong(conf.getString("period")));
                } else {
                    Main.getRunnableManager().startRunnableSync(planName, Long.parseLong(conf.getString("delay")), Long.parseLong(conf.getString("period")));
                }
                sender.sendMessage(YamlUtils.getConfigMessage("Message.successStart"));
                return true;
            }
            if (args[0].equalsIgnoreCase("stop") && sender.isOp() && args.length == 2) {
                String planName = args[1];
                if (!Main.getRunnableManager().getRunnables().containsKey(planName)) {
                    sender.sendMessage(YamlUtils.getConfigMessage("Message.noPlan"));
                    return true;
                }
                if (!Main.getRunnableManager().getRunningRunnables().contains(planName)) {
                    sender.sendMessage(YamlUtils.getConfigMessage("Message.noRunning"));
                    return true;
                }
                Main.getRunnableManager().stopRunnable(planName);
                Main.getRunnableManager().put(planName, new PlanRunnable(planName));
                sender.sendMessage(YamlUtils.getConfigMessage("Message.successStop"));
                return true;
            }
            return true;
        }
        return false;
    }
}
