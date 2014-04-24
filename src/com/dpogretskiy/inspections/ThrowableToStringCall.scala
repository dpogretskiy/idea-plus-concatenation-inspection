package com.dpogretskiy.inspections

import com.intellij.codeInspection.{ProblemsHolder, BaseJavaLocalInspectionTool}
import org.jetbrains.annotations.NotNull
import com.intellij.openapi.diagnostic.Logger
import com.intellij.codeInsight.daemon.GroupNames
import com.intellij.psi._

class ThrowableToStringCall extends BaseJavaLocalInspectionTool {
  lazy val log = Logger.getInstance("#com.dpogretskiy.ExceptionToString")

  @NotNull
  override def getDisplayName = "Suspicious toString() on Exception instance"

  @NotNull
  override def getGroupDisplayName = GroupNames.BUGS_GROUP_NAME

  @NotNull
  override def getShortName = "ExceptionToString"

  @NotNull
  override def buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = new JavaElementVisitor {
    override def visitMethodCallExpression(exp: PsiMethodCallExpression) {
      super.visitMethodCallExpression(exp)

      lazy val instanceIsThrowable: Boolean = {
        val me = exp.getMethodExpression
        if (me != null) {
          val qe = me.getQualifierExpression
          if (qe != null) {
            superTypeOfExpressionIs(qe, classOf[java.lang.Throwable])
          } else false
        } else false
      }

      if (methodCalledIs(exp, "toString") && instanceIsThrowable)
        holder.registerProblem(exp, s"Trying to call toString() on java.lang.Throwable instance, its not the best way to log")
    }
  }

  override def isEnabledByDefault = true
}

