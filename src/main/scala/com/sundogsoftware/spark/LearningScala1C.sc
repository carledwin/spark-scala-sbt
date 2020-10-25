//VALUES ARE IMMUTABLE CONSTANTS
  val hello: String = "Hola!"
  val helloPlusThere: String  = hello + "There!"
  println(helloPlusThere)

//VALUES ARE MUTABLE
  var helloThere: String = hello
  helloThere = hello + " there!"
  println(helloThere)

//Data Types
  val numberOne: Int = 1
  val truth: Boolean = true
  val letterA: Char = 'a'
  val pi: Double = 3.14159265
  val piSinglePrecision: Float = 3.14159265f
  val bigNumber: Long = 123456789
  val smallNumber: Byte = 127

 // println("Here is a mess: " + numberOne + truth + letterA + pi)

//isn't work
  //  println(f"Zero padding on the left: $numberOne%05d")
  //  println(f"Pi is about $piSinglePrecision%.3f")
  //  println(s"I can use th s prefix to use variables like $numberOne $truth $letterA")
  //  println(s"The s prefix isnt limited to variables; I can include any expression. Like ${1 + 2}")

  val theUltimateAnswer: String = "To life, the universe, and everything is 42."
  //val pattern = """.* ([\d]+).*""".r
  //val pattern(answerString) = theUltimateAnswer
  //val answer = answerString.toInt
  //println(answer)

//Booleans
val isGreater = 1 > 2
val isLesser = 1 < 2
val impossible = isGreater & isLesser
val anotherWay = isGreater || isLesser

val picard: String = "Picard"
val bestCaptain: String = "Picard"
val isBest: Boolean = picard == bestCaptain

//EXERCISE
//Write some code that takes the value of pi, doublesi it, and then print within a string
//with three decimal places of presision to the right.