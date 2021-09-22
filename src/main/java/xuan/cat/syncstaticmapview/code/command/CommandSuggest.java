package xuan.cat.syncstaticmapview.code.command;

import org.bukkit.Bukkit;
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
            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.reload")) {
                list.add("reload");
            }
            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.get")) {
                list.add("get");
            }
            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.create.*") || sender.hasPermission("command.mapview.create.url") || sender.hasPermission("command.mapview.create.file") || sender.hasPermission("command.mapview.create.hand")) {
                list.add("create");
            }
            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.redirect.*") || sender.hasPermission("command.mapview.redirect.list") || sender.hasPermission("command.mapview.redirect.set") || sender.hasPermission("command.mapview.redirect.delete") || sender.hasPermission("command.mapview.redirect.delete_all")) {
                list.add("redirect");
            }
            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.set") || sender.hasPermission("command.mapview.limit.increase") || sender.hasPermission("command.mapview.limit.subtract")) {
                list.add("limit");
            }
        } else if (parameters.length == 2) {
            switch (parameters[0]) {
                case "create":
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.create.*") || sender.hasPermission("command.mapview.create.url")) {
                        list.add("url");
                    }
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.create.*") || sender.hasPermission("command.mapview.create.file")) {
                        list.add("file");
                    }
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.create.*") || sender.hasPermission("command.mapview.create.hand")) {
                        list.add("hand");
                    }
                    break;
                case "get":
                    list.add("1");
                    break;
                case "redirect":
                    list.add("1");
                    break;
                case "limit":
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.set")) {
                        list.add("set");
                    }
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.increase")) {
                        list.add("increase");
                    }
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.subtract")) {
                        list.add("subtract");
                    }
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.check_own") || sender.hasPermission("command.mapview.limit.check_all")) {
                        list.add("check");
                    }
                    break;
            }
        } else if (parameters.length == 3) {
            switch (parameters[0]) {
                case "create":
                    switch (parameters[1]) {
                        case "url":
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.create.*") || sender.hasPermission("command.mapview.create.url")) {
                                list.add("1:1");
                            }
                            break;
                        case "file":
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.create.*") || sender.hasPermission("command.mapview.create.file")) {
                                list.add("1:1");
                            }
                            break;
                        case "hand":
                            list.add("");
                            break;
                    }
                    break;
                case "redirect":
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.redirect.*") || sender.hasPermission("command.mapview.redirect.list")) {
                        list.add("list");
                    }
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.redirect.*") || sender.hasPermission("command.mapview.redirect.set")) {
                        list.add("set");
                    }
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.redirect.*") || sender.hasPermission("command.mapview.redirect.delete")) {
                        list.add("delete");
                    }
                    if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.redirect.*") || sender.hasPermission("command.mapview.redirect.delete_all")) {
                        list.add("delete_all");
                    }
                    break;
                case "limit":
                    switch (parameters[1]) {
                        case "set":
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.set")) {
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                    list.add(player.getName());
                                    list.add(player.getUniqueId().toString());
                                });
                            }
                            break;
                        case "increase":
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.increase")) {
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                    list.add(player.getName());
                                    list.add(player.getUniqueId().toString());
                                });
                            }
                            break;
                        case "subtract":
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.subtract")) {
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                    list.add(player.getName());
                                    list.add(player.getUniqueId().toString());
                                });
                            }
                            break;
                        case "check":
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.check_own")) {
                                list.add("");
                            }
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.limit.*") || sender.hasPermission("command.mapview.limit.check_all")) {
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                    list.add(player.getName());
                                    list.add(player.getUniqueId().toString());
                                });
                            }
                            break;
                    }
                    break;
            }
        } else if (parameters.length == 4) {
            switch (parameters[0]) {
                case "create":
                    switch (parameters[1]) {
                        case "url":
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.create.*") || sender.hasPermission("command.mapview.create.url")) {
                                list.add("http://");
                                list.add("https://");
                            }
                            break;
                        case "file":
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.create.*") || sender.hasPermission("command.mapview.create.file")) {
                                list.add("./");
                            }
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
                            if (sender.hasPermission("command.mapview.*") || sender.hasPermission("command.mapview.redirect.*") || sender.hasPermission("command.mapview.redirect.delete")) {
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
                case "limit":
                    switch (parameters[1]) {
                        case "set":
                            list.add("1");
                            break;
                        case "increase":
                            list.add("1");
                            break;
                        case "subtract":
                            list.add("1");
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
