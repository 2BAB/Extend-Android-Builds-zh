package me.xx2bab.extandagp.aapt2

import com.didiglobal.booster.aapt2.Aapt2Container
import com.didiglobal.booster.aapt2.BinaryParser
import com.didiglobal.booster.aapt2.metadata
import com.didiglobal.booster.aapt2.parseAapt2Container
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.io.File
import java.net.URL

class BoostAaptFlatTest {

    @Test
    fun `Extract metadata from mipmap-xxxhdpi_ic_launcher successfully`() {
        val metadata = javaClass.classLoader
            .getResource("mipmap-xxxhdpi_ic_launcher.png.flat")
            .let(URL::getFile)
            .let(::File)
            .metadata
        println(metadata.resourceName)
        assertThat(metadata.resourceName, `is`("mipmap/ic_launcher"))
        println(metadata.resourcePath)
        assertThat(metadata.resourcePath, `is`("mipmap-xxxhdpi/ic_launcher.png"))
        println(metadata.sourcePath)
        assertThat(metadata.sourcePath,
            `is`("./src/main/res/mipmap-xxxhdpi/ic_launcher.png"))
        println(metadata.configuration.screenType.density)
        assertThat(metadata.configuration.screenType.density, `is`(640))
    }

    @Test
    fun `Extract viewGroup from layout_activity_main successfully`() {
        val valueStringFile = javaClass.classLoader
            .getResource("layout_activity_main.xml.flat")
            .let(URL::getFile)
            .let(::File)
        val container = BinaryParser(valueStringFile).use { parser ->
            parser.parseAapt2Container()
        }
        val viewGroup = (container.entries[0] as Aapt2Container.Xml)
            .root.element.name
        println(viewGroup)
        assertThat(viewGroup, `is`("RelativeLayout"))
    }

}


