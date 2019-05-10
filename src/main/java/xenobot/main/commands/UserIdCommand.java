package xenobot.main.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.awt.*;
import java.util.List;

public class UserIdCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() == 0) {
            generateAndSendEmbed(event, event.getAuthor().getIdLong(), event.getMember().getEffectiveName());
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.size() > 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you can only mention one member at a " +
                    "time.").queue();
            return;
        }

        Member target = mentionedMembers.get(0);

        generateAndSendEmbed(event, target.getIdLong(), target.getEffectiveName());
    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event, long id, String name) {

        EmbedBuilder builder = new EmbedBuilder()
                .addField(name, "__**User ID:**__ " + id, false)
                .setFooter("User ID requested by " + event.getMember().getEffectiveName(), event.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.MAGENTA);

        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Gets the ID of a member.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "` **or** `" + Constants.PREFIX + getInvoke() + " @user`";
    }

    @Override
    public String getInvoke() {
        return "userid";
    }
}
