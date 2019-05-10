package xenobot.main.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;
import xenobot.main.utilities.extras.RoleUtils;

import java.util.List;

public class BuyRoleCommand implements ICommand {

    private Database database;

    public BuyRoleCommand(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that is not a valid command. Type " +
                    "`" + Constants.PREFIX + "help " + getInvoke() + "` for more information on this command.").queue();
            return;
        }

        if (args.size() == 1 && args.get(0).equalsIgnoreCase("role")) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you forgot to tell me which role " +
                    "you would like to buy. Type `x.help " + getInvoke() + "` for more information on this command.").queue();
            return;
        }

        if (args.size() == 2) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that is not a command. Type " +
                    "`x.help " + getInvoke() + "` for more information on this command.").queue();
            return;
        }

        if (args.size() > 3) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that is not a valid command. Type " +
                    "`x.help " + getInvoke() + "` for more information on this command.").queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        String role = String.valueOf(sb.append(args.get(1)).append(" ").append(args.get(2)));

        if (args.get(0).equalsIgnoreCase("role")) {
            if (role.equalsIgnoreCase("upper class")) {
                if (RoleUtils.hasRole(event.getMember().getRoles(), Constants.UPPER_CLASS)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you already have this role. You do " +
                            "not need to buy it again.").queue();
                    return;
                }

                if (!RoleUtils.hasRole(event.getMember().getRoles(), Constants.LOWER_CLASS)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you need to purchase the **Lower Class** " +
                            "role, then the **Middle Class** role before you can purchase the **Upper Class** role.").queue();
                    return;
                }

                if (!RoleUtils.hasRole(event.getMember().getRoles(), Constants.MIDDLE_CLASS)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you need to purchase the **Middle Class** " +
                            "role before you can purchase the **Upper Class** role.").queue();
                    return;
                }

                int points = database.getPoints(event.getAuthor().getIdLong());
                int cost = 575000;
                if (points < cost) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you do not have enough points to buy this role.\n\n" +
                            "__Cost:__ **" + cost + " points**").queue();
                    return;
                }

                database.subtractPoints(event.getAuthor().getIdLong(), cost);

                Role upperClassRole = event.getGuild().getRoleById(Constants.UPPER_CLASS);
                event.getGuild().getController().addSingleRoleToMember(event.getMember(), upperClassRole).queue();

                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you have just purchased the **Upper Class** " +
                        "role for **" + cost + "** points.").queue();
                return;
            }
            if (role.equalsIgnoreCase("middle class")) {
                if (RoleUtils.hasRole(event.getMember().getRoles(), Constants.MIDDLE_CLASS)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you already have this role. You do " +
                            "not need to buy it again.").queue();
                    return;
                }

                if (!RoleUtils.hasRole(event.getMember().getRoles(), Constants.LOWER_CLASS)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you need to purchase the **Lower Class** " +
                            "role before you can purchase the **Middle Class** role.").queue();
                    return;
                }

                int points = database.getPoints(event.getAuthor().getIdLong());
                int cost = 125000;
                if (points < cost) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you do not have enough points to buy this role.\n\n" +
                            "__Cost:__ **" + cost + " points**").queue();
                    return;
                }

                database.subtractPoints(event.getAuthor().getIdLong(), cost);

                Role middleClassRole = event.getGuild().getRoleById(Constants.MIDDLE_CLASS);
                event.getGuild().getController().addSingleRoleToMember(event.getMember(), middleClassRole).queue();

                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you have just purchased the **Middle Class** " +
                        "role for **" + cost + "** points.").queue();
                return;
            }
            if (role.equalsIgnoreCase("lower class")) {
                if (RoleUtils.hasRole(event.getMember().getRoles(), Constants.LOWER_CLASS)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you already have this role. You do " +
                            "not need to buy it again.").queue();
                    return;
                }

                int points = database.getPoints(event.getAuthor().getIdLong());
                int cost = 10000;
                if (points < cost) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you do not have enough points to buy this role.\n\n" +
                            "__Cost:__ **" + cost + " points**").queue();
                    return;
                }

                database.subtractPoints(event.getAuthor().getIdLong(), cost);

                Role lowerClassRole = event.getGuild().getRoleById(Constants.LOWER_CLASS);
                event.getGuild().getController().addSingleRoleToMember(event.getMember(), lowerClassRole).queue();

                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you have just purchased the **Lower Class** " +
                        "role for **" + cost + "** points.").queue();
                return;
            }
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", that is not a valid role. Type " +
                    "`x.help " + getInvoke() + "` for more information on this command.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Allows user to buy roles and begin earning interest on their bank balance.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + " role <role>`\n\n" +
                "Available Roles:\n" +
                                "**Lower Class** - 2% Interest Rate\n" +
                                "**Middle Class** - 5% Interest Rate\n" +
                                "**Upper Class** - 8% Interest Rate";
    }

    @Override
    public String getInvoke() {
        return "buy";
    }
}
