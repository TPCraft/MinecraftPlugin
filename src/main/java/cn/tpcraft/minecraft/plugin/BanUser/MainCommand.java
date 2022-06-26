package cn.tpcraft.minecraft.plugin.BanUser;

import cn.tpcraft.minecraft.plugin.Function;
import cn.tpcraft.minecraft.plugin.TPCraftPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Objects;

import static org.bukkit.Bukkit.*;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {

        //检查权限
        if (Sender.hasPermission("banuser.admin")) {
            //拥有权限

            //检查ApiKey与Url
            if (Objects.equals(TPCraftPlugin.Plugin.getConfig().get("ApiKey").toString(), "") || Objects.equals(TPCraftPlugin.Plugin.getConfig().get("Url.BanUser").toString(), "")) {
                //未填写ApiKey或Url

                //发送信息给玩家
                Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.get("Prefix.BanUser").toString() + TPCraftPlugin.Message.get("TPCraftPlugin.ConfigIsNull").toString()));
                return false;
            }

            //检查参数
            if (Args.length == 3) {
                //包含一个参数

                try {
                    //尝试

                    //请求服务器
                    JSONObject Request = new Function().HttpGet(
                            TPCraftPlugin.Plugin.getConfig().get("Url.BanUser") + "?" + "ApiKey=" + TPCraftPlugin.Plugin.getConfig().get("ApiKey") + "&Username=" + Args[0] + "&Reason=" + Args[1] + "&Time=" + Args[2]
                    );
                    //检查请求状态
                    if ((boolean)Request.get("Status")) {
                        //返回True

                        //获取玩家
                        Player Player = getPlayer(Args[0]);
                        //检查玩家是否在线
                        if (Player != null) {
                            //玩家在线

                            //获取语言文件
                            String Message = TPCraftPlugin.Message.get("BanUser.Kick").toString();

                            //替换变量
                            Message = Message.replace("{Reason}", Args[1]);
                            Message = Message.replace("{UnBanDate}", (!Objects.equals(Args[2], "-1") ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(((System.currentTimeMillis() / 1000 + Integer.parseInt(Args[2])) * 1000)) : "永不解封"));

                            //踢出封禁玩家
                            Player.kickPlayer(
                                    translateAlternateColorCodes('&',
                                            TPCraftPlugin.Message.get("Prefix.BanUser").toString() + Message
                                    )
                            );
                        }
                        //广播
                        broadcastMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.get("Prefix.BanUser").toString() + TPCraftPlugin.Message.get("BanUser.Broadcast").toString().replace("{Player}", Args[0])));
                        //发送信息给玩家
                        Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.get("Prefix.BanUser").toString() + TPCraftPlugin.Message.get("BanUser.Success").toString()));
                    } else {
                        //返回False

                        //发送信息给玩家
                        Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.get("Prefix.BanUser").toString() + TPCraftPlugin.Message.get("BanUser.Fail").toString().replace("{Message}", Request.get("Message").toString())));
                    }
                } catch (Exception Exception) {
                    //发生错误

                    //发送信息给控制台
                    getLogger().warning(Exception.toString());
                    //发送信息给玩家
                    Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.get("Prefix.BanUser").toString() + TPCraftPlugin.Message.get("Error.Admin").toString()));
                }
            } else {
                //无参数

                //发送信息给玩家
                Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.get("Prefix.BanUser").toString() + TPCraftPlugin.Message.get("BanUser.Help").toString()));
            }
        } else {
            //权限不足

            //发送信息给玩家
            Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.get("Prefix.BanUser").toString() + TPCraftPlugin.Message.get("TPCraftPlugin.PermissionDenied").toString()));
        }
        return false;
    }
}
