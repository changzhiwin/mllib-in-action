package books.spark.definitive.guide.entry

import books.spark.definitive.guide.ch24.{ OneStepTrain, PipelineTrain }

object MainApp {
  def main(args: Array[String]) = {

    val (whichCase, otherArg) = args.length match {
      case 1 => (args(0).toUpperCase, "")
      case 2 => (args(0).toUpperCase, args(1).toUpperCase)
      case _ => ("", "")
    }

    println(s"=========== whichCase = $whichCase, otherArg = $otherArg ===========")

    whichCase match {
      case "PIPE"     => PipelineTrain.run()
      case _          => OneStepTrain.run()
    }
  }
}