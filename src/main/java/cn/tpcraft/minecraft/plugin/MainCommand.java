package cn.tpcraft.minecraft.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
        //检查命令参数
        if (Args.length == 0) {
            //无参数

        } else if (Args.length == 1) {
            //包含一个参数

        } else {
            //其他数量参数

        }
        return false;
    }
}
