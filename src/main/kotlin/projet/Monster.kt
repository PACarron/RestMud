package projet

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class Monster(totalVie: Int, currentRoom: Room): Entity(totalVie, currentRoom) {

}