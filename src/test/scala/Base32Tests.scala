import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

class Base32Tests extends FunSuite with GeneratorDrivenPropertyChecks {

  implicit override val generatorDrivenConfig =
    PropertyCheckConfiguration(minSuccessful = 100)

  val unicodeString: Gen[String] = Arbitrary.arbString.arbitrary

  test("Decoding should produce the original byte sequence") {
    forAll (unicodeString) { (str:String) =>
      val inputBytes = str.getBytes
      val encoded = Base32.encode(inputBytes)
      val decoded = Base32.decode(encoded)
      assert(str == new String(decoded))
    }
  }
}
