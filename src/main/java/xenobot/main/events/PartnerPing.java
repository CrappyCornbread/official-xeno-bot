package xenobot.main.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xenobot.main.Constants;

import java.awt.*;

public class PartnerPing extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getGuild().getIdLong() == Constants.MAIN_GUILD_ID) {
            if (event.getChannel().getIdLong() == Constants.PARTNERS_PING) {
                EmbedBuilder partnerPingEmbed = new EmbedBuilder();
                if (event.getMessage().mentionsEveryone()) {
                    partnerPingEmbed.setTitle("⫸ Partner Manager ⫷");
                    partnerPingEmbed.setDescription("**Don't want to receive partner ping anymore?**\n\n" +
                            "Go to the <#" + Constants.ASSIGN_ROLES + "> and react to the `No Server Partner Ping` role.");
                    partnerPingEmbed.setColor(Color.MAGENTA);
                    partnerPingEmbed.setFooter("Bot Developed by " + Constants.DEVELOPER + " - Version " +
                            Constants.VERSION, event.getJDA().getSelfUser().getAvatarUrl());
                    event.getChannel().sendMessage(partnerPingEmbed.build()).queue();
                }
            }
        }
    }
}
