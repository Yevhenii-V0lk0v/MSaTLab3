package multiagent.lab3.wumpus.environment

import akka.actor.{Actor, ActorRef}
import multiagent.lab2.GameAction
import multiagent.lab2.environment.EnvironmentState
import multiagent.lab3.wumpus.spelunker.RequestGame

class EnvironmentActor(logMap: Boolean) extends Actor {
  val state = new EnvironmentState

  override def receive: Receive = {
    case RequestGame(spel) =>
      println(context.self.path.name + ": received request for a game")
      spel ! "OK"
      context.become(processRequest(spel))
  }

  def processRequest(spelunker: ActorRef): Receive = {
    case "StateRequest" =>
      if (logMap) {
        println(state.toString)
      }
      spelunker ! state.getStatePercept
    case x if x.toString.startsWith("Action") =>
      val action = GameAction.getByPredicateValue("^Action\\((\\S+)\\)$".r.findFirstMatchIn(x.toString).get.group(1))
      action match {
        case GameAction.CLIMB =>
          state.performClimb()
        case GameAction.SHOOT =>
          state.performShot()
        case GameAction.GRAB =>
          state.performGrab()
        case GameAction.FORWARD =>
          state.performForward()
        case GameAction.TURN_LEFT =>
          state.performTurn(action.getNatLangValue)
        case GameAction.TURN_RIGHT =>
          state.performTurn(action.getNatLangValue)
      }
      spelunker ! "OK"
  }

}
