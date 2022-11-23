package guessNumberGame.data;

import guessNumberGame.data.GameDao;
import junit.framework.TestCase;
import guessNumberGame.Service.GameService;
import guessNumberGame.TestApplicationConfiguration;
import guessNumberGame.models.Game;
import guessNumberGame.models.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDatabaseDaoTest extends TestCase {
    @Autowired
    GameDao gameDao;

   @Autowired
   RoundDao roundDao;
   public GameDatabaseDaoTest()
 {}
 
    @Before
    public void setUp() {
        List<Round> rounds = roundDao.getAll();
        for(Round round : rounds) {
            roundDao.deleteById(round.getRound_id());
        }

        List<Game> games = gameDao.getAll();
        for(Game game : games) {
            gameDao.deleteById(game.getGame_id());
        }
    }


    @Test
    public void testAddGetGames() {
        // adds new game using dao
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Game fromDao = gameDao.findById(game.getGame_id());
        assertEquals(game.getGame_id(), fromDao.getGame_id());
    }

    @Test
    public void testGetAll() {
        GameService gameService = new GameService();
        Game game1 = gameService.newGame();
        Game game2 = gameService.newGame();
        gameDao.add(game1);
        gameDao.add(game2);

        List<Game> games = gameDao.getAll();

        assertEquals(2, games.size());
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
    }


    @Test
    public void testUpdate() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);
        game.setFinished(true);
        gameDao.update(game);
        Game updated = gameDao.findById(game.getGame_id());
        assertTrue(updated.isFinished());
    }

    @Test
    public void testDeleteById() {
        GameService gameService = new GameService();
        Game game1 = gameService.newGame();
        Game game2 = gameService.newGame();
        gameDao.add(game1);
        gameDao.add(game2);

        gameDao.deleteById(game1.getGame_id());
        gameDao.deleteById(game2.getGame_id());

        List<Game> games = gameDao.getAll();
        assertEquals(1, games.size());

    }
}
