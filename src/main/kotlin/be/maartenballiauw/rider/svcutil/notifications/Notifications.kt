package be.maartenballiauw.rider.svcutil.notifications

import com.intellij.icons.AllIcons
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup


val WCF_NOTIFICATION_GROUP = NotificationGroup(
        "Windows Communication Foundation integration",
        NotificationDisplayType.TOOL_WINDOW,
        true,
        null,
        AllIcons.Webreferences.Server)