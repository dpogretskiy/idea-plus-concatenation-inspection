package com.dpogretskiy

import com.intellij.codeInspection.{ProblemsHolder, BaseJavaLocalInspectionTool}
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.annotations.NotNull
import com.intellij.codeInsight.daemon.GroupNames
import com.intellij.psi._
import scala.collection.convert.WrapAsScala

class ImplicitToStringInspection extends BaseJavaLocalInspectionTool {
  lazy val log = Logger.getInstance("#com.dpogretskiy.ImplicitToStringInspection")

  @NotNull
  override def getDisplayName = "Implicit toString conversion"

  @NotNull
  override def getGroupDisplayName = GroupNames.BUGS_GROUP_NAME

  @NotNull
  override def getShortName = "Implicit toString inspection"

  @NotNull
  override def buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = new JavaElementVisitor {
    override def visitPolyadicExpression(exp: PsiPolyadicExpression) {
      super.visitPolyadicExpression(exp)
      if (exp.getOperationTokenType == JavaTokenType.PLUS && exp.getOperands.exists(isString) && exp.getOperands.exists(x => !isString(x))) {
        val suspiciousExpressions = exp.getOperands.filterNot(isString)
        holder.registerProblem(exp, s"Implicit toString conversion of ${
          suspiciousExpressions.map(x => Option(x.getType)
            .map(_.getPresentableText).getOrElse("null")).mkString(", ")
        }")
      }
    }

    /*override def visitBinaryExpression(exp: PsiBinaryExpression) {
      super.visitBinaryExpression(exp)
      if (exp.getOperationTokenType == JavaTokenType.PLUS) {
        val l = exp.getLOperand
        val r = exp.getROperand
        if (r != null && (isString(l) || isString(r)) && (!isString(l) || !isString(r)))
          holder.registerProblem(exp, s"Implicit toString conversion of ${
            if (!isString(l)) Option(l.getType).map(_.getCanonicalText).getOrElse("null")
            else Option(r.getType).map(_.getCanonicalText).getOrElse("null")
          }")
      }
    }*/

  }

  override def isEnabledByDefault = true

  private def isString(expr: PsiExpression) = {
    val `type` = expr.getType
    val ct = Option(`type`).map(_.getCanonicalText)
    ct.exists(_ == "java.lang.String")
  }
}
