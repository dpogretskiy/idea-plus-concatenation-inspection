package com.dpogretskiy.inspections

import com.intellij.codeInspection.{ProblemsHolder, BaseJavaLocalInspectionTool}
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.annotations.NotNull
import com.intellij.codeInsight.daemon.GroupNames
import com.intellij.psi._

class FileLengthCall extends BaseJavaLocalInspectionTool {
  lazy val log = Logger.getInstance("#com.dpogretskiy.ExceptionToString")

  @NotNull
  override def getDisplayName = "Suspicious length() on File instance"

  @NotNull
  override def getGroupDisplayName = GroupNames.BUGS_GROUP_NAME

  @NotNull
  override def getShortName = "CallToFileLength"

  @NotNull
  override def buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = new JavaElementVisitor {
    override def visitMethodCallExpression(expr: PsiMethodCallExpression) {
      super.visitMethodCallExpression(expr)

      lazy val instanceIsFile: Boolean = {
        val me = expr.getMethodExpression
        if (me != null) {
          val qe = me.getQualifierExpression
          if (qe != null) {
            typeOfExpressionIs(qe, classOf[java.io.File])
          } else false
        } else false
      }

      if (methodCalledIs(expr, "length") && instanceIsFile)
        holder.registerProblem(expr, s"Trying to call length() on java.lang.File instance")
    }
  }

  override def isEnabledByDefault = true
}
