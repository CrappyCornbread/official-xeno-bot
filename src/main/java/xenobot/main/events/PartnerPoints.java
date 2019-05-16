package xenobot.main.events;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xenobot.main.Constants;
import xenobot.main.utilities.Database;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PartnerPoints extends ListenerAdapter {

    private Database database;

    public PartnerPoints(Database database) {
        this.database = database;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().isWebhookMessage()) {
            return;
        }

        if (event.getGuild().getIdLong() == Constants.MAIN_GUILD_ID) {
            if (event.getChannel().getIdLong() == Constants.PARTNERS) {
                List<String> invites = event.getMessage().getInvites();

                if (invites.size() == 0) {
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to post the invite " +
                            "link for the server you're trying to partner with.").queue(messageSent -> messageSent.delete()
                            .queueAfter(5, TimeUnit.SECONDS));
                    return;
                }

                if (invites.size() != 1) {
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only post one " +
                            "invite link at a time.").queue(messageSent -> messageSent.delete()
                            .queueAfter(5, TimeUnit.SECONDS));
                    return;
                }

                Invite.resolve(event.getJDA(), invites.get(0), true).queue(success -> {
                    if (success.getType() != Invite.InviteType.GUILD) {
                        event.getMessage().delete().queue();
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that invite link is " +
                                "not for a server.").queue(messageSent -> messageSent.delete()
                                .queueAfter(5, TimeUnit.SECONDS));
                        return;
                    }

                    int memberCount = success.getGuild().getMemberCount();

                    int amount;
                    String partnerType;

                    if (memberCount < 500) {
                        amount = 100;
                        partnerType = "Small Partnership";
                    } else if (memberCount < 5000) {
                        amount = 500;
                        partnerType = "Medium Partnership";
                    } else {
                        amount = 1000;
                        partnerType = "Large Partnership";
                    }

                    event.getGuild().getTextChannelById(Constants.PARTNER_POINTS).sendMessage(event.getAuthor().getAsMention() +
                            " has been awarded **" + amount + " points** for posting a **" + partnerType + "**.").queue();
                    database.addPoints(event.getAuthor().getIdLong(), amount);
                }, error -> {
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that invite link is " +
                            "invalid. Please, try and get another link.").queue(messageSent -> messageSent.delete()
                            .queueAfter(5, TimeUnit.SECONDS));
                });
            }
        }
    }
}