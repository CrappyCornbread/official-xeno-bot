package xenobot.main.objects;

import net.dv8tion.jda.internal.utils.Checks;

import java.util.*;
import java.util.stream.Collectors;

public enum Permission
{
    CREATE_INSTANT_INVITE(0, true, true, "Create Instant Invite"),
    KICK_MEMBERS(1, true, false, "Kick Members"),
    BAN_MEMBERS(2, true, false, "Ban Members"),
    ADMINISTRATOR(3, true, false, "Administrator"),
    MANAGE_CHANNEL(4, true, true, "Manage Channels"),
    MANAGE_SERVER(5, true, false, "Manage Server"),
    MESSAGE_ADD_REACTION(6, true, true, "Add Reactions"),
    VIEW_AUDIT_LOGS(7, true, false, "View Audit Logs"),
    PRIORITY_SPEAKER(8, true, true, "Priority Speaker"),

    // Applicable to all channel types
    VIEW_CHANNEL(10, true, true, "Read Text Channels & See Voice Channels"),

    // Text Permissions
    MESSAGE_READ(10, true, true, "Read Messages"),
    MESSAGE_WRITE(11, true, true, "Send Messages"),
    MESSAGE_TTS(12, true, true, "Send TTS Messages"),
    MESSAGE_MANAGE(13, true, true, "Manage Messages"),
    MESSAGE_EMBED_LINKS(14, true, true, "Embed Links"),
    MESSAGE_ATTACH_FILES(15, true, true, "Attach Files"),
    MESSAGE_HISTORY(16, true, true, "Read History"),
    MESSAGE_MENTION_EVERYONE(17, true, true, "Mention Everyone"),
    MESSAGE_EXT_EMOJI(18, true, true, "Use External Emojis"),

    // Voice Permissions
    VOICE_CONNECT(20, true, true, "Connect"),
    VOICE_SPEAK(21, true, true, "Speak"),
    VOICE_MUTE_OTHERS(22, true, true, "Mute Members"),
    VOICE_DEAF_OTHERS(23, true, true, "Deafen Members"),
    VOICE_MOVE_OTHERS(24, true, true, "Move Members"),
    VOICE_USE_VAD(25, true, true, "Use Voice Activity"),

    NICKNAME_CHANGE(26, true, false, "Change Nickname"),
    NICKNAME_MANAGE(27, true, false, "Manage Nicknames"),

    MANAGE_ROLES(28, true, false, "Manage Roles"),
    MANAGE_PERMISSIONS(28, false, true, "Manage Permissions"),
    MANAGE_WEBHOOKS(29, true, true, "Manage Webhooks"),
    MANAGE_EMOTES(30, true, false, "Manage Emojis"),

    UNKNOWN(-1, false, false, "Unknown");

    public static final Permission[] EMPTY_PERMISSIONS = new Permission[0];

    public static final long ALL_PERMISSIONS = Permission.getRaw(Permission.values());

    public static final long ALL_CHANNEL_PERMISSIONS = Permission.getRaw(Arrays.stream(values())
            .filter(Permission::isChannel).collect(Collectors.toList()));

    public static final long ALL_GUILD_PERMISSIONS = Permission.getRaw(Arrays.stream(values())
            .filter(Permission::isGuild).collect(Collectors.toList()));

    public static final long ALL_TEXT_PERMISSIONS = Permission.getRaw(Arrays.stream(values())
            .filter(Permission::isText).collect(Collectors.toList()));

    public static final long ALL_VOICE_PERMISSIONS = Permission.getRaw(Arrays.stream(values())
            .filter(Permission::isVoice).collect(Collectors.toList()));

    private final int offset;
    private final long raw;
    private final boolean isGuild, isChannel;
    private final String name;

    Permission(int offset, boolean isGuild, boolean isChannel, String name)
    {
        this.offset = offset;
        this.raw = 1 << offset;
        this.isGuild = isGuild;
        this.isChannel = isChannel;
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public int getOffset()
    {
        return offset;
    }

    public long getRawValue()
    {
        return raw;
    }

    public boolean isGuild()
    {
        return isGuild;
    }

    public boolean isChannel()
    {
        return isChannel;
    }

    public boolean isText()
    {
        return offset == 6 || (offset > 9 && offset < 20);
    }

    public boolean isVoice()
    {
        return offset == 8 || offset == 10 || (offset > 19 && offset < 26);
    }

    public static Permission getFromOffset(int offset)
    {
        for (Permission perm : values())
        {
            if (perm.offset == offset)
                return perm;
        }
        return UNKNOWN;
    }

    public static List<Permission> getPermissions(long permissions)
    {
        if (permissions == 0)
            return Collections.emptyList();
        List<Permission> perms = new LinkedList<>();
        for (Permission perm : Permission.values())
        {
            if (perm != UNKNOWN && (permissions & perm.raw) == perm.raw)
                perms.add(perm);
        }
        return perms;
    }

    public static EnumSet<Permission> toEnumSet(long permissions)
    {
        EnumSet<Permission> set = EnumSet.noneOf(Permission.class);
        if (permissions == 0)
            return set;
        for (Permission perm : values())
        {
            if (perm != UNKNOWN && (permissions & perm.raw) == perm.raw)
                set.add(perm);
        }
        return set;
    }

    public static long getRaw(Permission... permissions)
    {
        long raw = 0;
        for (Permission perm : permissions)
        {
            if (perm != null && perm != UNKNOWN)
                raw |= perm.raw;
        }

        return raw;
    }

    public static long getRaw(Collection<Permission> permissions)
    {
        Checks.notNull(permissions, "Permission Collection");

        return getRaw(permissions.toArray(EMPTY_PERMISSIONS));
    }
}