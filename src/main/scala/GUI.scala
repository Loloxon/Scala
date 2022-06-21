import GameOfLife.Modes.toRules
import GameOfLife.{GolBoard, Modes}
import scalafx.Includes._
import scalafx.animation._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.geometry.Pos.Center
import scalafx.scene._
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

import scala.sys.exit

object GUI extends JFXApp{
  val rectangle_size = 4
  val scene_size_x = 1024
  val scene_size_y = 640
  stage = new JFXApp.PrimaryStage{
    val width1 = 1200
    val height1 = 690
    scene = new Scene(width1,height1){
      val rootPane = new BorderPane
      rootPane.center = makeGoLSim()
      root = rootPane
    }
  }
  stage.resizable = false

  def makeGoLGrid(buttonList: Map[String, Button], timeScroll: ScrollBar, modesList: ListView[Modes.Value]): GridPane = {
    val grid = new GridPane
    val width = scene_size_x/rectangle_size
    val height = scene_size_y/rectangle_size
    val rectangles = Array.fill(
      scene_size_x / rectangle_size, scene_size_y / rectangle_size)(Rectangle(rectangle_size, rectangle_size, Grey))
    println(rectangles.length)
    println(rectangles(0).length)
    println(rectangle_size)
    for (i <- 0 until width) {
      for(j <- 0 until height) {
        grid.add(rectangles(i)(j), i, j)
      }
    }

    val board = new GolBoard
    board.initialize(width,height)

    timeScroll.min = 0
    timeScroll.max = 7
    timeScroll.value = 2

    var lastTime = 0.0
    var total_delay = math.exp(-timeScroll.value.apply)
    var updateDelay = total_delay
    var running = false
    val timer:AnimationTimer = AnimationTimer(t => {
      if(lastTime>0){
        val delta = (t-lastTime)/1e9
        updateDelay-=delta
        if(updateDelay<0) {
          board.iteration()
          for (i <- 0 until width) {
            for (j <- 0 until height) {
              rectangles(i)(j).fill = board.cells(i)(j).color
            }
          }
          updateDelay=total_delay
        }
      }
      timeScroll.value.onChange {
        total_delay = math.exp(-timeScroll.value.apply)
      }
      lastTime = t
    })
    buttonList("respawn").onAction = (e: ActionEvent) => {
      if(!running) {
        board.respawn()
        for (i <- 0 until width) {
          for (j <- 0 until height) {
            rectangles(i)(j).fill = board.cells(i)(j).color
          }
        }
      }
    }
    buttonList("clear").onAction = (e: ActionEvent) => {
      if(!running) {
        board.clear()
        for (i <- 0 until width) {
          for (j <- 0 until height) {
            rectangles(i)(j).fill = board.cells(i)(j).color
          }
        }
      }
    }
    buttonList("settings").onAction = (e: ActionEvent) => {
      var idk = Modes.Basic
      if(modesList.selectionModel.apply.getSelectedItems.size()!=0){
        idk = modesList.selectionModel.apply.getSelectedItems.get(0)
      }
      println(idk)
      board.changeRules(toRules(idk))
    }
    buttonList("stop").onAction = (e: ActionEvent) => {
      if(running) {
        timer.stop
        buttonList("stop").text = "Resume"
        running = false
      }
      else{
        timer.start
        buttonList("stop").text = "Pause"
        running = true
      }
    }
    grid
  }

  def makeGoLSim(): Pane = {
    val mainBorder = new BorderPane

    val title = new Text("Game of life simulation")
    title.minHeight(50)
    title.alignmentInParent = Center
    mainBorder.top = title

    val controls = new GridPane
    val buttonsRow = new GridPane
    val stopButton = new Button("Start")
    stopButton.prefWidth = 80
    val clearButton = new Button("Clear")
    clearButton.prefWidth = 80
    val respawnButton = new Button("Respawn")
    respawnButton.prefWidth = 80
    val settingsButton = new Button("Save")
    settingsButton.prefWidth = 80

    buttonsRow.add(stopButton,0,0)
    buttonsRow.add(respawnButton,1,0)
    buttonsRow.add(settingsButton,0,1)
    buttonsRow.add(clearButton,1,1)
    buttonsRow.prefWidth = 160

    controls.add(buttonsRow, 0, 0)

    val modesList = new ListView(List.tabulate(Modes.values.size)(i=>Modes(i)))
    modesList.prefWidth = 80
    controls.add(modesList,0,1)

    val exitButton = new Button("Exit")
    exitButton.onAction = (e:ActionEvent) => {
      exit(77)
    }
    exitButton.prefWidth = 160
    exitButton.prefHeight = 40
    controls.add(exitButton,0,2)

    mainBorder.left = controls

    val timeScroll = new ScrollBar()
    timeScroll.prefHeight = 30
    timeScroll.maxWidth = 500
    timeScroll.alignmentInParent = Center
    mainBorder.bottom = timeScroll

    val buttonList = Map("stop"->stopButton, "clear"->clearButton, "respawn"->respawnButton, "settings"->settingsButton)


    val grid = makeGoLGrid(buttonList, timeScroll, modesList)
    mainBorder.center = grid

    mainBorder

  }

}