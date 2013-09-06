package com.minecraftdimensions.bungeesuitechat.listeners;

import com.minecraftdimensions.bungeesuitechat.BungeeSuiteChat;
import com.minecraftdimensions.bungeesuitechat.managers.ChannelManager;
import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;
import com.minecraftdimensions.bungeesuitechat.managers.PrefixSuffixManager;
import com.minecraftdimensions.bungeesuitechat.objects.BSPlayer;
import com.minecraftdimensions.bungeesuitechat.objects.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived( String pluginChannel, Player receiver, byte[] message ) {
        if ( !pluginChannel.equalsIgnoreCase( BungeeSuiteChat.INCOMING_PLUGIN_CHANNEL ) )
            return;
        DataInputStream in = new DataInputStream( new ByteArrayInputStream( message ) );
        String channel = null;
        try {
            channel = in.readUTF();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        if ( channel.equals( "SendGlobalChat" ) ) {
            try {
                ChannelManager.getGlobalChat( in.readUTF(), in.readUTF() );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            return;
        }
        if ( channel.equals( "SendAdminChat" ) ) {
            try {
                ChannelManager.getAdminlChat( in.readUTF() );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            return;
        }
        if ( channel.equals( "SendPlayer" ) ) {
            try {
                PlayerManager.addPlayer( new BSPlayer( in.readUTF() ) );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            return;
        }
        if ( channel.equals( "SendChannel" ) ) {
            System.out.println( "Got channels!" );
            try {
                ChannelManager.addChannel( in.readUTF() );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            return;
        }
        if ( channel.equals( "SendServerData" ) ) {
            try {
                ServerData.deserialise( in.readUTF() );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            return;
        }
        if ( channel.equals( "PrefixesAndSuffixes" ) ) {

            boolean check;
            String group;
            String affix;
            try {
                check = in.readBoolean();
                group = in.readUTF();
                affix = in.readUTF();
                if ( check ) {
                    PrefixSuffixManager.prefixes.put( group, affix );
                    PrefixSuffixManager.prefix = true;
                    Bukkit.getPluginManager().addPermission( new Permission( "bungeesuite.chat.prefix." + group, "Gives access to the " + affix + " prefix", PermissionDefault.FALSE ) );
                } else {
                    PrefixSuffixManager.suffixes.put( group, affix );
                    PrefixSuffixManager.suffix = true;
                    Bukkit.getPluginManager().addPermission( new Permission( "bungeesuite.chat.suffix." + group, "Gives access to the " + affix + " suffix", PermissionDefault.FALSE ) );
                }
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        }
        if ( channel.equals( "SendPlayersIgnores" ) ) {
            String player = null;
            String ignoresString[] = null;
            try {
                player = in.readUTF();
                ignoresString = in.readUTF().split( "%" );
            } catch ( IOException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final ArrayList<String> ignores = new ArrayList<>();
            for ( String s : ignoresString ) {
                ignores.add( s );
            }
            final String name = player;
            BSPlayer p = PlayerManager.getPlayer( player );
            if ( p != null ) {
                p.setIgnores( ignores );
            } else {
                Bukkit.getScheduler().runTaskLaterAsynchronously( BungeeSuiteChat.instance, new Runnable() {

                    @Override
                    public void run() {
                        PlayerManager.getPlayer( name ).setIgnores( ignores );
                    }

                }, 10L );
            }
            return;
        }
        if ( channel.equals( "ReloadChat" ) ) {
            ChannelManager.reload();
            return;
        }


    }

}
