package be.maartenballiauw.rider.svcutil.dialogs

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.IdeBorderFactory
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.jetbrains.rdclient.protocol.IPermittedModalities
import java.awt.Dimension
import javax.swing.*

class GenerateWcfServiceClientUI(var serviceUrl: String, var serviceNamespace: String, var serviceFileName: String)
    : DialogWrapper(true) {

    init {
        title = "Generate WCF client..."
        init()
        IPermittedModalities.getInstance().allowPumpProtocolForComponent(this.window, this.disposable)
    }

    override fun createCenterPanel(): JComponent? {
        // Build UI
        val rootPanel = JPanel().apply {
            layout = GridLayoutManager(4, 3).apply {
                preferredSize = Dimension(450, 350)
            }
            border = IdeBorderFactory.createEmptyBorder(3)
        }

        val serviceUrlField = JTextField()
        serviceUrlField.text = serviceUrl
        serviceUrlField.addPropertyChangeListener { serviceUrl = serviceUrlField.text }
        rootPanel.add(JLabel().apply {
            text = "Service URL:"
            border = IdeBorderFactory.createEmptyBorder(0, 0, 2, 2)
            labelFor = serviceUrlField },
                GridConstraints().apply {
                    row = 0
                    column = 0
                    vSizePolicy = 0
                    hSizePolicy = 0
                    anchor = GridConstraints.ANCHOR_NORTHWEST
                })

        rootPanel.add(serviceUrlField,
                GridConstraints().apply {
                    row = 0
                    column = 1
                    vSizePolicy = 0
                    hSizePolicy = 0
                    anchor = GridConstraints.ANCHOR_NORTHWEST
                    fill = GridConstraints.FILL_HORIZONTAL
                })

        val namespaceField = JTextField()
        namespaceField.text = serviceNamespace
        namespaceField.addPropertyChangeListener { serviceNamespace = namespaceField.text }
        rootPanel.add(JLabel().apply {
            text = "Namespace:"
            border = IdeBorderFactory.createEmptyBorder(0, 0, 2, 2)
            labelFor = namespaceField },
                GridConstraints().apply {
                    row = 1
                    column = 0
                    vSizePolicy = 0
                    hSizePolicy = 0
                    anchor = GridConstraints.ANCHOR_NORTHWEST
                })

        rootPanel.add(namespaceField,
                GridConstraints().apply {
                    row = 1
                    column = 1
                    vSizePolicy = 0
                    hSizePolicy = 0
                    anchor = GridConstraints.ANCHOR_NORTHWEST
                    fill = GridConstraints.FILL_HORIZONTAL
                })

        val fileNameField = JTextField()
        fileNameField.text = serviceFileName
        fileNameField.addPropertyChangeListener { serviceFileName = fileNameField.text }
        rootPanel.add(JLabel().apply {
            text = "File name:"
            border = IdeBorderFactory.createEmptyBorder(0, 0, 2, 2)
            labelFor = namespaceField },
                GridConstraints().apply {
                    row = 2
                    column = 0
                    vSizePolicy = 0
                    hSizePolicy = 0
                    anchor = GridConstraints.ANCHOR_NORTHWEST
                })

        rootPanel.add(fileNameField,
                GridConstraints().apply {
                    row = 2
                    column = 1
                    vSizePolicy = 0
                    hSizePolicy = 0
                    anchor = GridConstraints.ANCHOR_NORTHWEST
                    fill = GridConstraints.FILL_HORIZONTAL
                })

        rootPanel.add(JPanel(),
            GridConstraints().apply {
                row = 3
                column = 1
                fill = 3
                vSizePolicy = 3
                hSizePolicy = 3
                anchor = GridConstraints.ANCHOR_CENTER
            })

        return rootPanel
    }

    override fun createActions(): Array<Action> = arrayOf(okAction, cancelAction)

    override fun doOKAction() {
        // TODO: validation

        super.doOKAction()
    }

    override fun doCancelAction() {
        super.doCancelAction()
    }
}