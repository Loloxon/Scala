package GameOfLife

object Modes extends Enumeration {
  type Mode = Value

  val Basic, Coral, Labirynt, Replicator, Ameba, Day_and_night, Cities_with_wall, Diameba = Value

  def toRules(m: Mode): (Array[Int], Array[Int]) = { //(fromLive, toLive, fromRevive, toRevive)
    m match {
      case Basic => (Array(2,3),Array(3))
      case Coral => (Array(4,5,6,7,8),Array(3))
      case Labirynt => (Array(1,2,3,4,5),Array(3))
      case Replicator => (Array(1,3,5,7),Array(1,3,5,7))
      case Ameba => (Array(1,3,5,8),Array(3,5,7))
      case Day_and_night => (Array(3,4,6,7,8),Array(3,6,7,8))
      case Diameba => (Array(5,6,7,8),Array(3,5,6,7,8))
      case Cities_with_wall => (Array(2,3,4,5),Array(4,5,6,7,8))
    }
  }
}
