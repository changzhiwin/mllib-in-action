package books.spark.definitive.guide.ch24

import org.apache.spark.ml.{ Pipeline, PipelineModel }
import org.apache.spark.ml.feature.RFormula
import org.apache.spark.ml.classification.{ LogisticRegression, LogisticRegressionModel }
import org.apache.spark.ml.tuning.ParamGridBuilder
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.tuning.{ TrainValidationSplit, TrainValidationSplitModel}

import com.typesafe.scalalogging.Logger

import books.spark.definitive.guide.basic.Basic

object PipelineTrain extends Basic {

  val log = Logger(getClass.getName)

  def run(): Unit = {
    val spark = getSession("MLlib in Acion - Pipeline Case")

    val df = spark.read.json("data/simple-ml")
    df.printSchema

    val Array(train, test) = df.randomSplit(Array(0.7, 0.3))

    val rForm = new RFormula()
    val lr = new LogisticRegression().
      setLabelCol("label").
      setFeaturesCol("features")

    val stages = Array(rForm, lr)
    val pipeline = new Pipeline().setStages(stages)

    val params = new ParamGridBuilder().
      addGrid(
        rForm.formula,
        Array(
          "lab ~ . + color:value1",
          "lab ~ . + color:value1 + color:value2"
        )
      ).
      addGrid(lr.elasticNetParam, Array(0.0, 0.5, 1.0)).
      addGrid(lr.regParam, Array(0.1, 0.2)).
      build()

    val evaluator = new BinaryClassificationEvaluator().
      setMetricName("areaUnderROC").
      setRawPredictionCol("prediction").
      setLabelCol("label")

    val tvs = new TrainValidationSplit().
      setTrainRatio(0.75).
      setEstimatorParamMaps(params).
      setEstimator(pipeline).
      setEvaluator(evaluator)

    // Get the best model
    val tvsFitted = tvs.fit(train)

    // Prediction
    val quality = evaluator.evaluate(tvsFitted.transform(test))

    log.warn("----->  Trained model's quality = {}", quality)

    val trainedPipeline = tvsFitted.bestModel.asInstanceOf[PipelineModel]
    val trainLR = trainedPipeline.stages(1).asInstanceOf[LogisticRegressionModel]
    val summaryLR = trainLR.summary
    log.warn("----->  Objective history = {}", summaryLR.objectiveHistory.map(s => s.toString).mkString(", "))

    tvsFitted.write.overwrite().save("/tmp/definitive/modelLocation")

    // load model
    val model = TrainValidationSplitModel.load("/tmp/definitive/modelLocation")
    val predictDF = model.transform(test)
    predictDF.show(10)
  }
}