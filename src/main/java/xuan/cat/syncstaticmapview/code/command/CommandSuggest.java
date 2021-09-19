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
            if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.get")) {
                list.add("get");
            }
            if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.create.*") || sender.hasPermission("command.map.create.url") || sender.hasPermission("command.map.create.file") || sender.hasPermission("command.map.create.hand")) {
                list.add("create");
            }
            if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.redirect.*") || sender.hasPermission("command.map.redirect.list") || sender.hasPermission("command.map.redirect.set") || sender.hasPermission("command.map.redirect.delete") || sender.hasPermission("command.map.redirect.delete_all")) {
                list.add("redirect");
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
                case "get":
                    list.add("1");
                    break;
                case "redirect":
                    list.add("1");
                    break;
            }
        } else if (parameters.length == 3) {
            switch (parameters[0]) {
                case "create":
                    switch (parameters[1]) {
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
                case "redirect":
                    if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.redirect.*") || sender.hasPermission("command.map.redirect.list")) {
                        list.add("list");
                    }
                    if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.redirect.*") || sender.hasPermission("command.map.redirect.set")) {
                        list.add("set");
                    }
                    if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.redirect.*") || sender.hasPermission("command.map.redirect.delete")) {
                        list.add("delete");
                    }
                    if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.redirect.*") || sender.hasPermission("command.map.redirect.delete_all")) {
                        list.add("delete_all");
                    }
                    break;
            }
        } else if (parameters.length == 4) {
            switch (parameters[0]) {
                case "create":
                    switch (parameters[1]) {
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
                case "redirect":
                    switch (parameters[2]) {
                        case "list":
                            list.add("");
                            break;
                        case "set":
                            list.add("1");
                            break;
                        case "delete":
                            if (sender.hasPermission("command.map.*") || sender.hasPermission("command.map.redirect.*") || sender.hasPermission("command.map.redirect.delete")) {
                                try {
                                    mapServer.cacheMapRedirects(Integer.parseInt(parameters[1])).redirects.forEach(v -> list.add(v.getPermission()));
                                } catch (NumberFormatException exception) {
                                }
                            }
                            break;
                        case "delete_all":
                            list.add("");
                            break;
                    }
                    break;
            }
        } else if (parameters.length == 5) {
            switch (parameters[0]) {
                case "redirect":
                    switch (parameters[2]) {
                        case "set":
                            list.add("permission");
                            break;
                    }
                    break;
            }
        } else if (parameters.length == 6) {
            switch (parameters[0]) {
                case "redirect":
                    switch (parameters[2]) {
                        case "set":
                            list.add("1");
                            break;
                    }
                    break;
            }
        }

        return list;
    }
}
