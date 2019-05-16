package xenobot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import xenobot.main.BotInfo;
import xenobot.main.CommandManager;
import xenobot.main.Listener;
import xenobot.main.events.*;
import xenobot.main.utilities.Database;
import xenobot.main.utilities.timers.BaseTimers;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Main {

    private Main() {

        Database database = new Database();
        CommandManager commandManager = new CommandManager();
        Listener listener = new Listener(commandManager, database);
        HashMap<Long, Long> mapInvestCooldowns = new HashMap<>();
        HashMap<Long, Long> mapWorkCooldowns = new HashMap<>();
        HashMap<Long, Long> mapStealCooldowns = new HashMap<>();

        try {
            new JDABuilder(AccountType.BOT)
                    .setToken(BotInfo.TOKEN)
                    .setAudioEnabled(false)
                    .addEventListeners(listener)
                    .addEventListeners(new MemberJoin())
                    .addEventListeners(new ConfessionsManager())
                    .addEventListeners(new BaseTimers(database, mapInvestCooldowns, mapWorkCooldowns, mapStealCooldowns))
                    .addEventListeners(new PartnerPing())
                    .addEventListeners(new SuggestionsManager())
                    .addEventListeners(new PartnerPoints(database))
                    .setAutoReconnect(true)
                    .setActivity(Activity.watching(BotInfo.ACTIVITY))
                    .setStatus(OnlineStatus.ONLINE)
                    .build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}