package com.dpogretskiy

import com.intellij.codeInspection.InspectionToolProvider

class InspectionsProvider extends InspectionToolProvider {
  def getInspectionClasses: Array[Class[_]] = {
    Array(classOf[ImplicitToStringInspection], classOf[FileLengthCall])
  }
}