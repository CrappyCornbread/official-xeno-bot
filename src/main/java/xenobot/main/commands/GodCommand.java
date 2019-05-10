package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.util.List;
import java.util.Random;

public class GodCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " - **God of " +
                    getAbility() + "**").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Tells you what you are the god of.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "god";
    }

    private String getAbility() {
        final String[] ability = {"Nothing", "AIDS", "Sky", "Sea", "Agriculture", "War", "Wisdom",
                "Art", "Archery", "Music", "Double Penetration", "Fire", "Love", "Steel", "Beauty",
                "Victory", "Magic", "Ghosts", "Darkness", "Insurance", "Vegetables", "Big Macs",
                "Starbucks", "Fortnite", "Apex Legends", "Religion", "Technology", "Electricity",
                "Explosives", "Liquid", "Wealth", "Tapestry", "Shapes", "Hair", "Perception",
                "Chocolate", "Plants", "Law & Order", "Secretions", "Night", "Nascar", "Loneliness",
                "Stupidity", "Sleep", "Rap", "Women", "Men", "Cookies", "Hair Loss", "Herpes",
                "Mario", "Kawaiiness", "Hoes", "Peace", "Nature", "Perfection", "Monopoly",
                "Cheese", "Cherry Pie", "OCD", "#2 Pencils", "Mischief", "Robots", "Artificial Insemination",
                "Animals", "History", "Happiness"};
        Random random = new Random();
        int index = random.nextInt(ability.length);

        return ability[index];
    }
}
