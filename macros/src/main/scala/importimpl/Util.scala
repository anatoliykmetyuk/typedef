package importimpl

import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

/**
  * Created by anatolii on 10/4/16.
  */


class Utils[C <: Context](ctx: C) {
  import ctx.universe._
}

trait DefOps {
  type C <: Context
  val ctx: C
  import ctx.universe._

  val t: DefDef
  lazy val q"$mods def $tname[..$tparams](...$paramss): $tpt = $expr" = t

  lazy val impl: List[     Tree]  = paramss.flatten.filter(_.mods.hasFlag(Flag.IMPLICIT))
  lazy val expl: List[List[Tree]] = paramss.takeWhile(_.forall(!impl.contains(_)))
  lazy val all : List[List[Tree]] = expl :+ impl

  def apply(): DefDef =
    q"$mods def $tname[..$tparams](...$all): $tpt = $expr"
}