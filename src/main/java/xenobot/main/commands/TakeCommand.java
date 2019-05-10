package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.List;

public class TakeCommand implements ICommand {

    private Database database;

    public TakeCommand(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you need to tell me how much you " +
                    "want to take from your bank. Type `" + Constants.PREFIX + "help " + getInvoke() + "` for more information.").queue();
            return;
        }

        if (args.size() != 1) {
            return;
        }

        int balance = database.getBankBalance(event.getAuthor().getIdLong());
        if (args.get(0).equalsIgnoreCase("all")) {
            database.addPoints(event.getAuthor().getIdLong(), balance);
            database.subtractBankBalance(event.getAuthor().getIdLong(), balance);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", **" + balance + "** points has just been taken from your " +
                    "bank.").queue();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args.get(0));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only enter an amount of points as an integer.").queue();
            return;
        }
        if (amount > balance) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can not take more points than you have in the bank.").queue();
        }
        if (amount < 0) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can not take a negative amount of points from your bank.").queue();
            return;
        }
        if (amount == 0) {
            return;
        }
        if (amount <= balance) {
            database.subtractBankBalance(event.getAuthor().getIdLong(), amount);
            database.addPoints(event.getAuthor().getIdLong(), amount);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", **" + amount + " points** has just been taken from your " +
                    "bank.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Allows a user to transfer an amount of points from their bank.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " <amount>` **or** " +
                "`" + Constants.PREFIX + getInvoke() + " all`";
    }

    @Override
    public String getInvoke() {
        return "take";
    }
}
