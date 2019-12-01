package multiagent.lab3.wumpus

import akka.actor.{ActorSystem, Props}
import multiagent.lab3.wumpus.environment.EnvironmentActor
import multiagent.lab3.wumpus.navigator.NavigatorActor
import multiagent.lab3.wumpus.spelunker.SpelunkerActor

object WumpusMain {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("WumpusSystem")
    val logMap = if (args.length > 0) args(0).toBoolean else true
    val usePc = if (args.length > 1) args(1).toBoolean else true
    val botLogging = if (args.length > 2) args(2).toBoolean else true
    val envActor = system.actorOf(Props(new EnvironmentActor(logMap)), "environment")
    val navActor = system.actorOf(Props(new NavigatorActor(usePc, botLogging)), "navigator")
    system.actorOf(Props(new SpelunkerActor(envActor, navActor)), "spelunker")
  }
}
