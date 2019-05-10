package xenobot.main.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;
import xenobot.main.utilities.extras.Users;

import java.awt.*;
import java.util.List;

public class CountLeaderboardCommand implements ICommand {

    private Database database;

    public CountLeaderboardCommand(Database database) {
        this.database = database;
    }
    
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            List<Users> users = database.getCountLeaderboard();

            if (users.size() == 0) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " there is currently no leaderboard for this section.").queue();
                return;
            }
            if (users.size() < 10) {
                int x = users.size();
                users = users.subList(0, x);
            } else if (users.size() > 10) {
                users = users.subList(0, 10);
            }

            StringBuilder countLeaderboard = new StringBuilder();
            int x = 1;
            for (Users user : users) {
                countLeaderboard.append("**").append(x).append(".** ").append("<@").append(user.getUserId()).append("> **")
                        .append(user.getMsgCount()).append(" messages**").append("\n");
                x++;
            }

            generateAndSendEmbed(event, countLeaderboard.toString());
        }
    }

    @Override
    public String getHelp() {
        return "Displays the leaderboard for the weekly message count.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "countlb";
    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event, String leaderboard) {

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("⫸ Message Count Leaderboard ⫷")
                .setDescription(leaderboard)
                .setFooter("Message count resets every sunday.", event.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.MAGENTA);

        event.getChannel().sendMessage(builder.build()).queue();
    }
}
