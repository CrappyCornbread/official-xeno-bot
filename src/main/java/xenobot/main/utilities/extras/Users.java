package xenobot.main.utilities.extras;

public class Users {
    private long userId;
    private int msgCount;
    private int points;
    private int bank;

    public Users() {}
    public Users(long userId, int msgCount, int points, int bank) {
        this.userId = userId;
        this.msgCount = msgCount;
        this.points = points;
        this.bank = bank;
    }
    public Users(long userId, int msgCount, int points) {
        this.userId = userId;
        this.msgCount = msgCount;
        this.points = points;
    }
    public Users(long userId, int msgCount) {
        this.userId = userId;
        this.msgCount = msgCount;
    }
    public Users(long userId) {
        this.userId = userId;
    }


    public long getUserId() {
        return this.userId;
    }
    public int getMsgCount() {
        return this.msgCount;
    }
    public int getBankBalance() {
        return this.bank;
    }
    public int getPoints() { return this.points; }
}