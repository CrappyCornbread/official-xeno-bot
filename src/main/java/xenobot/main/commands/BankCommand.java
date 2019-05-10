package xenobot.main.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.awt.*;
import java.util.List;

public class BankCommand implements ICommand {

    private Database database;

    public BankCommand(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            int points = database.getBankBalance(event.getAuthor().getIdLong());
            generateAndSendEmbed(event, points, event.getMember().getEffectiveName());
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.size() > 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only mention one member at a " +
                    "time.").queue();
            return;
        }

        Member target = mentionedMembers.get(0);

        if (target.getUser().isBot()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", bots don't have points in general. " +
                    "Why would we have any in our bank?").queue();
            return;
        }

        int points = database.getBankBalance(target.getIdLong());
        generateAndSendEmbed(event, points, target.getEffectiveName());
    }

    @Override
    public String getHelp() {
        return "Returns how many points a user has in their bank.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "` **or** " +
                "`" + Constants.PREFIX + getInvoke() + " @user`";
    }

    @Override
    public String getInvoke() {
        return "bank";
    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event, int points, String name) {

        EmbedBuilder builder = new EmbedBuilder()
                .addField(name, "You currently have **" + points + " points** in your bank.", false)
                .setFooter("Earn points by sending messages, working, investing, or stealing from others.", event.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.MAGENTA);

        event.getChannel().sendMessage(builder.build()).queue();
    }
}