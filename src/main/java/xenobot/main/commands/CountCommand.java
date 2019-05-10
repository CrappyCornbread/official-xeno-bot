package xenobot.main.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.awt.*;
import java.util.List;

public class CountCommand implements ICommand {

    private Database database;

    public CountCommand(Database database) {
        this.database = database;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            int count = database.getMessageCount(event.getAuthor().getIdLong());
            generateAndSendEmbed(event, count, event.getMember().getEffectiveName());
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.size() > 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only mention one member at a " +
                    "time.").queue();
            return;
        }

        if (args.size() != 1) {
            return;
        }

        if (mentionedMembers.isEmpty()) {
            return;
        }

        Member target = mentionedMembers.get(0);

        if (target.getUser().isBot()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", bots do not participate in the weekly " +
                    "message count.").queue();
            return;
        }

        int count = database.getMessageCount(target.getIdLong());
        generateAndSendEmbed(event, count, target.getEffectiveName());
    }

    @Override
    public String getHelp() {
        return "Returns how many messages a user has sent so far this week.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "` **or** " +
                "`" + Constants.PREFIX + getInvoke() + " @user`";
    }

    @Override
    public String getInvoke() {
        return "count";
    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event, int count, String name) {

        EmbedBuilder builder = new EmbedBuilder()
                .addField(name, "You currently have **" + count + " messages** sent this week.", false)
                .setFooter("Message count resets every Sunday.", event.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.MAGENTA);

        event.getChannel().sendMessage(builder.build()).queue();
    }
}
