package xenobot.main.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CleanBotMessagesCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!args.isEmpty()) {
            return;
        }

        event.getMessage().delete().queue();

        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you do not have permission to use " +
                    "this command.").queue();
            return;
        }

        if (event.getChannel().getIdLong() == Constants.GENERAL_CHAT) {
            MessagePaginationAction history = event.getChannel().getIterableHistory();
            List<Message> allMsgs = new ArrayList<>();
            OffsetDateTime currentTime = OffsetDateTime.now().minusDays(14);
            history.forEachAsync(msgs -> {
                if (msgs.getTimeCreated().isAfter(currentTime)) {
                    if (msgs.getAuthor().getIdLong() == Constants.BOT_ID) {
                        allMsgs.add(msgs);
                    }
                    return true;
                }
                return false;
            }).thenRun(() -> event.getChannel().purgeMessages(allMsgs));

            event.getChannel().sendMessage("***Bots messages are being cleared.***").queue(message -> {
                long id = message.getIdLong();
                event.getChannel().editMessageById(id, "***Cleaned!***").queueAfter(3, TimeUnit.SECONDS);
                event.getChannel().deleteMessageById(id).queueAfter(5, TimeUnit.SECONDS);
            });
            return;
        }

        event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", this command can only be used in <#" +
                Constants.GENERAL_CHAT + ">").queue();
    }

    @Override
    public String getHelp() {
        return "Cleans bots messages.\n\n" +
                "__**Requires:**__ `Manage Messages` permission\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "botclean";
    }
}