package projet

import lombok.Getter
import lombok.Setter
import net.minidev.json.JSONObject

@Getter
@Setter
class Room(var description: String, var passages: MutableMap<String, Room>, var entity: MutableList<Entity>){

    fun insertEntityIntoTheRoom(e : Entity) {
        entity.add(e);
    }

    fun addNeighboor(d : String, r : Room){
        passages.put(d, r);
    }

    override fun toString() : String {
        return description;
    }

    fun toJSON() : JSONObject {
        val json = JSONObject();
        json.appendField("description", this.description);
        json.appendField("direction", this.passages.keys.toString());
        json.appendField("entités", this.entity.toString());
        return json;
    }
}