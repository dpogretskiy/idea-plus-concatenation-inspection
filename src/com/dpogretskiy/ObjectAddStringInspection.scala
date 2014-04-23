package com.dpogretskiy

import com.intellij.codeInspection.{ProblemsHolder, BaseJavaLocalInspectionTool}
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.annotations.NotNull
import com.intellij.codeInsight.daemon.GroupNames
import com.intellij.psi._

class ObjectAddStringInspection extends BaseJavaLocalInspectionTool {
  lazy val log = Logger.getInstance("#com.dpogretskiy.github.ObjectAndStringConcat")

  @NotNull
  override def getDisplayName = "Object + String concatenation"

  @NotNull
  override def getGroupDisplayName = GroupNames.BUGS_GROUP_NAME

  @NotNull
  override def getShortName = "ObjectAddStringConcat"

  @NotNull
  override def buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = new JavaElementVisitor {
    override def visitReferenceExpression(exp: PsiReferenceExpression) {}

    override def visitBinaryExpression(exp: PsiBinaryExpression) {
      super.visitBinaryExpression(exp)
      if (exp.getOperationTokenType == JavaTokenType.PLUS) {
        val l = exp.getLOperand
        val r = exp.getROperand

        if (r != null && (isString(l) || isString(r)) && (!isString(l) || !isString(r)))
          holder.registerProblem(exp, s"Trying to add ${Option(l.getType).map(_.getPresentableText).getOrElse("null")} and ${
            Option(r.getType).map(_.getPresentableText).getOrElse("null")}")
      }
    }
  }

  override def isEnabledByDefault = true

  private def isString(expr: PsiExpression) = {
    val `type` = expr.getType
    val ct = Option(`type`).map(_.getCanonicalText)
    ct.exists(_ == "java.lang.String")
  }
}
