package com.minecraftdimensions.bungeesuitechat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.minecraftdimensions.bungeesuitechat.BungeeSuiteChat;
import com.minecraftdimensions.bungeesuitechat.CommandUtil;

public class AfkCommand implements CommandExecutor {

	BungeeSuiteChat plugin;

	private static final String[] PERMISSION_NODES = {
			"bungeesuite.chat.admin", "bungeesuite.chat.*",
			"bungeesuite.admin", "bungeesuite.*" };

	public AfkCommand(BungeeSuiteChat bungeeSuiteChat) {
		plugin = bungeeSuiteChat;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			plugin.utils.getMessage(sender.getName(), "NO_PERMISSION");
			return true;
		}
		plugin.utils.chatSpy(sender.getName());
		return true;
	}

}
