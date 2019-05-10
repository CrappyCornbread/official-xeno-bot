package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.List;

public class GambleCommand implements ICommand {

    private Database database;

    public GambleCommand(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that is not a valid command. Type " +
                    "`" + Constants.PREFIX + "help " + getInvoke() + "` for more information on this command.").queue();
            return;
        }

        int balance = database.getPoints(event.getAuthor().getIdLong());
        if (balance < 100) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you don't have enough money to place " +
                    "a bet.\n\n**Minimum Bet** - 100 points\n" +
                    "**Current Balance** - " + balance).queue();
            return;
        }


        int minimumBet = 100;

        if (args.get(0).equalsIgnoreCase("flop")) {
            if (args.size() != 2) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to tell me how much you " +
                        "would like to bet.").queue();
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(args.get(1));
            } catch (NumberFormatException ex) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only gamble with an " +
                        "integer number of points.").queue();
                return;
            }
            if (amount < minimumBet) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you must have a minimum of **" +
                        minimumBet + " points** to place a bet.").queue();
                return;
            }

            database.subtractPoints(event.getAuthor().getIdLong(), amount);
            int winnings = computeFlopGamble(amount);

            if (winnings == 0) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you lose. Better luck next time.").queue();
                return;
            }

            database.addPoints(event.getAuthor().getIdLong(), winnings + amount);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you beat the odds and won **" +
                    winnings + " points**!").queue();

            return;
        }

        if (args.get(0).equalsIgnoreCase("drop")) {
            if (args.size() != 2) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to tell me how much you " +
                        "would like to bet.").queue();
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(args.get(1));
            } catch (NumberFormatException ex) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only gamble with an " +
                        "integer number of points.").queue();
                return;
            }
            if (amount < minimumBet) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you must have a minimum of **" +
                        minimumBet + " points** to place a bet.").queue();
                return;
            }

            database.subtractPoints(event.getAuthor().getIdLong(), amount);
            int winnings = computeDropGamble(amount);

            if (winnings == 0) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you lose. Better luck next time.").queue();
                return;
            }

            database.addPoints(event.getAuthor().getIdLong(), winnings + amount);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you beat the odds and won **" +
                    winnings + " points**!").queue();

            return;
        }

        if (args.get(0).equalsIgnoreCase("roll")) {
            if (args.size() != 2) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to tell me how much you " +
                        "would like to bet.").queue();
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(args.get(1));
            } catch (NumberFormatException ex) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only gamble with an " +
                        "integer number of points.").queue();
                return;
            }
            if (amount < minimumBet) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you must have a minimum of **" +
                        minimumBet + " points** to place a bet.").queue();
                return;
            }

            database.subtractPoints(event.getAuthor().getIdLong(), amount);
            int winnings = computeRollGamble(amount);

            if (winnings == 0) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you lose. Better luck next time.").queue();
                return;
            }

            database.addPoints(event.getAuthor().getIdLong(), winnings + amount);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you beat the odds and won **" +
                    winnings + " points**!").queue();

            return;
        }

        event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that is not a valid machine.").queue();
    }

    @Override
    public String getHelp() {
        return "Allows a user to gamble with several different machines.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " <machine> <amount>`\n\n" +
                "Available Machines:\n" +
                "**Flop** - 1.5x Payout (50% Chance)\n" +
                "**Drop** - 2.5x Payout (25% Chance)\n" +
                "**Roll** - 4x Payout (10% Chance)\n\n" +
                "**MINIMUM 100 point** bet in order to play.";
    }

    @Override
    public String getInvoke() {
        return "gamble";
    }

    private int computeFlopGamble(int amount) {
        int winningAmount = 0;
        double computeWinnings;
        if (Math.random() < .5) {
            computeWinnings = amount * 1.5;
            winningAmount = (int) computeWinnings;
        }

        return winningAmount;
    }

    private int computeDropGamble(int amount) {
        int winningAmount = 0;
        double computeWinnings;
        if (Math.random() < .25) {
            computeWinnings = amount * 2.5;
            winningAmount = (int) computeWinnings;
        }

        return winningAmount;
    }

    private int computeRollGamble(int amount) {
        int winningAmount = 0;
        double computeWinnings;
        if (Math.random() < .10) {
            computeWinnings = amount * 4.0;
            winningAmount = (int) computeWinnings;
        }

        return winningAmount;
    }
}
