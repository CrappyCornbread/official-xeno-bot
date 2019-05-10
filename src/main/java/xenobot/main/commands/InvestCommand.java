package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class InvestCommand implements ICommand {

    private Database database;

    public InvestCommand(Database database) {
        this.database = database;
    }

    private HashMap<Long, Long> mapInvestCooldowns = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() == 0) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you need to give an amount to invest.").queue();
            return;
        }

        if (args.size() != 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that is not a valid command. Type " +
                    "`x.help " + getInvoke() + "` for more information on this command.").queue();
            return;
        }

        int investAmount;
        try {
            investAmount = Integer.parseInt(args.get(0));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only invest an amount of points as an integer.").queue();
            return;
        }

        if (investAmount == 0) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can't invest an amount of **0** into stocks.").queue();
            return;
        }
        final long currentTime = System.currentTimeMillis();
        if (!mapInvestCooldowns.containsKey(event.getAuthor().getIdLong()) || mapInvestCooldowns.get(event.getAuthor().getIdLong()) <= currentTime) {
            Random random = new Random();
            double amount = random.nextDouble() * 10;
            int choice = random.nextInt(10) + 1;
            int winningAmount;
            if (choice == 0 || choice == 3 || choice == 5 || choice == 6 || choice == 7 || choice == 9) {
                winningAmount = investAmount * (int) amount;
            } else {
                int checkAmount = (int)amount;
                if (checkAmount == 0) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " just lost all their points investing " +
                            "in stocks. You should look up some investment strategies.").queue();
                    return;
                }
                winningAmount = investAmount / (int) amount;
            }
            if (winningAmount == investAmount) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you didn't make anything, but you didn't lose " +
                        "anything either!").queue();
            } else if (winningAmount > investAmount) {
                int rawAmount = winningAmount - investAmount;
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", just invested in stocks and made **" + rawAmount + " " +
                        "points**.").queue();
            } else {
                int rawAmount = investAmount - winningAmount;
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " just lost **" + rawAmount + " " +
                        "points** investing in stocks. Clearly they don't know how to invest.").queue();
            }

            database.addPoints(event.getAuthor().getIdLong(), winningAmount);

            mapInvestCooldowns.put(event.getAuthor().getIdLong(), System.currentTimeMillis() + (36000000));
        } else {
            long cooldown = mapInvestCooldowns.get(event.getAuthor().getIdLong()) - currentTime;
            long hours = TimeUnit.MILLISECONDS.toHours(cooldown);
            cooldown -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldown);
            cooldown -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldown);
            StringBuilder sb = new StringBuilder(64);
            if (hours != 0) {
                sb.append(hours);
                sb.append(" Hours ");
                sb.append(minutes);
                sb.append(" Minutes ");
                sb.append(seconds);
                sb.append(" Seconds");
            } else if (minutes != 0) {
                sb.append(minutes);
                sb.append(" Minutes ");
                sb.append(seconds);
                sb.append(" Seconds");
            } else {
                sb.append(seconds);
                sb.append(" Seconds");
            }

            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you must wait **" + sb.toString() + "** until you " +
                    "can invest in stocks again.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Allows a user to invest points into stocks.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " amount`";
    }

    @Override
    public String getInvoke() {
        return "invest";
    }
}
