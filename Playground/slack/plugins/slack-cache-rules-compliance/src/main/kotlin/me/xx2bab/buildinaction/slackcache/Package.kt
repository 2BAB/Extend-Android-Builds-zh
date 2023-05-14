package me.xx2bab.buildinaction.slackcache

import org.gradle.api.provider.Property

interface Package {
    
    val id: Property<String>
    
}
