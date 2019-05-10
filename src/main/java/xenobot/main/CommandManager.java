package xenobot.main;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import xenobot.main.commands.*;
import xenobot.main.commands.developer.GivePointsCMD;
import xenobot.main.commands.developer.TakePointsCMD;
import xenobot.main.commands.moderation.CleanBotMessagesCommand;
import xenobot.main.objects.ICommand;
import xenobot.main.utilities.Database;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandManager() {
        Database database = new Database();
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new GodCommand());
        addCommand(new BuyRoleCommand(database));
        addCommand(new PointsCommand(database));
        addCommand(new UserIdCommand());
        addCommand(new ShopCommand());
        addCommand(new TruckCommand());
        addCommand(new SportsCarCommand());
        addCommand(new WorkCommand(database));
        addCommand(new StealCommand(database));
        addCommand(new InvestCommand(database));
        addCommand(new GivePointsCommand(database));
        addCommand(new PutCommand(database));
        addCommand(new TakeCommand(database));
        addCommand(new CountLeaderboardCommand(database));
        addCommand(new BankLeaderboardCommand(database));
        addCommand(new GivePointsCMD(database));
        addCommand(new TakePointsCMD(database));
        addCommand(new CountCommand(database));
        addCommand(new UserInfoCommand());
        addCommand(new BankCommand(database));
        addCommand(new CleanBotMessagesCommand());
        addCommand(new GuildMemberCountCommand());
        addCommand(new GambleCommand(database));
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }

    void handleCommand(GuildMessageReceivedEvent event) {
        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(Constants.PREFIX), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            commands.get(invoke).handle(args, event);
        }
    }
}