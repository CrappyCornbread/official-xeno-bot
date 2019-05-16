package xenobot.main.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xenobot.main.Constants;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SuggestionsManager extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getGuild().getIdLong() == Constants.MAIN_GUILD_ID && event.getChannel().getIdLong() == Constants.SUGGEST_HERE) {

            if (event.getAuthor().getIdLong() == Constants.BOT_ID) {
                return;
            }

            event.getMessage().delete().queue();

            if (event.getAuthor().isBot()) {
                return;
            }

            if (event.getMessage().isWebhookMessage()) {
                return;
            }

            EmbedBuilder suggestionEmbed = new EmbedBuilder();

            List<Member> membersMentions = event.getMessage().getMentionedMembers();

            if (membersMentions.size() != 0) {
                suggestionEmbed.setTitle("⫸ Suggestions Manager ⫷");
                suggestionEmbed.setDescription("You are not allowed to mentions members when sending a suggestion.");
                suggestionEmbed.setColor(Color.MAGENTA);
                suggestionEmbed.setFooter("Contact the bot developer if there is an issue.", event.getJDA().getSelfUser().getAvatarUrl());
                event.getChannel().sendMessage(suggestionEmbed.build()).queue(messageSent -> messageSent.delete()
                        .queueAfter(5, TimeUnit.SECONDS));
                return;
            }

            List<Message.Attachment> attachments = event.getMessage().getAttachments();

            if (attachments.size() != 0) {
                suggestionEmbed.setTitle("⫸ Suggestions Manager ⫷");
                suggestionEmbed.setDescription("You are not allowed to attach images or files when sending a suggestion.");
                suggestionEmbed.setColor(Color.MAGENTA);
                suggestionEmbed.setFooter("Contact the bot developer if there is an issue.", event.getJDA().getSelfUser().getAvatarUrl());
                event.getChannel().sendMessage(suggestionEmbed.build()).queue(messageSent -> messageSent.delete()
                        .queueAfter(5, TimeUnit.SECONDS));
                return;
            }

            List<String> invites = event.getMessage().getInvites();

            if (invites.size() != 0) {
                suggestionEmbed.setTitle("⫸ Suggestions Manager ⫷");
                suggestionEmbed.setDescription("Your suggestion has not been sent due to detection of a Discord link.");
                suggestionEmbed.setColor(Color.MAGENTA);
                suggestionEmbed.setFooter("Contact the bot developer if there is an issue.", event.getJDA().getSelfUser().getAvatarUrl());
                event.getChannel().sendMessage(suggestionEmbed.build()).queue(messageSent -> messageSent.delete()
                        .queueAfter(5, TimeUnit.SECONDS));
                return;
            }

            suggestionEmbed.setTitle("⫸ Suggestions Manager ⫷");
            suggestionEmbed.setDescription("Thank you for submitting a suggestion. It has been received " +
                    "and sent to <#" + Constants.SUGGEST_LOG + ">.");
            suggestionEmbed.setColor(Color.MAGENTA);
            suggestionEmbed.setFooter("All suggestions are made public.", event.getJDA().getSelfUser().getAvatarUrl());
            event.getChannel().sendMessage(suggestionEmbed.build()).queue(messageSent -> messageSent.delete()
                    .queueAfter(5, TimeUnit.SECONDS));

            EmbedBuilder suggestionLogEmbed = new EmbedBuilder();
            suggestionLogEmbed.setTitle("⫸ Suggestion ⫷");
            suggestionLogEmbed.setDescription(event.getMessage().getContentRaw());
            suggestionLogEmbed.setColor(Color.MAGENTA);
            suggestionLogEmbed.setFooter("Your suggestion will be read and processed based on order received.", event.getJDA().getSelfUser().getAvatarUrl());

            event.getGuild().getTextChannelById(Constants.SUGGEST_LOG).sendMessage(event.getAuthor().getAsMention())
                    .embed(suggestionLogEmbed.build()).queue();
        }
    }
}