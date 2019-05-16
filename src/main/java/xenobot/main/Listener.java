package xenobot.main;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xenobot.main.utilities.Database;
import xenobot.main.utilities.extras.PointUtils;

public class Listener extends ListenerAdapter {

    private final Database database;
    private final CommandManager manager;

    public Listener(CommandManager manager, Database database) {
        this.manager = manager;
        this.database = database;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (!event.getAuthor().isBot() && event.getMember() != null) {
            if (event.getGuild().getIdLong() == Constants.MAIN_GUILD_ID) {
                if (event.getChannel().getIdLong() == Constants.GENERAL_CHAT || event.getChannel().getIdLong() == Constants.VC_TEXT) {
                    database.addMessageCounter(event.getAuthor().getIdLong());

                    int msgCount = database.getMessageCount(event.getAuthor().getIdLong());

                    int points = PointUtils.computeBonusPoints(msgCount);

                    if (points != 0) {
                        database.addPoints(event.getAuthor().getIdLong(), points);
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you have been awarded **" +
                                points + " points** for being active and typing in the chats.").queue();
                    }
                }
            }
        }

        if (!event.getMessage().isWebhookMessage() && !event.getAuthor().isBot() && event.getMember() != null) {
            if (event.getGuild().getIdLong() == Constants.MAIN_GUILD_ID &&
                    event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
                if (event.getChannel().getIdLong() == Constants.BOT_COMMANDS ||
                    event.getChannel().getIdLong() == Constants.GENERAL_CHAT ||
                    event.getChannel().getIdLong() == Constants.VC_TEXT) {

                    manager.handleCommand(event);
                }
            }
        }
    }

}