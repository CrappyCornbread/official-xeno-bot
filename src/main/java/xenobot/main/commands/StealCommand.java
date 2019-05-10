package xenobot.main.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StealCommand implements ICommand {

    private HashMap<Long, Long> mapStealCooldowns = new HashMap<>();

    private Database database;

    public StealCommand(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() != 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that is not a valid command. Type " +
                    "`x.help " + getInvoke() + "` for more information on this command.").queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (mentionedMembers.size() > 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only steal from one user at a time.").queue();
            return;
        }
        if (mentionedMembers.size() < 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to mention the user to steal from.").queue();
            return;
        }
        Member target = mentionedMembers.get(0);

        if (target.getUser().isBot()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can't steal from a bot, silly.").queue();
            return;
        }

        final long currentTime = System.currentTimeMillis();
        if (!mapStealCooldowns.containsKey(event.getAuthor().getIdLong()) || mapStealCooldowns.get(event.getAuthor().getIdLong()) <= currentTime) {

            if (target.getIdLong() == event.getAuthor().getIdLong()) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can't steal from yourself, silly.").queue();
                return;
            }

            if (target.getIdLong() == Constants.OWNER_ID) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you tried to steal from my " +
                        "creator? How dare you! Do you really think I would take points from them?").queue();
                return;
            }

            Random random = new Random();
            int stealAmount = random.nextInt(200) + 101;
            int targetPoints = database.getPoints(target.getIdLong());

            if (stealAmount > targetPoints) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you attempted to steal **" + stealAmount + " " +
                        "points** from " + target.getAsMention() + " but they only have **" + targetPoints + " points**. " +
                        "Better luck next time, thief.").queue();
                return;
            }

            database.addPoints(event.getAuthor().getIdLong(), stealAmount);
            database.subtractPoints(target.getIdLong(), stealAmount);

            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " just stole **" + stealAmount + " " +
                    "points** from " + target.getAsMention() + ".").queue();

            mapStealCooldowns.put(event.getAuthor().getIdLong(), System.currentTimeMillis() + (50400000));
        } else {
            long cooldown = mapStealCooldowns.get(event.getAuthor().getIdLong()) - currentTime;
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
                    "can steal again.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Allows a user to steals points from the mentioned user.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " @user`";
    }

    @Override
    public String getInvoke() {
        return "steal";
    }
}
