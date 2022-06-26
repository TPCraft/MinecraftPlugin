package cn.tpcraft.minecraft.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
        //检查命令参数
        if (Args.length == 0) {
            //无参数

            //获取语言文件
            List<String> Message = TPCraftPlugin.Message.getStringList("TPCraftPlugin.Info");

            //获取插件信息
            PluginDescriptionFile PluginDescriptionFile = TPCraftPlugin.Plugin.getDescription();

            //发送信息给玩家
            for (int I = 0; Message.size() > I; I++) {
                Sender.sendMessage(
                        translateAlternateColorCodes('&', Message.get(I).replace("{Version}", PluginDescriptionFile.getVersion()))
                );
            }
        } else if (Args.length == 1) {
            //包含一个参数

            switch (Args[0]) {

                //获取帮助
                case "help":

                    //获取语言文件
                    List<String> Message = TPCraftPlugin.Message.getStringList("TPCraftPlugin.HelpList");

                    //发送信息给玩家
                    for (int I = 0; Message.size() > I; I++) {
                        Sender.sendMessage(
                                translateAlternateColorCodes('&', Message.get(I))
                        );
                    }
                    break;

                //重载插件
                case "reload":

                    //检查权限
                    if (Sender.hasPermission("tpcraft.admin")) {
                        //拥有权限

                        //重载配置文件
                        TPCraftPlugin.Plugin.reloadConfig();
                        //读取语言文件
                        File MessageFile = new File(TPCraftPlugin.Plugin.getDataFolder() + "/", "message.yml");
                        TPCraftPlugin.Message = YamlConfiguration.loadConfiguration(MessageFile);

                        //发送信息给玩家
                        Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.TPCraftPlugin") + TPCraftPlugin.Message.getString("TPCraftPlugin.ReloadConfig")));
                    } else {
                        //权限不足

                        //发送信息给玩家
                        Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.TPCraftPlugin") + TPCraftPlugin.Message.getString("TPCraftPlugin.PermissionDenied")));
                    }
                    break;

                //其他参数
                default:

                    //发送信息给玩家
                    Sender.sendMessage(
                            translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.TPCraftPlugin") + TPCraftPlugin.Message.getString("TPCraftPlugin.Help"))
                    );
                    break;
            }
        } else {
            //其他数量参数

            //发送信息给玩家
            Sender.sendMessage(
                    translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.TPCraftPlugin") + TPCraftPlugin.Message.getString("TPCraftPlugin.Help"))
            );
        }
        return false;
    }
}
