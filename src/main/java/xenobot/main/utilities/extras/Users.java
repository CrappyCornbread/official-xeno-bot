package xenobot.main.utilities.extras;

public class Users {
    private long userId;
    private int amount;

    Users(long userId) {
        this.userId = userId;
    }

    Users(long userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public long getUserId() {
        return this.userId;
    }
    public int getMsgCount() {
        return this.amount;
    }
    public int getBankBalance() {
        return this.amount;
    }
}