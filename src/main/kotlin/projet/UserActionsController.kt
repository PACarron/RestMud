package projet

import net.minidev.json.JSONObject
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicInteger

@RestController
class RestController {

    val joueur = AtomicInteger();
    val partie = AtomicInteger();
    val listEntity = mutableListOf<Entity>();
    val mapFirstRoom = mutableMapOf<String, Room>()
    val firstRoom = Room("Bienvenue dans la pièce de départ.", mapFirstRoom, listEntity);
    val listEntityZero = mutableListOf<Entity>();
    val mapRoomZero = mutableMapOf<String, Room>();
    val mapGuidEntity = mutableMapOf<Int, Entity>();

    val roomZero = Room("Bienvenue dans la pièce de départ.", mapRoomZero, listEntityZero);
    val player = Player(200, roomZero);
    var game: Game = createGame(roomZero, player);

    @GetMapping("/connect")
    fun connect() : JSONObject
    {
        val gameToJson = game.toJSON();
        return gameToJson;
    }

    @GetMapping("/{guid}/regarder")
    fun regarder(@PathVariable guid : Int) : JSONObject {
        for((guidPlayer, player) in mapGuidEntity){
            println(player);
            if(guidPlayer == guid){
                return player.currentRoom.toJSON();
            }
        }
        val jsonError = JSONObject();
        jsonError.appendField("erreur", "pas de joueur");
        return jsonError;
    }

    @GetMapping("/{guid}/deplacement")
    fun deplacement(@PathVariable guid : Int, @RequestParam(value="direction", defaultValue = "Choississez une direction.") direction: String) : JSONObject {
        for((guidPlayer, player) in mapGuidEntity){
            if(guidPlayer == guid){
                val passageOfPlayer = player.currentRoom.passages;
                for((directionRoom, nextRoom) in passageOfPlayer){
                    if(direction == directionRoom){
                        player.currentRoom.entity.remove(player);
                        player.currentRoom = nextRoom;
                        nextRoom.insertEntityIntoTheRoom(player);
                        return player.currentRoom.toJSON();
                    }
                }
            }
        }
        val jsonError = JSONObject();
        jsonError.appendField("erreur", "erreur");
        return jsonError;
    }

    @GetMapping("/{guidsource}/examiner/{guiddest}")
    fun examiner(@PathVariable guidsource : Int, @PathVariable guiddest : Int) : JSONObject{
        for ((guidPlayer, player) in mapGuidEntity){
            if (guidPlayer == guidsource) {
                val listOfEntityInCurrentRoom = player.currentRoom.entity;
                for (entity in listOfEntityInCurrentRoom) {
                    for((guidEntity, entityInMap) in mapGuidEntity){
                        if (guidEntity == guiddest && entityInMap.currentRoom == player.currentRoom) {
                            val jsonEntity = JSONObject();
                            val classOfEntity = entity.javaClass.name;
                            jsonEntity.appendField("Classe", classOfEntity);
                            jsonEntity.appendField("totalVie", entity.totalVie);
                            return jsonEntity;
                        }
                    }
                }
            }
        }
        val jsonError = JSONObject();
        jsonError.appendField("erreur", "pas de joueur");
        return jsonError;
    }

    @GetMapping("/{guidsource}/taper/{guidcible}")
    fun taper(@PathVariable guidsource : Int, @PathVariable guidcible : Int) : JSONObject{
        for ((guidPlayer, player) in mapGuidEntity){
            if (guidPlayer == guidsource) {
                val listOfEntityInCurrentRoom = player.currentRoom.entity;
                for (entity in listOfEntityInCurrentRoom) {
                    for((guidEntity, entityInMap) in mapGuidEntity){
                        if (guidEntity == guidcible && entityInMap.currentRoom == player.currentRoom) {
                            val jsonEntity = JSONObject();
                            if(entityInMap.totalVie > 0){
                                entityInMap.totalVie -= 10;
                                if(entityInMap.totalVie == 0){
                                    entityInMap.currentRoom.entity.remove(entityInMap);
                                }
                            }
                            jsonEntity.appendField("totalVie", entityInMap.totalVie);
                            return jsonEntity;
                        }
                    }
                }
            }
        }
        val jsonError = JSONObject();
        jsonError.appendField("erreur", "pas de joueur");
        return jsonError;
    }



    fun createGame(roomZero : Room, p : Player) : Game {
        // First room
        val listEntityFirstRoom = mutableListOf<Entity>();
        val mapFirstRoom = mutableMapOf<String, Room>();
        val firstRoom = Room("Bienvenue dans la pièce n°1.", mapFirstRoom, listEntityFirstRoom);
        val monster1 = Monster(150, firstRoom);
        mapGuidEntity.put(999, monster1);
        firstRoom.insertEntityIntoTheRoom(monster1);

        // Second room
        val listEntitySecondRoom = mutableListOf<Entity>();
        val mapSecondRoom = mutableMapOf<String, Room>();
        val secondRoom = Room("Bienvenue dans la pièce n°2.", mapSecondRoom, listEntitySecondRoom);

        // Third Room
        val listEntityThirdRoom = mutableListOf<Entity>();
        val mapThirdRoom = mutableMapOf<String, Room>();
        val thirdRoom = Room("Bienvenue dans la pièce n°3.", mapThirdRoom, listEntityThirdRoom);
        val monster2 = Monster(150, thirdRoom);
        mapGuidEntity.put(1000, monster2);
        thirdRoom.insertEntityIntoTheRoom(monster2);

        // Fourth room
        val listEntityFourthRoom = mutableListOf<Entity>();
        val mapFourthRoom = mutableMapOf<String, Room>();
        val fourthRoom = Room("Bienvenue dans la pièce n°4.", mapFourthRoom, listEntityFourthRoom);

        mapGuidEntity.put(1, p);
        p.currentRoom = roomZero;
        roomZero.insertEntityIntoTheRoom(p);
        roomZero.addNeighboor("N", firstRoom);
        firstRoom.addNeighboor("N", secondRoom);
        secondRoom.addNeighboor("N", thirdRoom);
        thirdRoom.addNeighboor("N", fourthRoom);

        val game = Game(p, roomZero);
        return game;
    }

}