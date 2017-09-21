package be.maartenballiauw.rider.svcutil.config

import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SearchableConfigurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class WcfToolingConfigurable : SearchableConfigurable {
    private var ui: WcfToolingConfigurableUI? = null
    private val config: WcfToolingConfiguration = WcfToolingConfiguration.instance

    @Nls
    override fun getDisplayName(): String {
        return "WCF Tooling"
    }

    override fun getHelpTopic(): String? {
        return "preference.WcfToolingConfigurable"
    }

    override fun getId(): String {
        return "preference.WcfToolingConfigurable"
    }

    override fun enableSearch(query: String?): Runnable? {
        return null
    }

    override fun createComponent(): JComponent? {
        ui = WcfToolingConfigurableUI(config)
        return ui!!.getPanel()
    }

    override fun isModified(): Boolean {
        return ui!!.isModified
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        ui!!.apply()
    }

    override fun reset() {
        ui!!.reset()
    }

    override fun disposeUIResources() {
        ui = null
    }
}