package me.xx2bab.buildinaction.slackblocks.internal

// When the extension become complex you may need to use the isolation mode,
// to separate the interface and actual implementation.
// See how it gets registered to project in SlackNotificationPlugin to wire these 2 portions together.
// You can also see this design in Android Gradle Plugin.

// Put this interface to outer package as it is not internal
// if you want to use it in real project.

// interface SlackNotificationExtension {
//
//    val enabled: Property<Boolean>
//
//    val channels: NamedDomainObjectContainer<SlackChannel>
//
//    val defaultConfig: DefaultConfig
//
//    fun defaultConfig(action: Action<DefaultConfig>)
//}

//abstract class SlackNotificationExtensionImpl @Inject constructor(
//    objects: ObjectFactory
//) : SlackNotificationExtension {
//
//    override val channels: NamedDomainObjectContainer<SlackChannel> = objects.domainObjectContainer(SlackChannel::class.java)
//    override val enabled: Property<Boolean> = objects.property(Boolean::class.java).convention(true)
//    override val defaultConfig: DefaultConfig = objects.newInstance(DefaultConfig::class.java)
//
//    override fun defaultConfig(action: Action<DefaultConfig>) {
//        action.execute(defaultConfig)
//    }
//}