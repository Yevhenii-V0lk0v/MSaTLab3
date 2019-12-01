package multiagent.lab3.wumpus.navigator

import akka.actor.{Actor, ActorRef}
import multiagent.lab2.navigator.behaviour.WumpusBot
import multiagent.lab3.wumpus.spelunker.RequestGame

class NavigatorActor(pc: Boolean, botLogging: Boolean) extends Actor {
  val bot = new WumpusBot(botLogging)

  override def receive: Receive = {
    case RequestGame(spel) =>
      println(context.self.path.name + ": received request for a game")
      spel ! "OK"
      context.become(processState(spel))
  }

  def processState(spelunker: ActorRef): Receive = {
    case state =>
      val stateStr = state.toString
      println(stateStr)
      if (stateStr.contains("Current tick")) {
        if (pc) {
          spelunker ! bot.getCommand(stateStr)
        } else {
          println("Your move?")
          spelunker ! scala.io.StdIn.readLine()
        }
      } else {
        context.system.terminate()
      }
  }

}

