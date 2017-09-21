package be.maartenballiauw.rider.svcutil.config

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.IdeBorderFactory
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.util.ui.UIUtil
import java.awt.Cursor
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent

class WcfToolingConfigurableUI(private val config: WcfToolingConfiguration) {
    private val rootPanel: JPanel = JPanel().apply {
        layout = GridLayoutManager(3, 3)
        border = IdeBorderFactory.createEmptyBorder(3)
    }

    private val svcUtilExecutableField: TextFieldWithBrowseButton = TextFieldWithBrowseButton()

    fun getPanel(): JComponent {
        return rootPanel
    }

    val isModified: Boolean
        get() {
            var modified = false
            modified = modified or (svcUtilExecutableField.text != config.svcUtilPath)
            return modified
        }

    fun apply() {
        config.svcUtilPath = svcUtilExecutableField.text
    }

    fun reset() {
        svcUtilExecutableField.text = config.svcUtilPath
    }

    init {
        // Build UI
        rootPanel.add(JLabel().apply {
            text = "Path to svcutil.exe:"
            border = IdeBorderFactory.createEmptyBorder(0, 0, 2, 2)
            labelFor = svcUtilExecutableField },
                GridConstraints().apply {
                    row = 0
                    column = 0
                    vSizePolicy = 0
                    hSizePolicy = 0
                    anchor = GridConstraints.ANCHOR_WEST
                })

        rootPanel.add(svcUtilExecutableField.apply {
            isEditable = true
            isEnabled = true
            addActionListener({
                val descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("exe")
                        .withFileFilter({ it != null && it.name.toLowerCase() == "svcutil.exe" })

                descriptor.title = "Browse for svcutil.exe..."
                val file = FileChooser.chooseFile(descriptor,null, LocalFileSystem.getInstance().findFileByPath(svcUtilExecutableField.text))
                if (file != null) {
                    svcUtilExecutableField.text = file.path.replace('/', '\\')
                }
            })},
                GridConstraints().apply {
                    row = 0
                    column = 1
                    fill = GridConstraints.FILL_HORIZONTAL
                    vSizePolicy = 0
                    hSizePolicy = 6
                    anchor = GridConstraints.ANCHOR_NORTHWEST
                })

        rootPanel.add(JTextPane().apply {
            editorKit = UIUtil.getHTMLEditorKit()
            isEditable = false
            isEnabled = true
            text = "<html><body>Make sure to install a Windows SDK that contains <code>svcutil.exe</code>.<br />More information is available <a href=\"https://docs.microsoft.com/en-us/dotnet/framework/wcf/servicemodel-metadata-utility-tool-svcutil-exe\">here</a>.</body></html>"
            background = UIUtil.TRANSPARENT_COLOR
            cursor = Cursor(Cursor.HAND_CURSOR)
            addHyperlinkListener {
                e -> run {
                    if (e.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        BrowserUtil.browse(e.url)
                    }
                }
            }
        },
                GridConstraints().apply {
                    row = 1
                    column = 1
                    fill = GridConstraints.FILL_HORIZONTAL
                    vSizePolicy = 0
                    hSizePolicy = 6
                    anchor = GridConstraints.ANCHOR_NORTHWEST
                })

        rootPanel.add(JPanel(),
                GridConstraints().apply {
                    row = 2
                    column = 1
                    fill = 3
                    vSizePolicy = 3
                    hSizePolicy = 3
                    anchor = GridConstraints.ANCHOR_CENTER
                })
    }
}