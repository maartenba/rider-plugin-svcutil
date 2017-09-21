package be.maartenballiauw.rider.svcutil.util


import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.util.io.exists
import org.apache.commons.lang.SystemUtils
import com.google.common.io.MoreFiles.listFiles
import com.intellij.lang.pratt.PathPattern.path
import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import com.intellij.lang.pratt.PathPattern.path
import com.intellij.util.io.isFile
import java.nio.file.*
import jdk.nashorn.internal.objects.NativeArray.forEach


class SvcUtilLocator {
    companion object {
        fun findSvcUtilExecutable() : Path? {
            if (SystemUtils.IS_OS_WINDOWS ) {
                val fallbackPaths = ArrayList<Path>()
                fallbackPaths.add(Paths.get(System.getenv("ProgramFiles"), "Microsoft SDKs", "Windows"))
                fallbackPaths.add(Paths.get(System.getenv("ProgramFiles(X86)"), "Microsoft SDKs", "Windows"))

                val files = ArrayList<Path>()
                fallbackPaths
                        .filter { it.exists() }
                        .forEach {
                            Files.walk(it)
                                    .filter { it.isFile() && it.fileName.toString().toLowerCase() == "svcutil.exe" }
                                    .forEach {
                                        files.add(it)
                                    }
                        }

                return files.lastOrNull()
            }

            return null
        }
    }
}