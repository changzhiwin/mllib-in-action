# Purpose
This is full practice of chapter 24 in `Spark The Definitive Guide`.

# Environment
- Java 8
- Scala 2.13.8
- Spark 3.2.1

# How to run
## 1, sbt package, in project root dir
When success, there a jar file at ./target/scala-2.13. The name is `main-scala-ch24_2.13-1.0.jar` (the same as name property in sbt file)


## 2, submit jar file, in project root dir
```
// io.delta:delta-core_2.13:1.2.1,  not use

$ YOUR_SPARK_HOME/bin/spark-submit    \
  --class books.spark.definitive.guide.entry.MainApp  \
  --master "local[*]"   \
  --packages com.typesafe.scala-logging:scala-logging_2.13:3.9.4 \
  target/scala-2.13/main-scala-ch24_2.13-1.0.jar
```

## 3, print

### Case: OneStepTrain
```
root
 |-- color: string (nullable = true)
 |-- lab: string (nullable = true)
 |-- value1: long (nullable = true)
 |-- value2: double (nullable = true)

+-----+----+------+------------------+--------------------+-----+
|color| lab|value1|            value2|            features|label|
+-----+----+------+------------------+--------------------+-----+
|green| bad|    16|14.386294994851129|(10,[1,2,3,5,8],[...|  0.0|
|  red|good|    35|14.386294994851129|(10,[0,2,3,4,7],[...|  1.0|
|green|good|    12|14.386294994851129|(10,[1,2,3,5,8],[...|  1.0|
|  red|good|    35|14.386294994851129|(10,[0,2,3,4,7],[...|  1.0|
|  red| bad|    16|14.386294994851129|(10,[0,2,3,4,7],[...|  0.0|
| blue| bad|     8|14.386294994851129|(10,[2,3,6,9],[8....|  0.0|
|green| bad|    16|14.386294994851129|(10,[1,2,3,5,8],[...|  0.0|
| blue| bad|     8|14.386294994851129|(10,[2,3,6,9],[8....|  0.0|
+-----+----+------+------------------+--------------------+-----+

aggregationDepth: suggested depth for treeAggregate (>= 2) (default: 2)
elasticNetParam: the ElasticNet mixing parameter, in range [0, 1]. For alpha = 0, the penalty is an L2 penalty. For alpha = 1, it is an L1 penalty (default: 0.0)
family: The name of family which is a description of the label distribution to be used in the model. Supported options: auto, binomial, multinomial. (default: auto)
featuresCol: features column name (default: features, current: features)
fitIntercept: whether to fit an intercept term (default: true)
labelCol: label column name (default: label, current: label)
lowerBoundsOnCoefficients: The lower bounds on coefficients if fitting under bound constrained optimization. (undefined)
lowerBoundsOnIntercepts: The lower bounds on intercepts if fitting under bound constrained optimization. (undefined)
maxBlockSizeInMB: Maximum memory in MB for stacking input data into blocks. Data is stacked within partitions. If more than remaining data size in a partition then it is adjusted to the data size. Default 0.0 represents choosing optimal value, depends on specific algorithm. Must be >= 0. (default: 0.0)
maxIter: maximum number of iterations (>= 0) (default: 100)
predictionCol: prediction column name (default: prediction)
probabilityCol: Column name for predicted class conditional probabilities. Note: Not all models output well-calibrated probability estimates! These probabilities should be treated as confidences, not precise probabilities (default: probability)
rawPredictionCol: raw prediction (a.k.a. confidence) column name (default: rawPrediction)
regParam: regularization parameter (>= 0) (default: 0.0)
standardization: whether to standardize the training features before fitting the model (default: true)
threshold: threshold in binary classification prediction, in range [0, 1] (default: 0.5)
thresholds: Thresholds in multi-class classification to adjust the probability of predicting each class. Array must have length equal to the number of classes, with values > 0 excepting that at most one value may be 0. The class with largest value p/t is predicted, where p is the original probability of that class and t is the class's threshold (undefined)
tol: the convergence tolerance for iterative algorithms (>= 0) (default: 1.0E-6)
upperBoundsOnCoefficients: The upper bounds on coefficients if fitting under bound constrained optimization. (undefined)
upperBoundsOnIntercepts: The upper bounds on intercepts if fitting under bound constrained optimization. (undefined)
weightCol: weight column name. If this is not set or empty, we treat all instance weights as 1.0 (undefined)

----->  Perform prediction on train data set
+-----+----------+
|label|prediction|
+-----+----------+
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
+-----+----------+
only showing top 10 rows


----->  Perform prediction on test data set
+-----+----------+
|label|prediction|
+-----+----------+
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  0.0|       0.0|
|  1.0|       1.0|
|  1.0|       1.0|
|  1.0|       1.0|
+-----+----------+
only showing top 10 rows
```

### Case: Pipeline
```
----->  Trained model's quality = 0.9 

----->  Objective history = 0.6834216514972252, 0.4252929710190926, 0.4018487597341551, 0.38388148358632335, 0.38211442790906586, 0.3820432664181048, 0.38201391214262026, 0.3820132294380923, 0.382013098146724, 0.3820130906723006, 0.3820130905664508, 0.38201309054911853, 0.38201309054877997

+-----+----+------+------------------+--------------------+-----+--------------------+--------------------+----------+
|color| lab|value1|            value2|            features|label|       rawPrediction|         probability|prediction|
+-----+----+------+------------------+--------------------+-----+--------------------+--------------------+----------+
| blue| bad|     8|14.386294994851129|(10,[2,3,6,9],[8....|  0.0|[2.58505301729552...|[0.92989340014310...|       0.0|
| blue| bad|    12|14.386294994851129|(10,[2,3,6,9],[12...|  0.0|[2.73484631550830...|[0.93905179976190...|       0.0|
| blue| bad|    12|14.386294994851129|(10,[2,3,6,9],[12...|  0.0|[2.73484631550830...|[0.93905179976190...|       0.0|
| blue| bad|    12|14.386294994851129|(10,[2,3,6,9],[12...|  0.0|[2.73484631550830...|[0.93905179976190...|       0.0|
|green| bad|    16|14.386294994851129|(10,[1,2,3,5,8],[...|  0.0|[-0.2417764817841...|[0.43984861127384...|       1.0|
|green| bad|    16|14.386294994851129|(10,[1,2,3,5,8],[...|  0.0|[-0.2417764817841...|[0.43984861127384...|       1.0|
|green| bad|    16|14.386294994851129|(10,[1,2,3,5,8],[...|  0.0|[-0.2417764817841...|[0.43984861127384...|       1.0|
|green|good|     1|14.386294994851129|(10,[1,2,3,5,8],[...|  1.0|[-0.3600297372666...|[0.41095236740669...|       1.0|
|green|good|     1|14.386294994851129|(10,[1,2,3,5,8],[...|  1.0|[-0.3600297372666...|[0.41095236740669...|       1.0|
|green|good|     1|14.386294994851129|(10,[1,2,3,5,8],[...|  1.0|[-0.3600297372666...|[0.41095236740669...|       1.0|
+-----+----+------+------------------+--------------------+-----+--------------------+--------------------+----------+
```

## 4, Some diffcult case

### trun off INFO logs
```
cd SPARK_HOME/conf
cp log4j.properties.template log4j.properties
vim log4j.properties

// change one line config
log4j.rootCategory=INFO, console
// to
log4j.rootCategory=WARN, console
```

