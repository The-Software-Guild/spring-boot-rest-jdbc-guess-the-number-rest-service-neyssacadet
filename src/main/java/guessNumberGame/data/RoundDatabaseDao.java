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

        final String sql = "INSERT INTO guessGame.round(game_id, guess_time, guess, result)"
                + " VALUES(?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, round.getGame_id());
            statement.setTimestamp(2, round.getGuess_time());
            statement.setString(3, round.getGuess());
            statement.setString(4, round.getResult());
            return statement;

        }, keyHolder);

        round.setRound_id(keyHolder.getKey().intValue());

        return round;
    }

    @Override
    public List<Round> getAll() {
        //implement
        final String sql= "SELECT round_id, game_id, guess_time, guess, result"
        + " FROM round";
        return jdbcTemplate.query(sql, new RoundMapper());
    }

    @Override
    public List<Round> getAllOfGame(int game_id) {
        //implement
        final String sql="SELECT * FROM guessGame.round WHERE game_id = ?";
        return jdbcTemplate.query(sql, new RoundMapper(),game_id);
    }

    @Override
    public Round findById(int round_id) {
        //implement }
        final String sql="SELECT round_id,game_id,guess_time, guess, result "
                + " FROM guessGame.round WHERE round_id=?";
        return jdbcTemplate.queryForObject(sql, new RoundMapper(), round_id);
    }

    @Override
    public boolean update(Round round) {
        return false;
    }

    @Override
    public boolean deleteById(int round_id) {
        return jdbcTemplate.update("DELETE FROM round WHERE round_id = ?", round_id) > 0;
    }


    private static final class RoundMapper implements RowMapper<Round> {
        @Override
        //this reads the table row by row
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            //round has 5 private variables therefore:
            round.setRound_id(rs.getInt("round_id"));
            round.setGame_id(rs.getInt("game_id"));
            round.setGuess_time(rs.getTimestamp("guess_time"));
            round.setGuess(rs.getString("guess"));
            round.setResult(rs.getString("result"));
            return round;
        }
    }
}
