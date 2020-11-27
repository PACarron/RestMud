package projet

import lombok.Getter
import lombok.Setter
import net.minidev.json.JSONObject

@Getter
@Setter
class Player(totalVie: Int, currentRoom : Room): Entity(totalVie, currentRoom) {


    fun toJSON() : JSONObject {
        val json = JSONObject();
        json.appendField("totalVie", this.totalVie);
        return json;
    }
}