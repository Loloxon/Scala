package GameOfLife

import scala.math.random

class GolBoard(var cells: Array[Array[GolCell]] = null, var width: Int = 0, var height: Int = 0){

	def clear(): Unit={
		for (i <- 0 until width) {
			for (j <- 0 until height) {
				cells(i)(j).clear()
			}
		}
	}

	def initialize(width: Int,height: Int): Unit ={
		this.width = width
		this.height = height
		cells = Array.fill(width,height)(new GolCell)
		for(x <- 1 until width-1){
			for(y <- 1 until height-1){
				for(i <- -1 to 1) {
					for(j <- -1 to 1){
						if (!(i == 0 && j == 0)) cells(x)(y).addNeighbour(cells(x + i)(y + j))
					}
				}
			}
		}
	}

	def iteration(): Unit ={
		for(x <- 1 until width-1){
			for(y <- 1 until height-1) {
				cells(x)(y).calculateNewState()
			}
		}
		for(x <- 1 until width-1){
			for(y <- 1 until height-1) {
				cells(x)(y).update()
			}
		}
	}

	def respawn(): Unit ={
		for(x <- 1 until width-1){
			for(y <- 1 until height-1) {
				val r = random
				if(r*100>75)
					cells(x)(y).currentState = 1
			}
		}
	}

	def changeRules(packed: (Array[Int], Array[Int])): Unit={
		val toLive = packed._1
		val	toRevive = packed._2
		for(x <- 1 until width-1){
			for(y <- 1 until height-1) {
				cells(x)(y).changeRules(toLive, toRevive)
			}
		}
	}
}