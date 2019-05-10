package xenobot.main.utilities.extras;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapperPoints implements RowMapper<Users> {

    @Override
    public Users map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Users(rs.getLong("UserId"), rs.getInt("Points"));
    }
}