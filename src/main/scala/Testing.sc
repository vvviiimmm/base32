/**
  Scala worksheet for interactive development and testing
 */

val chars = ('a' to 'z') ++ ('A' to 'G')
val bytes = 0 to 31

val bytesToChars = bytes.zip(chars).toMap
val charsToBytes = bytesToChars.map(_.swap)

val input = "Hello world".getBytes()

println("ENCODING")

val toBinary = input.map(_.toInt.toBinaryString.takeRight(8).reverse.padTo(8, "0").reverse.mkString)

val concated = toBinary.foldLeft("")(_ + _)

val padded = concated.padTo(concated.length + 5 - (concated.length % 5), "0").mkString

val grouped = padded.grouped(5).toList

val toBytes = grouped.map(Integer.parseInt(_, 2))

val toChars = toBytes.map(bytesToChars(_))

val encodedString = toChars.foldLeft("")(_ + _)

println("DECODING")

val decToBytes = encodedString.map(charsToBytes(_))

val decToBin = decToBytes.map(_.toBinaryString.reverse.padTo(5, "0").reverse.mkString)

val decConcated = decToBin.foldLeft("")(_ + _)

val trimmed = decConcated.dropRight((encodedString.length * 5) - (((encodedString.length * 5) / 8) * 8))

val decGrouped = trimmed.grouped(8).toList

val decParsed = decGrouped.map(Integer.parseInt(_, 2))

val bResult = decParsed.map(_.toByte).toArray
