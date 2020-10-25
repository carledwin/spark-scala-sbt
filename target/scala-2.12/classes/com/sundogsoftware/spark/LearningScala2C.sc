//Flow control

//If / else;
if(1 > 3) println("Impossible") else println("The world makds sense")

if(1 > 3) {
  println("Impossible")
  println("really?")
}else{
  println("The world makes sense")
  print("still")
}

//Matching
val number = 4
number match{
  case 1 => println("one")
  case 2 => println("two")
  case 3 => println("three")
  case _ => println("Something else")
}

//for (x <- 1 to 4) {
//  val squared = x * x
//  println(squared)
//}

var x = 10
while (x >= 0 ) {
  println(x)
  x -= 1
}

//var y = 0
//do{ println(y); y= y + 1} while (y <= 10)

//Expressions
{var z = 10; z + 20 }
println({var z = 10; z + 20 })

//EXERCISE
//Write some code taha prints out the first 10
//values of the Fibonacci sequence
//This is the seqence where every number is the sum of the tow
//numbers before it
//so, the result should be 0,1,1,2,3, 5, 8, 13,21,34