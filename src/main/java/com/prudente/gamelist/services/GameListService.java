package com.prudente.gamelist.services;

import com.prudente.gamelist.dto.GameListDTO;
import com.prudente.gamelist.entities.GameList;
import com.prudente.gamelist.projections.GameMinProjection;
import com.prudente.gamelist.repositories.GameListRepository;
import com.prudente.gamelist.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameListService {

    @Autowired
    private GameListRepository gameListRepository;

    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public List<GameListDTO> findAll() {
        List<GameList> result = gameListRepository.findAll();
        return result.stream().map(gameList -> new GameListDTO(gameList)).toList();
    }

    @Transactional
    public void move(Long listId, int sourceIndex, int destinationIndex) {

        List<GameMinProjection> list = gameRepository.searchByList(listId);
        GameMinProjection game = list.remove(sourceIndex);
        list.add(destinationIndex, game);

        int min = Math.min(sourceIndex, destinationIndex);
        int max = Math.max(sourceIndex, destinationIndex);

        for (int i = min; i <= max; i++){
            gameListRepository.updateBelongingPosition(listId, list.get(i).getId(), i);
        }
    }


}
