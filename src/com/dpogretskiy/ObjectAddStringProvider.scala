package com.dpogretskiy

import com.intellij.codeInspection.InspectionToolProvider

class ObjectAddStringProvider extends InspectionToolProvider {
  def getInspectionClasses: Array[Class[_]] = {
    Array[Class[_]](classOf[ObjectAddStringInspection])
  }
}