package gameClient;

import Server.Game_Server_Ex2;
import api.game_service;

public class Ex2 {
    public static void main(String[] args){
        int level_num = 0;
        game_service game = Game_Server_Ex2.getServer(level_num);

        System.out.println(game);
        System.out.println(game.getGraph());
        System.out.println(game.getPokemons());

        game.addAgent(2);
        System.out.println(game.getAgents());

        game.startGame();

        while (game.isRunning()){
            game.chooseNextEdge(0,1);
            game.move();
        }

    }
}
