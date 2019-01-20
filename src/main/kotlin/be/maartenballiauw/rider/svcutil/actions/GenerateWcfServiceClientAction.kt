package be.maartenballiauw.rider.svcutil.actions

import be.maartenballiauw.rider.svcutil.notifications.WCF_NOTIFICATION_GROUP
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.projectView.actions.ProjectViewActionBase
import com.jetbrains.rider.projectView.nodes.ProjectModelNode
import com.jetbrains.rider.projectView.nodes.containingProject
import com.jetbrains.rider.projectView.nodes.isProject
import javax.swing.SwingUtilities

class GenerateWcfServiceClientAction : ProjectViewActionBase(
        "Generate WCF client...",
        "Generate WCF client...") {

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

        SwingUtilities.invokeLater {
            val notification = WCF_NOTIFICATION_GROUP.createNotification(
                    "Deprecated - Generate WCF client",
                    null,
                    "Use Add Web Reference... action instead.", NotificationType.ERROR)
            Notifications.Bus.notify(notification , project)
        }
    }
}