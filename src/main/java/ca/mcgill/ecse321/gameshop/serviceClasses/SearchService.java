package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    GameRepository gameRepository;


    @Transactional
    public Set<Game> searchGames(String searchInput) {

        if (searchInput.isEmpty()) {
            Set<Game> games = new HashSet<>();
            gameRepository.findAll().forEach(game -> {if(game.isActive()){games.add(game);}});
            return games;
        }

        String[] words = searchInput.split(" ");

        Set<Game> searchedGames = new HashSet<>();

        gameRepository.findAll().forEach(game -> {

            if(game.isActive()) {

                int matchCounter = 0;
                String[] gameWords = game.getName().split(" ");

                for (String searchedWord : words) {
                    for (String word : gameWords) {
                        if (searchedWord.equalsIgnoreCase(word)) {
                            matchCounter++;
                            break;
                        }
                    }
                }
                if (matchCounter == words.length) {
                    searchedGames.add(game);
                }
            }
        });
        return searchedGames;
    }
}
