package com.dpogretskiy

import com.intellij.psi._
import com.intellij.psi.tree.IElementType
import scala.annotation.tailrec

package object inspections {

  def typeOfExpressionIs[T](expr: PsiExpression, clazz: Class[T]): Boolean = {
    val `type` = expr.getType
    `type` != null && `type`.getCanonicalText == clazz.getCanonicalName
  }

  def superTypeOfExpressionIs[T](expr: PsiExpression, clazz: Class[T]): Boolean = {
    def rec(`type`: PsiType, found: Boolean): Boolean = {
      if (found) true
      else {
        val superTypes = `type`.getSuperTypes
        superTypes.exists(x => rec(x, superTypes.exists(_.getCanonicalText == clazz.getCanonicalName)))
      }
    }

    val `type` = expr.getType
    `type` != null && rec(`type`, typeOfExpressionIs(expr, clazz))
  }

  def typeOfExpressionIsPrimitive(expr: PsiExpression): Boolean = {
    val `type` = expr.getType
    `type` != null && `type`.isInstanceOf[PsiPrimitiveType]
  }

  def methodCalledIs(expr: PsiMethodCallExpression, name: String): Boolean = {
    expr.getMethodExpression.getLastChild match {
      case x: PsiIdentifier if x.getText == name =>
        true
      case _ => false
    }
  }

  def polyadicExpressionOperatorIs(expr: PsiPolyadicExpression, token: IElementType): Boolean = {
    expr.getOperationTokenType == token
  }

  def getPresentableTypeName(expr: PsiExpression): String = {
    val `type` = expr.getType
    if (`type` == null) "null"
    else `type`.getPresentableText
  }
}
