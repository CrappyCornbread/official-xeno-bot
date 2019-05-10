package xenobot.main.utilities.timers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xenobot.main.Constants;
import xenobot.main.utilities.Database;
import xenobot.main.utilities.extras.PointUtils;
import xenobot.main.utilities.extras.RoleUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BaseTimers extends ListenerAdapter {

    private static Database database;
    private static HashMap<Long, Long> mapInvestCooldowns;
    private static HashMap<Long, Long> mapWorkCooldowns;
    private static HashMap<Long, Long> mapStealCooldowns;

    public BaseTimers(Database database, HashMap<Long, Long> mapInvestCooldowns,
                      HashMap<Long, Long> mapStealCooldowns, HashMap<Long, Long> mapWorkCooldowns) {
        BaseTimers.database = database;
        BaseTimers.mapInvestCooldowns = mapInvestCooldowns;
        BaseTimers.mapStealCooldowns = mapStealCooldowns;
        BaseTimers.mapWorkCooldowns = mapWorkCooldowns;
    }

    private static void executeAnnouncements(Guild guild) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long guildId = guild.getIdLong();
        JDA jda = guild.getJDA();
        executor.scheduleAtFixedRate(() -> BaseTimers.announcementMessages(jda.getGuildById(guildId)), 0, 3, TimeUnit.SECONDS);
    }

    private static void executeRainbowRoles(Guild guild) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long guildId = guild.getIdLong();
        JDA jda = guild.getJDA();
        executor.scheduleAtFixedRate(() -> BaseTimers.constructRainbowRoles(jda.getGuildById(guildId)), 0, 2, TimeUnit.SECONDS);
    }

    private static void cleanHashMaps(Guild guild) {
        mapWorkCooldowns.entrySet().removeIf(entry -> entry.getValue() <= System.currentTimeMillis());
        mapStealCooldowns.entrySet().removeIf(entry -> entry.getValue() <= System.currentTimeMillis());
        mapInvestCooldowns.entrySet().removeIf(entry -> entry.getValue() <= System.currentTimeMillis());
    }

    private static void executeHashMapCleaner(Guild guild) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long guildId = guild.getIdLong();
        JDA jda = guild.getJDA();
        executor.scheduleAtFixedRate(() -> BaseTimers.cleanHashMaps(jda.getGuildById(guildId)), 0, 12, TimeUnit.HOURS);
    }

    private static void executeBankInterest(Guild guild) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long guildId = guild.getIdLong();
        JDA jda = guild.getJDA();
        executor.scheduleAtFixedRate(() -> BaseTimers.constructBankInterest(jda.getGuildById(guildId)), 0, 1, TimeUnit.MINUTES);
    }

    private static void executeWeeklyReset() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(BaseTimers::weeklyMessageCountReset, 0, 7, TimeUnit.DAYS);
    }

    private static void announcementMessages(Guild guild) {
        String[] msgs = {
                "Our rules can be found here: <#" + Constants.RULES + "> It is your responsibility to know them.",
                "Don't want anymore partner ping? Go here <#" + Constants.ASSIGN_ROLES + "> to remove the `partner ping` role.",
                "If you're having an issue with a member, please, don't hesitate to report them to the staff team.",
                guild.getName() + " is currently looking for staff. Head over to <#" + Constants.STAFF_APPLICATION + "> to apply!"
        };

        Random random = new Random();
        int index = random.nextInt(msgs.length - 1);

        EmbedBuilder announcement = new EmbedBuilder();
        announcement.setTitle("Attention members!");
        announcement.setDescription(msgs[index]);
        announcement.setColor(Color.MAGENTA);
        announcement.setFooter("Any problems, please, contact the staff team.", guild.getSelfMember().getUser().getAvatarUrl());
        guild.getTextChannelById(Constants.GENERAL_CHAT).sendMessage(announcement.build()).queue();
    }

    private static void constructBankInterest(Guild guild) {
        List<Member> members = guild.getMembers();

        for (Member member : members) {
            if (database.getBankBalance(member.getIdLong()) >= 500) {
                PointUtils.addBankInterest(database, member);
            }
        }
    }

    private static void constructRainbowRoles(Guild guild) {
        List<Role> roles = guild.getRoles();

        for (Role role : roles) {
            if (role.getIdLong() == Constants.RAINBOW) {
                if (role.getManager().getRole().getColor() == Color.MAGENTA) {
                    role.getManager().setColor(RoleUtils.getColor()).queue();
                    return;
                }
                role.getManager().setColor(RoleUtils.getColor()).queue();
                return;
            }
        }
    }

    private static void weeklyMessageCountReset() {
        database.resetWeeklyBoards();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getAuthor() == null || event.getAuthor().isBot()) {
            return;
        }

        if (event.getGuild().getIdLong() == Constants.MAIN_GUILD_ID &&
                event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
            String[] msgArgs = event.getMessage().getContentRaw().split("\\s+");
            if (event.getAuthor().getIdLong() == Constants.OWNER_ID) {
                if (msgArgs[0].equalsIgnoreCase(Constants.PREFIX + "execute")) {
                    if (msgArgs.length != 2) {
                        return;
                    }
                    if (msgArgs[1].equalsIgnoreCase("interest")) {
                        event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();
                        event.getChannel().sendMessage("Executing bank interest every **24** hours at this time.").queue(messageSent -> messageSent.delete()
                                .queueAfter(5, TimeUnit.SECONDS));
                        executeBankInterest(event.getGuild());
                        return;
                    }
                    if (msgArgs[1].equalsIgnoreCase("weekly")) {
                        event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();
                        event.getChannel().sendMessage("Executing weekly count reset every **7** days at this time.").queue(messageSent -> messageSent.delete()
                                .queueAfter(5, TimeUnit.SECONDS));
                        executeWeeklyReset();
                        return;
                    }
                    if (msgArgs[1].equalsIgnoreCase("cleanmaps")) {
                        event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();
                        event.getChannel().sendMessage("Executing hash map cleaning every **12** hours.").queue(messageSent -> messageSent.delete()
                                .queueAfter(5, TimeUnit.SECONDS));
                        executeHashMapCleaner(event.getGuild());
                        return;
                    }
                    if (msgArgs[1].equalsIgnoreCase("rainbow")) {
                        event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();
                        event.getChannel().sendMessage("Executing rainbow roles every **2** seconds.").queue(messageSent -> messageSent.delete()
                                .queueAfter(5, TimeUnit.SECONDS));
                        executeRainbowRoles(event.getGuild());
                        return;
                    }
                    if (msgArgs[1].equalsIgnoreCase("cleandb")) {
                        event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();
                        event.getChannel().sendMessage("***Cleaned database.***").queue(messageSent -> messageSent.delete()
                                .queueAfter(5, TimeUnit.SECONDS));
                        database.cleanDatabase();
                        return;
                    }
                    if (msgArgs[1].equalsIgnoreCase("announcements")) {
                        event.getChannel().deleteMessageById(event.getMessage().getIdLong()).queue();
                        event.getChannel().sendMessage("Executing announcements every **3** hours.").queue(messageSent -> messageSent.delete()
                                .queueAfter(5, TimeUnit.SECONDS));
                        executeAnnouncements(event.getGuild());
                    }
                }
            }
        }
    }
}