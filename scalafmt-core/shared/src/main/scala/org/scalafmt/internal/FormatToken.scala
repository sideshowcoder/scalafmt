package org.scalafmt.internal

import scala.meta.tokens.Token

import org.scalafmt.util.TokenOps._

/**
  * Two adjacent non-whitespace tokens.
  *
  * Consider a FormatToken as a node in a search graph and [[Split]]
  * are the edges. The format tokens remain unchanged after formatting,
  * while the splits are changed.
  *
  * @param left The left non-whitespace token.
  * @param right The right non-whitespace token.
  * @param meta Extra information about the token
  */
case class FormatToken(left: Token, right: Token, meta: FormatToken.Meta) {

  override def toString = s"${left.syntax}∙${right.syntax}"

  def inside(range: Set[Range]): Boolean = {
    if (range.isEmpty) true
    else range.exists(_.contains(right.pos.endLine))
  }

  def between = meta.between
  lazy val newlinesBetween: Int = meta.between.count(_.is[Token.LF])

  val leftHasNewline = left.syntax.contains('\n')

  /**
    * A format token is uniquely identified by its left token.
    */
  override def hashCode(): Int = hash(left).##
}

object FormatToken {

  /**
    * @param between The whitespace tokens between left and right.
    * @param idx The token's index in the FormatTokens array
    */
  case class Meta(
      between: Array[Token],
      idx: Int
  )

}
