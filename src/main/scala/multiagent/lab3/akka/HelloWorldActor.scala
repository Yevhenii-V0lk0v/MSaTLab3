package multiagent.lab3.akka

import akka.actor.{Actor, ActorSystem, Props}

class HelloWorldActor extends Actor {
  override def receive: Receive = {
    case "hello" => println("Hello back at you!")
    case _ => println("Um... What, sorry?")
  }
}

class FredActor(name: String) extends Actor {
  override def receive: Receive = {
    case "hello" => println("Hi! The name is " + name)
    case _ => println("Do I know you?")
  }
}

object AkkaMain extends App {
  val system = ActorSystem("AkkaSystem")
  val helloActor = system.actorOf(Props[HelloWorldActor], "HelloWorldActor")
  helloActor ! "добрый вечер"
  helloActor ! "hello"
  val fredActor = system.actorOf(Props(new FredActor("Fred")), "FredActor")
  fredActor ! "добрый вечер"
  fredActor ! "hello"
  system.terminate()
}
