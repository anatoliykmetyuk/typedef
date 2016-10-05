package importimpl

object Main {

  def main(args: Array[String]): Unit = {
    println(g(1))
  }

  // @hello def bundle {
  //   val A, B = Length[A]
  // }


  @prove(Bundles, "f")
  def g[A, B](x: A)(implicit
                    i1: TypeComp.Aux[String, A]
                    , i2: TypeComp.Aux[A, B]): B = Bundles.f(x)
}

object Bundles {
  def f[A, B](x: A)(implicit
    i1: TypeComp.Aux[String, A]
  , i2: TypeComp.Aux[A, B]): B = i2.out
}

trait TypeComp[A] {
  type Out
  val out: Out
}

object TypeComp {
  type Aux[A0, Out0] = TypeComp[A0] {type Out = Out0}

  implicit def stringInst: TypeComp.Aux[String, Int] = new TypeComp[String] {
    type Out = Int
    val out = 100
  }

  implicit def intInst: TypeComp.Aux[Int, String] = new TypeComp[Int] {
    type Out = String
    val out = "I work"
  }
}