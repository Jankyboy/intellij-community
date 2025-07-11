// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.python.packaging.conda

import com.jetbrains.python.PyBundle
import com.jetbrains.python.packaging.common.PythonPackageDetails
import com.jetbrains.python.packaging.management.PythonPackageManager
import com.jetbrains.python.packaging.toolwindow.actions.PythonPackageInstallAction
import com.jetbrains.python.packaging.toolwindow.actions.PythonPackagingToolwindowActionProvider
import com.jetbrains.python.packaging.toolwindow.actions.SimplePythonPackageInstallAction
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
class CondaPackagingToolwindowActionProvider : PythonPackagingToolwindowActionProvider {
  override fun getInstallActions(details: PythonPackageDetails, packageManager: PythonPackageManager): List<PythonPackageInstallAction>? {
    if (packageManager !is CondaPackageManager) {
      return null
    }

    return if (details is CondaPackageDetails) {
      listOf(SimplePythonPackageInstallAction(PyBundle.message("conda.packaging.button.install.with.conda"), packageManager.project))
    }
    else
      listOf(SimplePythonPackageInstallAction(PyBundle.message("conda.packaging.button.install.with.pip"), packageManager.project))
  }
}