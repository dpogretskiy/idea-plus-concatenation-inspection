package com

import com.intellij.psi.PsiType

package object dpogretskiy {
  implicit class PsiTypeOps(val pt: PsiType) extends AnyVal {
    def isStringType = pt.equalsToText("java.lang.String")
  }
}
