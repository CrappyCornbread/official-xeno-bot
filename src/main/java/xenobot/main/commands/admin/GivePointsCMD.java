package xenobot.main.commands.admin;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.List;

public class GivePointsCMD implements ICommand {

    private Database database;

    public GivePointsCMD(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != Constants.OWNER_ID) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you do not have permission to use " +
                    "this command.").queue();
            return;
        }
        if (args.size() != 2) {
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.size() != 1) {
            return;
        }

        Member target = mentionedMembers.get(0);

        int givePoints;
        try {
            givePoints = Integer.parseInt(args.get(1));
        } catch (NumberFormatException ex) {
            return;
        }

        if (target.getIdLong() != event.getAuthor().getIdLong()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " has given " + target.getAsMention() +
                    " **" + givePoints + " points**.").queue();
            database.addPoints(target.getIdLong(), givePoints);
            return;
        }

        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " has given himself **" +
                givePoints + " points**.").queue();
        database.addPoints(event.getAuthor().getIdLong(), givePoints);
    }

    @Override
    public String getHelp() {
        return "This command is for the bot developer only.";
    }

    @Override
    public String getInvoke() {
        return "admingive";
    }
}
