package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WorkCommand implements ICommand {

    private Database database;

    public WorkCommand(Database database) {
        this.database = database;
    }

    private HashMap<Long, Long> mapWorkCooldowns = new HashMap<>();
    
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() == 0) {
            final long currentTime = System.currentTimeMillis();
            if (!mapWorkCooldowns.containsKey(event.getAuthor().getIdLong()) || mapWorkCooldowns.get(event.getAuthor().getIdLong()) <= currentTime) {
                Random random = new Random();
                int workAmount = random.nextInt(400) + 201;
                database.addPoints(event.getAuthor().getIdLong(), workAmount);
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " just worked and made **" + workAmount + " " +
                        "points**.").queue();

                mapWorkCooldowns.put(event.getAuthor().getIdLong(), System.currentTimeMillis() + (28800000));
            } else {
                long cooldown = mapWorkCooldowns.get(event.getAuthor().getIdLong()) - currentTime;
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
                        "can work again.").queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Allows a user to work and earn a random amount of money every 8 hours.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "work";
    }
}
