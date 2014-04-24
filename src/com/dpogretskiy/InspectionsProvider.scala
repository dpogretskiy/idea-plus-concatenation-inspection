package com.dpogretskiy

import com.intellij.codeInspection.InspectionToolProvider
import com.dpogretskiy.inspections._

class InspectionsProvider extends InspectionToolProvider {
  def getInspectionClasses: Array[Class[_]] = {
    Array(classOf[ImplicitToStringConversion], classOf[ThrowableToStringCall], classOf[FileLengthCall])
  }
}