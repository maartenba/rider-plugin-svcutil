package be.maartenballiauw.rider.svcutil.actions

import be.maartenballiauw.rider.svcutil.config.WcfToolingConfiguration
import be.maartenballiauw.rider.svcutil.dialogs.GenerateWcfServiceClientUI
import be.maartenballiauw.rider.svcutil.notifications.WCF_NOTIFICATION_GROUP
import be.maartenballiauw.rider.svcutil.util.ShellExec
import com.intellij.icons.AllIcons
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.VcsShowConfirmationOption
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.ConfirmationDialog
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.projectView.actions.ProjectViewActionBase
import com.jetbrains.rider.projectView.actions.addExisting.AddExistingFileAction
import com.jetbrains.rider.projectView.actions.newFile.RiderNewActionBase
import com.jetbrains.rider.projectView.nodes.ProjectModelNode
import com.jetbrains.rider.projectView.nodes.containingProject
import com.jetbrains.rider.projectView.nodes.isProject
import javax.swing.SwingUtilities

class GenerateWcfServiceClientAction : ProjectViewActionBase(
        "Generate WCF client...",
        "Generate WCF client...") {

    val APPCONFIG_FILENAME = "App.config"
    val SERVICECLIENT_PREFIX = "ServiceClient"
    val SERVICECLIENT_SUFFIX = ".cs"

    override fun getItemInternal(item: ProjectModelNode) = when {
        item.isProject() -> item
        else -> null
    }

    override fun updatePresentation(e: AnActionEvent, items: Array<ProjectModelNode>) {
        val projectItem = items.firstOrNull()
        e.presentation.isEnabled = projectItem != null
                && !((projectItem.containingProject()?.descriptor as? RdProjectDescriptor)?.isDotNetCore ?: false) // no .NET Core projects!
    }

    override fun actionPerformedInternal(item: ProjectModelNode, project: Project) {
        val application = ApplicationManager.getApplication()

        val util = WcfToolingConfiguration.instance.svcUtilPath
        if (!util.isEmpty()) {
            // Very, very naive way of getting current project namespace
            // TODO: make this better
            val serviceNamespace = (item.containingProject()?.descriptor as? RdProjectDescriptor)?.name!!

            // Generate a file name
            var serviceFileName = SERVICECLIENT_PREFIX + SERVICECLIENT_SUFFIX
            var i = 1
            while (item.getVirtualFile()!!.parent!!.children.any { it.name == serviceFileName }) {
                serviceFileName = SERVICECLIENT_PREFIX + (i++) + SERVICECLIENT_SUFFIX
            }

            // Show dialog
            val dialog = GenerateWcfServiceClientUI("", serviceNamespace, serviceFileName)
            dialog.show()

            if (dialog.isOK) {
                // Make sure the user is ok with overwriting existing files, if that is the case
                if (item.getVirtualFile()!!.parent!!.children.any { it.name == dialog.serviceFileName }) {
                    val confirmationDialog = ConfirmationDialog(project, "File '${dialog.serviceFileName}' already exists.\n\nDo you want to overwrite the existing file?", "Confirm overwrite", AllIcons.Webreferences.Server, VcsShowConfirmationOption.STATIC_SHOW_CONFIRMATION)
                    confirmationDialog.show()

                    if (!confirmationDialog.isOK) return
                }

                // Do we have an existing App.config/Web.config?
                val configFile = item.getVirtualFile()!!.parent!!.children
                        .firstOrNull { it.name.endsWith(".config") }

                // Build command arguments
                val args =  mutableListOf<String>()
                args.add("/t:code")
                args.add("/serializer:DataContractSerializer")
                args.add("/language:csharp")
                args.add("/namespace:*,${dialog.serviceNamespace}")
                args.add("/out:${dialog.serviceFileName}")
                if (configFile != null) {
                    args.add("/mergeConfig")
                    args.add("/config:${configFile.name}")
                } else {
                    args.add("/config:$APPCONFIG_FILENAME")
                }
                args.add(dialog.serviceUrl)

                // Working directory
                val workingDirectory = item.getVirtualFile()?.parent?.path!!.replace('/', '\\');

                // Run command
                val shellExecutor = ShellExec(true, true)
                shellExecutor.execute(util, workingDirectory, true, *args.toTypedArray())
                if (shellExecutor.exitCode == 0) {
                    // TODO: add reference to System.ServiceModel

                    // Add our newly generated proxy file
                    AddExistingFile(application, item, dialog.serviceFileName)

                    // And the config file, too
                    if (configFile == null) {
                        AddExistingFile(application, item, APPCONFIG_FILENAME)
                    }
                } else {
                    // Show in log
                    SwingUtilities.invokeLater {
                        val notification = WCF_NOTIFICATION_GROUP.createNotification(
                                "Generate WCF client",
                                "Failed to generate WCF client. See output for more details.",
                                "Error while executing command: " + util + " " + args.joinToString(" ") + "\r\n\r\n" + shellExecutor.output + "\r\n\r\n" + shellExecutor.error, NotificationType.ERROR)
                        Notifications.Bus.notify(notification , project)
                    }
                }
            }
        }
    }

    private fun AddExistingFile(application: Application, item: ProjectModelNode, fileName: String) {
        var file : VirtualFile? = null
        application.runWriteAction {
            file = item.getVirtualFile()!!.parent!!.findOrCreateChildData(this, fileName)
            AddExistingFileAction.execute(item, file!!)
        }

        application.invokeAndWait { RiderNewActionBase.postProcess(item.project, file!!) }
    }
}