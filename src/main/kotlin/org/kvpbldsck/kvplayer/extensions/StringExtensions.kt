package org.kvpbldsck.kvplayer.extensions

fun String.normalize(): String {
    return this
            .toLowerCase()
            .replace("\\s".toRegex()) {""}
}