package com.dpogretskiy

import com.intellij.codeInspection.{ProblemsHolder, BaseJavaLocalInspectionTool}
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.annotations.NotNull
import com.intellij.codeInsight.daemon.GroupNames
import com.intellij.psi._

class FileLengthCall extends BaseJavaLocalInspectionTool {
  lazy val log = Logger.getInstance("#com.dpogretskiy.FileLengthCall")

  @NotNull
  override def getDisplayName = "Suspicious length() on File instance"

  @NotNull
  override def getGroupDisplayName = GroupNames.BUGS_GROUP_NAME

  @NotNull
  override def getShortName = "CallToFileLength"

  @NotNull
  override def buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = new JavaElementVisitor {
    override def visitMethodCallExpression(exp: PsiMethodCallExpression) {
      super.visitMethodCallExpression(exp)

      val instanceIsFile: Boolean = {
        val me = exp.getMethodExpression
        if (me != null) {
          val qe = me.getQualifierExpression
          if (qe != null) {
            val `type` = qe.getType
            if (`type` != null) {
              `type`.getCanonicalText == "java.io.File"
            } else false
          } else false
        } else false
      }

      val method = exp.getMethodExpression.getLastChild
      method match {
        case x: PsiIdentifier if instanceIsFile && x.getText == "length" =>
          holder.registerProblem(exp, s"Trying to call length() on java.lang.File instance")
        case _ =>
      }
    }
  }

  override def isEnabledByDefault = true
}
