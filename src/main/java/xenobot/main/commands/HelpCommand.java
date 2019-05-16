package xenobot.main.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.CommandManager;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class HelpCommand implements ICommand {

    private final Random random = new Random();

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            if (event.getChannel().getIdLong() == Constants.BOT_COMMANDS) {
                generateAndSendEmbed(event);
                return;
            }
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that command can only be used " +
                    "in <#" + Constants.BOT_COMMANDS + ">.").queue();
            return;
        }

        String joined = String.join(" ", args);

        ICommand command = manager.getCommand(joined);

        if(command == null) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", `" + joined + "` is not one of my " +
                    "commands. Type `" + Constants.PREFIX + getInvoke() + "` in <#" + Constants.BOT_COMMANDS + "> for a list of my commands.").queue();
            return;
        }

        EmbedBuilder helpCommandEmbed = new EmbedBuilder();
        helpCommandEmbed.setTitle("⫸ Help Menu ⫷");
        helpCommandEmbed.addField("__Command help for__ `" + command.getInvoke() + "`", command.getHelp(), false);
        helpCommandEmbed.setDescription("All commands can be found by typing `" + Constants.PREFIX + "help` in the " +
                "<#" + Constants.BOT_COMMANDS + "> channel.");
        helpCommandEmbed.setColor(Color.MAGENTA);
        event.getChannel().sendMessage(event.getAuthor().getAsMention()).embed(helpCommandEmbed.build()).queue();
    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event) {

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("⫸ Command List ⫷")
                .appendDescription("Prefix for all commands is `" + Constants.PREFIX + "`\n\n" +
                        "Type `x.help <command>` for help on the command usage.\n\n")
                .setFooter("All commands listed above are encouraged to be used in this channel.", event.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.MAGENTA);

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        manager.getCommands().forEach(
                (command) -> descriptionBuilder.append("**").append(command.getInvoke()).append("**\n")
        );

        event.getChannel().sendMessage(event.getAuthor().getAsMention()).embed(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shows a list of all the commands or gets help on specific command.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " <command>`\n" +
                "\t`" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "help";
    }
}