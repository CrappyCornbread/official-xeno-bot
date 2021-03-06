package xenobot.main.utilities;

import org.jdbi.v3.core.Jdbi;
import xenobot.main.utilities.extras.*;

import java.util.List;

public class Database {

    // jdbc:postgresql://localhost:5432/postgres?user=postgres&password=StoreMyData88Now
    private final String DB_URL = "postgres://yziauimhodlfxu:93282102cce1a8b57ec8cbf1a100a5e4f4ca767f882434b22fce0ba51dfa13bb@ec2-54-221-198-156.compute-1.amazonaws.com:5432/dbgotodc5bvvc8";
    private final Jdbi jdbi = Jdbi.create(DB_URL);

    public void addMessageCounter(long userId) {

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO UserInformation(UserId, MessageCount, Points, Bank)\n" +
                "VALUES (:id, :count, :points, :bank)\n" +
                "ON CONFLICT (UserId) DO\n" +
                "UPDATE\n" +
                "SET MessageCount = UserInformation.MessageCount + :count\n" +
                "WHERE UserInformation.UserId = :id;")
                .bind("id", userId)
                .bind("count", 1)
                .bind("points", 0)
                .bind("bank", 0)
                .execute());
    }
    public void addPoints(long userId, int points) {

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO UserInformation(UserId, MessageCount, Points, Bank)\n" +
                "VALUES (:id, :count, :points, :bank)\n" +
                "ON CONFLICT (UserId) DO\n" +
                "UPDATE\n" +
                "SET Points = UserInformation.Points + :points\n" +
                "WHERE UserInformation.UserId = :id;")
                .bind("id", userId)
                .bind("count", 0)
                .bind("points", points)
                .bind("bank", 0)
                .execute());
    }
    public void subtractPoints(long userId, int points) {

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO UserInformation(UserId, MessageCount, Points, Bank)\n" +
                "VALUES (:id, :count, :points, :bank)\n" +
                "ON CONFLICT (UserId) DO\n" +
                "UPDATE\n" +
                "SET Points = UserInformation.Points - :points\n" +
                "WHERE UserInformation.UserId = :id;")
                .bind("id", userId)
                .bind("count", 0)
                .bind("points", points)
                .bind("bank", 0)
                .execute());
    }
    public void addBankBalance(long userId, int bal) {

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO UserInformation(UserId, MessageCount, Points, Bank)\n" +
                "VALUES (:id, :count, :points, :bank)\n" +
                "ON CONFLICT (UserId) DO\n" +
                "UPDATE\n" +
                "SET Bank = UserInformation.Bank + :bank\n" +
                "WHERE UserInformation.UserId = :id;")
                .bind("id", userId)
                .bind("count", 0)
                .bind("points", 0)
                .bind("bank", bal)
                .execute());
    }
    public void subtractBankBalance(long userId, int bal) {

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO UserInformation(UserId, MessageCount, Points, Bank)\n" +
                "VALUES (:id, :count, :points, :bank)\n" +
                "ON CONFLICT (UserId) DO\n" +
                "UPDATE\n" +
                "SET Bank = UserInformation.Bank - :bank\n" +
                "WHERE UserInformation.UserId = :id;")
                .bind("id", userId)
                .bind("count", 0)
                .bind("points", 0)
                .bind("bank", bal)
                .execute());
    }
    public int getMessageCount(long userId) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT MessageCount FROM UserInformation WHERE UserId = " + userId)
                .mapTo(int.class).findFirst().orElse(-1));
    }
    public int getPoints(long userId) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT Points FROM UserInformation WHERE UserId = " + userId)
                .mapTo(int.class).findFirst().orElse(-1));
    }
    public int getBankBalance(long userId) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT Bank FROM UserInformation WHERE UserId = " + userId)
                .mapTo(int.class).findFirst().orElse(-1));
    }
    public List<Users> getCountLeaderboard() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT UserId, MessageCount FROM UserInformation\n" +
                "ORDER BY (MessageCount) DESC;")
                .map(new UserMapperCount()).list());
    }
    public List<Users> getBankLeaderboard() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT UserId, Bank FROM UserInformation\n" +
                "ORDER BY (Bank) DESC;")
                .map(new UserMapperBank()).list());
    }
    public void resetWeeklyLeaderboards() {
        jdbi.withHandle(handle -> handle.createUpdate("UPDATE UserInformation SET MessageCount = 0;")
                .execute());

    }
    public void cleanDatabase() {
        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM UserInformation WHERE\n" +
                "MessageCount = 0 AND\n" +
                "Points = 0 AND\n" +
                "Bank = 0;")
                .execute());
    }
}