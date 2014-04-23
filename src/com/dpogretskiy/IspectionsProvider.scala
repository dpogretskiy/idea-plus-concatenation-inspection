package com.dpogretskiy

import com.intellij.codeInspection.InspectionToolProvider

class IspectionsProvider extends InspectionToolProvider {
  def getInspectionClasses: Array[Class[_]] = {
    Array[Class[_]](classOf[FileLengthCall], classOf[FileLengthCall])
  }
}