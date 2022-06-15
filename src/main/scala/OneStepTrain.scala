package books.spark.definitive.guide.ch24

import org.apache.spark.ml.feature.RFormula
import org.apache.spark.ml.classification.LogisticRegression

import com.typesafe.scalalogging.Logger

import books.spark.definitive.guide.basic.Basic

object OneStepTrain extends Basic {

  val log = Logger(getClass.getName)

  def run(): Unit = {
    val spark = getSession("MLlib in Acion - Simple Case")

    val df = spark.read.json("data/simple-ml")
    df.printSchema

    log.warn("----->  Define transformation. [Transformer]")
    val supervised = (new RFormula()).setFormula("lab ~ . + color:value1 + color:value2")

    log.warn("----->  Fit the RFormula. [Transformer]")
    val fittedRF = supervised.fit(df)

    val preparedDF = fittedRF.transform(df)
    preparedDF.sample(0.1).show(10)

    log.warn("----->  Split data set for train and test")
    val Array(train, test) = preparedDF.randomSplit(Array(0.7, 0.3))

    log.warn("----->  Select classification algorithm. [Estimator]")
    val lr = new LogisticRegression().
      setLabelCol("label").
      setFeaturesCol("features")
    
    println(lr.explainParams())

    log.warn("----->  Do trainning, get one(only) model")
    val fittedLR = lr.fit(train)

    log.warn("----->  Perform prediction on train data set")
    fittedLR.transform(train).select("label", "prediction").sample(0.5).show(10)

    log.warn("----->  Perform prediction on test data set")
    fittedLR.transform(test).select("label", "prediction").sample(0.8).show(10)
  }
}