package xenobot.main.utilities.extras;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import xenobot.main.Constants;
import xenobot.main.utilities.Database;

import java.util.List;
import java.util.Random;

public class PointUtils {

    public static int computeBonusPoints(int msgCount) {
        Random random = new Random();
        if (msgCount < 5000) {
            for (int x = 100; x < 5000; x = x + 125) {
                if (msgCount == x) {
                    int add = random.nextInt(10) + 11;
                    return (x / 100) + add;
                }
            }
        }
        if (msgCount >= 5000 && msgCount <= 10000) {
            for (int x = 5000; x <= 10000; x = x + 500) {
                if (msgCount == x) {
                    int add = random.nextInt(40) + 1;
                    return ((x / 100) * 2) + add;
                }
            }
        }
        return 0;
    }
    public static void addBankInterest(Database database, Member member) {
        int amount = database.getBankBalance(member.getIdLong());
        int amountToAdd;
        double interest = getInterestAmount(member.getRoles());
        double amountDoub = amount * interest;
        amountToAdd = (int) amountDoub;

        if (amountToAdd != 0) {
            database.addBankBalance(member.getIdLong(), amountToAdd);
        }
    }
    private static double getInterestAmount(List<Role> roles) {
        for(Role role : roles) {
            switch(role.getId()) {
                case Constants.UPPER_CLASS_S:
                    return .08;
                case Constants.MIDDLE_CLASS_S:
                    return  .05;
                case Constants.LOWER_CLASS_S:
                    return  .02;
                default:
            }
        }
        return 0;
    }
}