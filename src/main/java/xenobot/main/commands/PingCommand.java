package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.util.List;

public class PingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Pong!").queue(message ->
                    message.editMessageFormat("Ping is **%sms**", event.getJDA().getGatewayPing()).queue()
            );
        }
    }

    @Override
    public String getHelp() {
        return "Gets the response time of the bot.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "ping";
    }
}
