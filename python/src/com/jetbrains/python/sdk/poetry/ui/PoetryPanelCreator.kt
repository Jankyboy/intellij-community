// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.python.sdk.poetry.ui

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.util.UserDataHolder
import com.intellij.util.PlatformUtils
import com.jetbrains.python.PyBundle
import com.jetbrains.python.sdk.add.PyAddSdkGroupPanel
import com.jetbrains.python.sdk.add.PyAddSdkPanel
import com.jetbrains.python.sdk.isAssociatedWithModule
import com.jetbrains.python.sdk.poetry.POETRY_ICON
import com.jetbrains.python.sdk.poetry.detectPoetryEnvs
import com.jetbrains.python.sdk.poetry.sdkHomes
import com.jetbrains.python.ui.pyModalBlocking
import java.util.function.Supplier

fun createPoetryPanel(
  project: Project,
  module: Module?,
  existingSdks: List<Sdk>,
  newProjectPath: String?,
  context: UserDataHolder,
): PyAddSdkPanel {
  val newPoetryPanel = when {
    allowCreatingNewEnvironments(project) -> PyAddNewPoetryPanel(project, module, existingSdks, null, context)
    else -> null
  }
  val existingPoetryPanel = PyAddExistingPoetryEnvPanel(project, module, existingSdks, null)
  val panels = listOfNotNull(newPoetryPanel, existingPoetryPanel)
  val existingSdkPaths = sdkHomes(existingSdks)
  val defaultPanel = when {
    pyModalBlocking {
      detectPoetryEnvs(module, existingSdkPaths, project.basePath ?: newProjectPath)
    }.any { it.isAssociatedWithModule(module) } -> existingPoetryPanel
    newPoetryPanel != null -> newPoetryPanel
    else -> existingPoetryPanel
  }

  return PyAddSdkGroupPanel(
    nameGetter = Supplier { PyBundle.message("python.add.sdk.panel.name.poetry.environment") },
    panelIcon = POETRY_ICON,
    panels = panels,
    defaultPanel = defaultPanel
  )
}

private fun allowCreatingNewEnvironments(project: Project?) =
  project != null || !PlatformUtils.isPyCharm() || PlatformUtils.isPyCharmEducational()