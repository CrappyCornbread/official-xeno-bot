package xenobot.main.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xenobot.main.Constants;

import java.awt.*;

public class MemberJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        if (event.getGuild().getIdLong() == Constants.MAIN_GUILD_ID) {
            if (event.getMember() == null) {
                return;
            }

            if (event.getMember().getUser().isBot()) {
                return;
            }

            EmbedBuilder joinMessageEmbed = new EmbedBuilder();
            joinMessageEmbed.setDescription("Welcome to **" + event.getJDA().getGuildById(Constants.MAIN_GUILD_ID).getName() + "**! " +
                    "Feel free to chat in the following channels:\n\n <#" + Constants.GENERAL_CHAT + "> __**and**__ <#" + Constants.VC_TEXT + ">.\n\n" +
                    "Member Count: **" + event.getJDA().getGuildById(Constants.MAIN_GUILD_ID).getMembers().size() + " members**");
            joinMessageEmbed.setFooter("These channels will add to your message count and reward you with points at random.", event.getJDA().getSelfUser().getAvatarUrl());
            joinMessageEmbed.setColor(Color.MAGENTA);

            event.getGuild().getTextChannelById(Constants.GENERAL_CHAT).sendMessage(event.getMember().getAsMention()).embed(joinMessageEmbed.build()).queue();
        }
    }
}