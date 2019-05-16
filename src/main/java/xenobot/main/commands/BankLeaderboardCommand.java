package xenobot.main.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;
import xenobot.main.utilities.extras.Users;

import java.awt.*;
import java.util.List;

public class BankLeaderboardCommand implements ICommand {
    
    private Database database;
    
    public BankLeaderboardCommand(Database database) {
        this.database = database;
    }
    
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            List<Users> users = database.getBankLeaderboard();

            if (users.size() == 0) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", there is currently no leaderboard " +
                        "for **Bank Balance**.").queue();
                return;
            }
            if (users.size() < 10) {
                int x = users.size();
                users = users.subList(0, x);
            } else if (users.size() > 10) {
                users = users.subList(0, 10);
            }

            StringBuilder bankLeaderboard = new StringBuilder();
            int x = 1;
            for (Users user : users) {
                bankLeaderboard.append("**").append(x).append(".** ").append("<@").append(user.getUserId()).append("> **")
                        .append(user.getBankBalance()).append(" points**").append("\n");
                x++;
            }

            generateAndSendEmbed(event, bankLeaderboard.toString());
        }
    }

    @Override
    public String getHelp() {
        return "Displays the leaderboard for points.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "banklb";
    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event, String leaderboard) {

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("⫸ Bank Leaderboard ⫷")
                .setDescription(leaderboard)
                .setFooter("These are the points in your bank.", event.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.MAGENTA);

        event.getChannel().sendMessage(builder.build()).queue();
    }
}
