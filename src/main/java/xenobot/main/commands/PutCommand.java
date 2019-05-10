package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.List;

public class PutCommand implements ICommand {

    private Database database;

    public PutCommand(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you need to tell me how much you " +
                    "want to put in your bank. Type `" + Constants.PREFIX + "help " + getInvoke() + "` for more information.").queue();
            return;
        }

        if (args.size() != 1) {
            return;
        }

        int points = database.getPoints(event.getAuthor().getIdLong());

        if (args.get(0).equalsIgnoreCase("all") && args.size() == 1) {
            database.subtractPoints(event.getAuthor().getIdLong(), points);
            database.addBankBalance(event.getAuthor().getIdLong(), points);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", **"  + points + " points** has just " +
                    "been put into your bank.").queue();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args.get(0));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only store an amount of " +
                    "points in your bank as an integer.").queue();
            return;
        }

        if (amount > points) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can not put away more points than you have.").queue();
        }
        if (amount < 0) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can not put away a negative amount of points.").queue();
            return;
        }
        if (amount == 0) {
            return;
        }
        if (amount <= points) {
            database.subtractPoints(event.getAuthor().getIdLong(), amount);
            database.addBankBalance(event.getAuthor().getIdLong(), amount);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", **" + amount + " points** has just been put into your " +
                    "bank.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Allows a user to transfer an amount of points to their bank.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " <amount>` **or** " +
                "`" + Constants.PREFIX + getInvoke() + " all`";
    }

    @Override
    public String getInvoke() {
        return "put";
    }
}
