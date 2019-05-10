package xenobot.main.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.List;

public class GivePointsCommand implements ICommand {

    private Database database;

    public GivePointsCommand(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.size() == 1 && args.size() == 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you need to tell me how many points to give.").queue();
            return;
        }
        if (mentionedMembers.size() > 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only give points to one user at a time.").queue();
            return;
        }
        if (mentionedMembers.size() < 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to mention the user to give points to.").queue();
            return;
        }
        if (args.size() != 2) {
            return;
        }
        int points = database.getPoints(event.getAuthor().getIdLong());
        int givePoints;
        try {
            givePoints = Integer.parseInt(args.get(1));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only give an amount of points as an integer.").queue();
            return;
        }
        if (givePoints > points) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can not give more points than you have.").queue();
        }
        if (givePoints < 0) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can not give a negative amount of points.").queue();
            return;
        }
        if (givePoints == 0) {
            if (points >= 1000) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", because you tried to be funny and give 0 points, " +
                        "I just took away some of yours. You're welcome!").queue();
                database.subtractPoints(event.getAuthor().getIdLong(), 100);
            } else {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you're lucky that you're broke or else I would take " +
                        "some points away from you for trying to troll someone like that.").queue();
            }
            return;
        }
        if (givePoints <= points) {
            Member target = mentionedMembers.get(0);

            database.subtractPoints(event.getAuthor().getIdLong(), givePoints);
            database.addPoints(target.getIdLong(), givePoints);

            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " has given " + target.getAsMention() + " **" +
                    givePoints + " points**.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Allows a user to give points to another user.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " <amount>`";
    }

    @Override
    public String getInvoke() {
        return "give";
    }
}
