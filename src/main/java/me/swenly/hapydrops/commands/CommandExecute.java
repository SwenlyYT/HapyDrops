package me.swenly.hapydrops.commands;

import me.swenly.hapydrops.HapyDrops;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandExecute implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> commands = new ArrayList<String>(Arrays.asList("add", "set", "remove", "delete", "spawn"));

        if (args.length == 0 || args[0].equals("help")) {
            sender.sendMessage(getHelpMsg());
            return true;
        }

        if (args.length > 1) {
            String chest = args[1];

            if (!HapyDrops.chests.contains(chest) && commands.contains(args[0])) {
                sender.sendMessage("§cОшибка! Название сундука неверное!");
                return true;
            }
        }

        switch (args[0]) {
            case "add":
                if (args.length == 1) {
                    sender.sendMessage("§cОшибка! Укажите сундук!");
                } else if (args.length == 2) {
                    sender.sendMessage("§cОшибка! Укажите шанс выпадения!");
                } else {
                    new AddCMD(sender, command, label, args);
                }
                break;
            case "set":
                if (args.length == 1) {
                    sender.sendMessage("§cОшибка! Укажите сундук!");
                } else if (args.length == 2) {
                    sender.sendMessage("§cОшибка! Укажите шанс выпадения!");
                } else if (args.length == 3) {
                    sender.sendMessage("§cОшибка! Укажите позицию, которая будет заменена!");
                } else {
                    try {
                        Integer.parseInt(args[3]);
                    }
                    catch (NumberFormatException exception) {
                        sender.sendMessage("§cОшибка! Вы указали позицию не в виде числа!");
                    }

                    new SetCMD(sender, command, label, args);
                }
                break;
            case "remove":
            case "delete":
                if (args.length == 1) {
                    sender.sendMessage("§cОшибка! Укажите сундук!");
                } else if (args.length == 2) {
                    sender.sendMessage("§cОшибка! Укажите позицию, которая будет удалена!");
                } else {
                    try {
                        Integer.parseInt(args[2]);
                    }
                    catch (NumberFormatException exception) {
                        sender.sendMessage("§cОшибка! Вы указали позицию не в виде числа!");
                    }

                    new RemoveCMD(sender, command, label, args);
                }
                break;
            case "spawn":
                if (args.length == 1) {
                    sender.sendMessage("§cОшибка! Укажите сундук!");
                } else {
                    new SpawnCMD(sender, command, label, args);
                }
                break;
            default:
                sender.sendMessage(getHelpMsg());
                break;
        }

        return true;
    }

    public String getHelpMsg() {
        return "§7---------- §8[ §eHapyDrops §8] §7----------\" + \"\n§e/drops add <chest> <chance> §8-§7 Добавляет в базу данных предмет в руке с шансом\n§e/drops set <chest> <chance> <position> §8-§7 Заменяет предмет в выбранной позиции\n§e/drops remove <chest> <position> §8-§7 Удаляет предмет в позиции базы данных";
    }
}
