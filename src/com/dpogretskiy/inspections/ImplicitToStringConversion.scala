package com.dpogretskiy.inspections

import com.intellij.codeInspection.{ProblemsHolder, BaseJavaLocalInspectionTool}
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.annotations.NotNull
import com.intellij.codeInsight.daemon.GroupNames
import com.intellij.psi._

class ImplicitToStringConversion extends BaseJavaLocalInspectionTool {
  lazy val log = Logger.getInstance("#com.dpogretskiy.ImplicitToStringInspection")

  @NotNull
  override def getDisplayName = "Implicit toString conversion"

  @NotNull
  override def getGroupDisplayName = GroupNames.BUGS_GROUP_NAME

  @NotNull
  override def getShortName = "ImplicitToString"

  @NotNull
  override def buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = new JavaElementVisitor {
    override def visitPolyadicExpression(expr: PsiPolyadicExpression) {
      super.visitPolyadicExpression(expr)
      if (polyadicExpressionOperatorIs(expr, JavaTokenType.PLUS)
        && expr.getOperands.exists(isStringOrPrimitive)
        && expr.getOperands.exists(x => !isStringOrPrimitive(x))) {

        val suspiciousExpressions = expr.getOperands.filter(x => !isStringOrPrimitive(x))
        holder.registerProblem(expr, s"Implicit toString conversion of ${
          suspiciousExpressions.map(getPresentableTypeName).mkString(", ")
        }")
      }
    }
  }

  override def isEnabledByDefault = true

  private def isStringOrPrimitive(expr: PsiExpression) = isString(expr) || typeOfExpressionIsPrimitive(expr)
  private def isString(expr: PsiExpression) = {
    typeOfExpressionIs(expr, classOf[java.lang.String])
  }
}
