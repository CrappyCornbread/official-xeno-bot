package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.util.List;

public class GuildMemberCountCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", there are **" +
                    event.getJDA().getGuildById(Constants.MAIN_GUILD_ID).getMembers().size() + " members** " +
                    "in this guild.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Tells a user how many members are in the guild.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "users";
    }
}
