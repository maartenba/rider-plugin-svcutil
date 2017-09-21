package be.maartenballiauw.rider.svcutil.config

import be.maartenballiauw.rider.svcutil.util.SvcUtilLocator
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "WcfToolingConfiguration", storages = arrayOf(Storage("WcfToolingConfiguration.xml")))
class WcfToolingConfiguration internal constructor() : PersistentStateComponent<WcfToolingConfiguration> {
    private var _svcUtilPath: String = ""
    var svcUtilPath: String
        get() {
            if (_svcUtilPath == "") {
                val path  = SvcUtilLocator.findSvcUtilExecutable()
                if (path != null) {
                    _svcUtilPath = path.toString()
                }
            }

            return _svcUtilPath
        }
        set(value) {
            _svcUtilPath = value
        }

    override fun getState(): WcfToolingConfiguration? {
        return this
    }

    override fun loadState(wcfToolingConfiguration: WcfToolingConfiguration) {
        XmlSerializerUtil.copyBean(wcfToolingConfiguration, this)
    }

    companion object {
        val instance: WcfToolingConfiguration
            get() = ServiceManager.getService(WcfToolingConfiguration::class.java)
    }
}