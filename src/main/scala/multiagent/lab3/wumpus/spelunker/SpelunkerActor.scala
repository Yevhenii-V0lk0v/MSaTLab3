package multiagent.lab3.wumpus.spelunker

import akka.actor.{Actor, ActorRef}
import multiagent.lab2.NaturalLanguageUtils
import multiagent.lab2.spelunker.StatePercept

class SpelunkerActor(environment: ActorRef, navigator: ActorRef) extends Actor {
  var environmentSet: Boolean = false
  var navigatorSet: Boolean = false

  environment ! RequestGame(context.self)
  navigator ! RequestGame(context.self)

  override def receive: Receive = {
    case "OK" =>
      if (environment.equals(sender())) environmentSet = true
      if (navigator.equals(sender())) navigatorSet = true
      if (environmentSet && navigatorSet) {
        environment ! "StateRequest"
        context.become(processState)
      }
    case _ =>
  }

  def processState: Receive = {
    case x if x.toString.startsWith("Percept") =>
      navigator ! NaturalLanguageUtils.transformPerceptToNaturalLanguage(new StatePercept(x.toString))
      context.become(processAction)
    case x if x.toString.startsWith("Win") =>
      navigator ! NaturalLanguageUtils.transformWinToNaturalLanguage(x.toString)
      context.unbecome()
    case x if x.toString.startsWith("Loss") =>
      navigator ! NaturalLanguageUtils.transformLossToNaturalLanguage(x.toString)
      context.unbecome()
    case _ =>
  }

  def processAction: Receive = {
    case message =>
      val parsedAction = NaturalLanguageUtils.transformPhraseIntoAction(message.toString)
      if (parsedAction != null) {
        environment ! String.format("Action(%s)", parsedAction.getPredicateValue)
      }
      context.become(receive)
      if (parsedAction == null) {
        context.self ! "OK"
      }
  }
}

case class RequestGame(spelunker: ActorRef)
