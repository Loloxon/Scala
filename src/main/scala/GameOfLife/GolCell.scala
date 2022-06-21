package GameOfLife

import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._

class GolCell(var neighbours: Array[GolCell] = Array(), var currentState: Int = 0, var nextState: Int = 0, val types: Int = 6,
							var toLive: Array[Int] = Array(2,3), var toRevive: Array[Int] = Array(3)){

	def clear(): Unit ={
		currentState = 0
	}

	def clicked(): Unit ={
		currentState = (currentState+1)%types
	}

	def changeRules(toLive: Array[Int], toRevive: Array[Int]): Unit ={
		this.toLive = toLive
		this.toRevive = toRevive
	}

	def calculateNewState(): Unit = {
		val san = sumAliveNeighbors
		if (currentState == 1) if (toLive contains san) nextState = 1
		else nextState = 0
		if (currentState == 0) if (toRevive contains san) nextState = 1
		else nextState = 0
	}

	def sumAliveNeighbors: Int = {
		var tmp = 0
		for(n <- neighbours)
			if(n.currentState==1) tmp+=1
		tmp
	}

	//  def drop(): Unit = {
	//    val random = new Random
	//    if (random.nextInt(100) < 5) this.currentState = 6
	//  }

	def update(): Boolean ={
		if(currentState==nextState)
			return false
		currentState = nextState
		true
	}

	def color: Color ={
		currentState match {
			case 0  => Grey
			case 1  => Green
			case 2  => Blue
			case 3  => Yellow
			case 4  => Red
			case 5  => Red
			case _  => Black
		}
	}
	def addNeighbour(c: GolCell): Unit ={
		neighbours = neighbours :+ c
	}

	//  override def addNeighbour(c: Any): Unit = ???
}