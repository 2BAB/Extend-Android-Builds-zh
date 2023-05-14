package me.xx2bab.buildinaction.slacktest.model

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject


abstract class DefaultConfig @Inject constructor(
    objectFactory: ObjectFactory
) {

    abstract val message: Property<String>

    val pkg: Package = objectFactory.newInstance(Package::class.java)

    fun pkg(action: Action<Package>) {
        action.execute(pkg)
    }

}