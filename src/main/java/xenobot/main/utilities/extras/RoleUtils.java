package xenobot.main.utilities.extras;

import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class RoleUtils {

    private static Random random;

    public RoleUtils(Random random) {
        RoleUtils.random = random;
    }

    public static boolean hasRole(List<Role> roles, long checkRole) {
        for(Role role : roles) {
            if (role.getIdLong() == checkRole) {
                return true;
            }
        }
        return false;
    }

    public static Color getColor() {
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
        final float luminance = 1.0f; //1.0 for brighter, 0.0 for black

        return Color.getHSBColor(hue, saturation, luminance);
    }
}
