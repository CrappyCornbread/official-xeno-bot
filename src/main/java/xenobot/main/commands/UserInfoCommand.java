package xenobot.main.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to mention a user.").queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (mentionedMembers.size() > 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only mention one user at a time.").queue();
            return;
        }
        if (mentionedMembers.size() < 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to mention the user to get the information from.").queue();
            return;
        }

        Member member = mentionedMembers.get(0);
        User user = member.getUser();

        EmbedBuilder userInfoEmbed = new EmbedBuilder();
        userInfoEmbed.setColor(Color.MAGENTA);
        userInfoEmbed.setFooter("User Info requested by " + event.getMember().getEffectiveName(), event.getJDA().getSelfUser().getAvatarUrl());
        userInfoEmbed.addField("Username#Discriminator", String.format("%#s", user), false);
        userInfoEmbed.addField("Display Name", member.getEffectiveName(), false);
        userInfoEmbed.addField("User ID & Mention", String.format("%s (%s)", user.getId(), member.getAsMention()), false);
        userInfoEmbed.addField("Account Created", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), false);
        userInfoEmbed.addField("Joined Guild", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), false);
        userInfoEmbed.addField("Online Status", member.getOnlineStatus().name().toLowerCase().replaceAll("_", " "), false);
        userInfoEmbed.addField("Bot Account", user.isBot() ? "Yes" : "No", false);

        event.getChannel().sendMessage(userInfoEmbed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Displays information about a user.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " @user`";
    }

    @Override
    public String getInvoke() {
        return "userinfo";
    }
}