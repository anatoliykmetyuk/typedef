package importimpl

import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

object proveMacro {

  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    import Flag._

    val prefix = c.prefix.tree
    val inputs = annottees.map(_.tree).toList
    // println(inputs)
    // println(prefix)

    val (obj: c.Tree, name: String) = prefix match {
      case q"new $_($obj, $name)" => (obj, c.eval(c.Expr[String](name)))
      case _ => EmptyTree
    }

    println(showRaw(obj) + " ;;; " + name)

    val checked = c.typecheck(obj)
    println(showRaw(checked))

    val sym = checked.tpe.member(TermName(name)).asMethod
    println(sym.info)

    val result = inputs.head match {
      case tr @ q"$mods def $tname[..$tparams](...$paramss): $tpt = $expr" =>
        val bldr = new DefOps {
          override type C = c.type
          val ctx: C = c
          val t   = tr.asInstanceOf[DefDef]
        }

        sym.typeParams(3).typeSignature

//        val stp = symTypeParams(sym)
//        val symInpl = symImplicitParams(sym)

        bldr()
    }

    println(result)
    c.Expr[Any](result)
    // http://www.scala-lang.org/api/2.11.8/scala-reflect/index.html#scala.reflect.api.Symbols$MethodSymbol@inheritance-diagram
    // val result = annottees.map(_.tree).toList match {
    //   case q"object $name extends ..$parents { ..$body }" :: Nil =>
    //     q"""
    //       object $name extends ..$parents {
    //         def hello: ${typeOf[String]} = "hello"
    //         ..$body
    //       }
    //     """
    // }
//    c.Expr[Any](EmptyTree)
  }
}

class prove extends StaticAnnotation {
  def macroTransform(annottees: Any*) = macro proveMacro.impl
}
