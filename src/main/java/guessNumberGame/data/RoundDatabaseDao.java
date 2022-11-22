package guessNumberGame.data;

import guessNumberGame.models.Game;
import guessNumberGame.models.Round;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class RoundDatabaseDao implements RoundDao {
    private final JdbcTemplate jdbcTemplate;
    Scanner sc;

    @Autowired
    public RoundDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Round add(Round round) {

        final String sql = "INSERT INTO Round(game_id, guess_time, guess, result)" +
                " VALUES(?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, round.getGameId());
            statement.setTimestamp(2, round.getTimeStamp());
            statement.setString(3, round.getGuess());
            statement.setString(4, round.getGuessResult());
            return statement;

        }, keyHolder);

        round.setId(keyHolder.getKey().intValue());

        return round;
    }

    @Override
    public List<Round> getAll() {
        final String SQL = "SELECT * FROM Round";
        return jdbcTemplate.query(SQL, new RoundDatabaseDao.RoundMapper());
    }

    @Override
    public List<Round> getAllOfGame(int game_id) {
        final String SQL = "SELECT * FROM Round WHERE game_id = ? ORDER BY guess_time;";
        return jdbcTemplate.query(SQL, new RoundDatabaseDao.RoundMapper(), game_id);
    }

    @Override
    public Round findById(int round_id) {
        final String SQL = "SELECT * FROM Round WHERE round_id = ?;";
        return jdbcTemplate.queryForObject(SQL, new RoundDatabaseDao.RoundMapper(), round_id);
    }

    @Override
    public boolean update(Round round) {
        return false;
    }

    @Override
    public boolean deleteById(int round_id) {
        return jdbcTemplate.update("DELETE FROM Round WHERE id = ?", round_id) > 0;
    }


    private static final class RoundMapper implements RowMapper<Round> {
        @Override
        //this reads the table row by row
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            //round has 5 private variables therefore:
            round.setId(rs.getInt("round_id"));
            round.setGameId(rs.getInt("game_id"));
            round.setTimeStamp(rs.getTimestamp("guess_time"));
            round.setGuess(rs.getString("guess"));
            round.setGuessResult(rs.getString("result"));
            return round;
        }
    }
}
