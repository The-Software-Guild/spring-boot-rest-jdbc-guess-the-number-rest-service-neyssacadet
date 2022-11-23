package guessNumberGame.data;

import guessNumberGame.models.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDatabaseDao implements GameDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game add(Game game) {

        final String sql = "INSERT INTO Game(answer, isFinished) VALUES(?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, game.getAnswer());
            statement.setBoolean(2, game.isFinished());
            return statement;

        }, keyHolder);

        game.setGame_id(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public List<Game> getAll() {
        final String SQL = "SELECT game_id, answer, isFinished FROM game";
        return jdbcTemplate.query(SQL, new GameMapper());
    }


    @Override
    public Game findById(int game_id) {
        final String SQL = "SELECT game_id, answer, isFinished FROM game WHERE game_id = ?";
        return jdbcTemplate.queryForObject(SQL, new GameMapper(), game_id);
    }

    @Override
    public boolean update(Game game) {
        final String SQL = "UPDATE game SET answer = ?, isFinished = ? WHERE game_id = ?";
        return jdbcTemplate.update(SQL, game.getAnswer(), game.isFinished(), game.getGame_id()) > 0;
    }

    @Override
    public boolean deleteById(int game_id) {
        return jdbcTemplate.update("DELETE FROM game WHERE id = ?", game_id) > 0;
    }


        private static final class GameMapper implements RowMapper<Game> {

        @Override
        //to read the information from the database row by row
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            //game only has 3 variables
            game.setGame_id(rs.getInt("game_id"));
            game.setAnswer(rs.getString("answer"));
            game.setFinished(rs.getBoolean("isFinished"));
            return game;
        }
    }
}
