package xenobot.main.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xenobot.main.Constants;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConfessionsManager extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().isWebhookMessage()) {
            return;
        }

        List<Message.Attachment> attachments = event.getMessage().getAttachments();

        if (attachments.size() != 0) {
            event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();
            return;
        }

        if (event.getGuild().getIdLong() == Constants.MAIN_GUILD_ID && event.getChannel().getIdLong() == Constants.CONFESS) {

            event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();



            EmbedBuilder confessionEmbed = new EmbedBuilder();

            if (event.getMessage().getContentRaw().contains("https://discord.gg/")) {
                confessionEmbed.setTitle("⫸ Confessions Manager ⫷");
                confessionEmbed.setDescription("Your confession has not been sent due to detection of a Discord link.");
                confessionEmbed.setColor(Color.MAGENTA);
                confessionEmbed.setFooter("All confessions are anonymous.", event.getJDA().getSelfUser().getAvatarUrl());
                event.getChannel().sendMessage(confessionEmbed.build()).queue(messageSent -> messageSent.delete()
                        .queueAfter(5, TimeUnit.SECONDS));
                return;
            }

            confessionEmbed.setTitle("⫸ Confessions Manager ⫷");
            confessionEmbed.setDescription("Thank you for submitting a confession. It has been received " +
                    "and sent to <#" + Constants.CONFESSIONS + ">.");
            confessionEmbed.setColor(Color.MAGENTA);
            confessionEmbed.setFooter("All confessions are anonymous.", event.getJDA().getSelfUser().getAvatarUrl());
            event.getChannel().sendMessage(confessionEmbed.build()).queue(messageSent -> messageSent.delete()
                    .queueAfter(5, TimeUnit.SECONDS));

            EmbedBuilder confessionLogEmbed = new EmbedBuilder();
            confessionLogEmbed.setTitle("⫸ Confession ⫷");
            confessionLogEmbed.setDescription(event.getMessage().getContentRaw());
            confessionLogEmbed.setColor(Color.MAGENTA);
            confessionLogEmbed.setFooter("All confessions are anonymous.", event.getJDA().getSelfUser().getAvatarUrl());

            event.getGuild().getTextChannelById(Constants.CONFESSIONS).sendMessage(confessionLogEmbed.build()).queue();
        }
    }
}
