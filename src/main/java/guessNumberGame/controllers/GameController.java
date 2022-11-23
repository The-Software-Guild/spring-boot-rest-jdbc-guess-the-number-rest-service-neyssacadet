package guessNumberGame.controllers;

import guessNumberGame.Service.GameService;
import guessNumberGame.data.GameDao;
import guessNumberGame.data.RoundDao;
import guessNumberGame.models.Game;

import java.util.List;

import guessNumberGame.models.Round;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
//annotation to turn any Java class (a POJO) into a web-enabled controller.
@RequestMapping("/api")
//this class can only handle URLs that begin with "/api"
public class GameController {


    private final GameDao gameDao;
    private final RoundDao roundDao;

    public GameController(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @PostMapping("/begin")
    //executes our method if an HTTP request's method is POST and the URL is "/api/begin".
    @ResponseStatus(HttpStatus.CREATED)
    public Game create() {
        //implement create gameService object and game object
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        //add to database
        gameDao.add(game);
        //getGame will hide answer before returning it to the user
        return gameService.getGames(game);
    }


    @PostMapping("/guess")
    //executes our method if an HTTP request's method is POST and the URL is "/api/guess".
    @ResponseStatus(HttpStatus.CREATED)
    //marks a method or exception class with the status code and reason message that should be returned.
    public Round guessNumber(@RequestBody Round body) {
        Game game = gameDao.findById(body.getGame_id());
        GameService gameService = new GameService();
        Round round = gameService.guessNumber(game, body.getGuess(), gameDao);
        return roundDao.add(round);
    }

    @GetMapping("/game")
    //signals to Spring MVC that this method can only handle HTTP requests using the GET method
    public List<Game> all() {
        List<Game> games = gameDao.getAll();
        GameService gameService = new GameService();
        gameService.getAllGames(games);
        return games;
    }

    @GetMapping("game/{id}")
    public Game getGameById(@PathVariable int id) {
        Game game = gameDao.findById(id);
        GameService gameService = new GameService();
        return gameService.getGames(game);
    }

    @GetMapping("rounds/{game_id}")
    public List<Round> getGameRounds(@PathVariable int game_id) {
        return roundDao.getAllOfGame(game_id);
    }
}