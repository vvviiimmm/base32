/**
  * MIT License
  * Copyright (c) [2016] [Alexey Avramenko]
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */

/**
  * Base32 encoding work similarly to Base64 but uses 5 bits patterns for encoded character index number
  * (instead of 6 bits in Base64) which allows us to encode any sequence of bytes using only 26 lowercase
  * and 6 uppercase english characters.
  */
object Base32 {
  val chars = ('a' to 'z') ++ ('A' to 'G')
  val bytes = 0 to 32

  val bytesToChars = bytes.zip(chars).toMap   // bytes -> encoded characters mapping
  val charsToBytes = bytesToChars.map(_.swap) // encoded characters -> bytes maoong

  def encode(bytes: Array[Byte]): String = {
    val binaryString = bytes
      .map(                   // for each byte in the array
          _.toInt             // covert byte to base10 integer
          .toBinaryString     // convert integer to binary (9 == "1001")
          .takeRight(8)       // take only significant 8 bits, discard 0s or 1s in case of negative number
          .reverse            // reverse before padding
          .padTo(8, "0")      // add zeros to form 8 characters binary string
          .reverse            // reverse it back
          .mkString)          // back to string
      .foldLeft("")(_ + _)    // concatenate everything into a single well formatted binary string

    binaryString
      .padTo(binaryString.length + 5 - (binaryString.length % 5), "0").mkString // add trailing 0s to the end to make the sequence length multiple of 5
      .grouped(5)                     // group by 5 characters (zeros or ones)
      .map(Integer.parseInt(_, 2))    // parse integer from binary
      .map(bytesToChars(_))           // lookup encoded characters by index
      .foldLeft("")(_ + _)            // concatenate everything
  }

  def decode(plainText: String): Array[Byte] =  {
    plainText
      .map(charsToBytes(_))                                         // convert characters to bytes
      .map(_.toBinaryString.reverse.padTo(5, "0").reverse.mkString) // convert bytes to binary and pad each one to make a group of 5 chars (0s or 1s)
      .foldLeft("")(_ + _)                                          // concatenate everything
      .dropRight((plainText.length * 5) - (((plainText.length * 5) / 8) * 8)) // drop insignificant 0s from the end that was added by encoder to be a multiple of 5
      .grouped(8).toList                                            // group by 8 characters (bits) to form a byte
      .map(Integer.parseInt(_, 2))                                  // parse an integer
      .map(_.toByte)                                                // convert back to bytes
      .toArray                                                      // return as Array
  }
}
