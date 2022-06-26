package cn.tpcraft.minecraft.plugin.Recharge;

import cn.tpcraft.minecraft.plugin.Function;
import cn.tpcraft.minecraft.plugin.TPCraftPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {

        //检查ApiKey与Url
        if (Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("ApiKey"), "") || Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("Recharge.Command"), "") || Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("Url.Recharge"), "")) {
            //未填写ApiKey或Url

            //发送信息给玩家
            Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("TPCraftPlugin.ConfigIsNull")));
            return false;
        }

        //检查参数
        if (Args.length == 1) {
            //包含一个参数

            //检查参数内容
            if (Objects.equals(Args[0], "exchangerate")) {
                //正确

                //发送信息给玩家
                Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("Recharge.ExchangeRate").replace("{ExchangeRate}", TPCraftPlugin.Plugin.getConfig().getString("Recharge.ExchangeRate"))));
            } else {
                //错误

                //发送信息给玩家
                Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("TPCraftPlugin.Help")));
            }
        } else if (Args.length == 2) {
            //包含两个参数

            //检查参数内容
            if (Objects.equals(Args[0], "add")) {
                //正确

                //初始化充值数额
                Integer NumBer = 0;

                try {
                    //尝试

                    //强制转换Int型数据
                    NumBer = Integer.valueOf(Args[1]);
                } catch (Exception Exception) {
                    //发生错误

                    //发送信息给玩家
                    Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("Recharge.NumBerError")));

                    return false;
                }

                //检查最大最小值
                if (NumBer < 1) {
                    //小于1

                    //发送信息给玩家
                    Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("Recharge.NumBerError")));

                    return false;
                } else if(NumBer > 100000) {
                    //大于100000

                    //发送信息给玩家
                    Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("Recharge.NumBerError")));

                    return false;
                }

                try {
                    //尝试

                    //请求服务器
                    JSONObject Request = Function.HttpGet(TPCraftPlugin.Plugin.getConfig().getString("Url.Recharge") + "?" + "ApiKey=" + TPCraftPlugin.Plugin.getConfig().getString("ApiKey") + "&Server=" + TPCraftPlugin.Plugin.getConfig().getString("Server") + "&Username=" + Sender.getName() + "&NumBer=" + NumBer);

                    //获取返回数据
                    JSONObject RequestData = (JSONObject) Request.get("Data");

                    //检查请求状态
                    if ((boolean)Request.get("Status")) {
                        //返回True

                        //初始化指令
                        String SendCommand = TPCraftPlugin.Plugin.getConfig().getString("Recharge.Command");
                        SendCommand = SendCommand.replace("{Player}", Sender.getName());
                        SendCommand = SendCommand.replace("{Point}", String.valueOf(NumBer));

                        //检查指令发送
                        if (getServer().dispatchCommand(getServer().getConsoleSender(), SendCommand)) {
                            //发送成功

                            //获取语言文件
                            List<String> Message = TPCraftPlugin.Message.getStringList("Recharge.Success");

                            //发送信息给玩家
                            for (int I = 0; Message.size() > I; I++) {
                                Sender.sendMessage(
                                        translateAlternateColorCodes('&', Message.get(I).replace("{Status}", "成功").replace("{NumBer}", NumBer.toString()).replace("{Coin}", RequestData.get("Coin").toString()).replace("{OrderNumber}", RequestData.get("OrderNumber").toString()))
                                );
                            }
                        } else {
                            //发送失败

                            //获取语言文件
                            List<String> Message = TPCraftPlugin.Message.getStringList("Recharge.Fail");

                            //发送信息给玩家
                            for (int I = 0; Message.size() > I; I++) {
                                Sender.sendMessage(
                                        translateAlternateColorCodes('&', Message.get(I).replace("{Status}", "失败").replace("{Reason}", "指令执行失败，请联系管理员。"))
                                );
                            }
                        }
                    } else {
                        //返回False

                        //获取语言文件
                        List<String> Message = TPCraftPlugin.Message.getStringList("Recharge.Fail");

                        //发送信息给玩家
                        for (int I = 0; Message.size() > I; I++) {
                            Sender.sendMessage(
                                    translateAlternateColorCodes('&', Message.get(I).replace("{Status}", "失败").replace("{Reason}", Request.get("Message").toString()))
                            );
                        }
                    }
                } catch (Exception Exception) {
                    //发生错误

                    // 发送信息给控制台
                    getLogger().info(Exception.toString());
                    //发送信息给玩家
                    Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("Error.Player")));
                }
            } else {
                //不包含参数

                //发送信息给玩家
                Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("TPCraftPlugin.Help")));
            }
        } else {
            //无参数

            //发送信息给玩家
            Sender.sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.Recharge") + TPCraftPlugin.Message.getString("TPCraftPlugin.Help")));
        }
        return false;
    }
}
