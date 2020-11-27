package projet

import lombok.Getter
import lombok.Setter
import net.minidev.json.JSONObject

@Getter
@Setter
class Game(var player: Player, var firstSalle : Room) {


    fun toJSON(): JSONObject {
        val json = JSONObject();
        val jsonPlayer = this.player.toJSON();
        val jsonSalle = this.firstSalle.toJSON();
        json.appendField("Player", jsonPlayer);
        json.appendField("Room", jsonSalle);
        return json;
    }


}