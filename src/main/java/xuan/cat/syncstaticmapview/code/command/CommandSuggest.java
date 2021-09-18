package xuan.cat.syncstaticmapview.code.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import xuan.cat.syncstaticmapview.code.MapServer;
import xuan.cat.syncstaticmapview.code.data.ConfigData;

import java.util.ArrayList;
import java.util.List;

public final class CommandSuggest implements TabCompleter {
    private final MapServer     mapServer;
    private final ConfigData    configData;

    public CommandSuggest(MapServer mapServer, ConfigData configData) {
        this.mapServer      = mapServer;
        this.configData     = configData;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] parameters) {
        List<String> list = new ArrayList<>();

        if (parameters.length == 1) {
            if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.reload")) {
                list.add("reload");
            }
            if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.create.*") || sender.hasPermission("command.map.create.url") || sender.hasPermission("command.map.create.file") || sender.hasPermission("command.map.create.hand")) {
                list.add("create");
            }
        } else if (parameters.length == 2) {
            switch (parameters[0]) {
                case "create":
                    if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.create.*") || sender.hasPermission("command.map.create.url")) {
                        list.add("url");
                    }
                    if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.create.*") || sender.hasPermission("command.map.create.file")) {
                        list.add("file");
                    }
                    if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.create.*") || sender.hasPermission("command.map.create.hand")) {
                        list.add("hand");
                    }
                    break;
            }
        } else if (parameters.length == 3) {
            switch (parameters[1]) {
                case "create":
                    switch (parameters[2]) {
                        case "url":
                            if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.create.*") || sender.hasPermission("command.map.create.url")) {
                                list.add("http://");
                                list.add("https://");
                            }
                            break;
                        case "file":
                            if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.create.*") || sender.hasPermission("command.map.create.file")) {
                                list.add("./");
                            }
                            break;
                        case "hand":
                            list.add("");
                            break;
                    }
                    break;
            }
        }

        return list;
    }
}
