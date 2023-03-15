package io.github.ryunen344.kn.keyed

import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding

@Suppress("CAST_NEVER_SUCCEEDS")
inline fun String.toNSString() = this as NSString

inline fun String.toNSData() = toNSString().toNSData()

inline fun NSString.toNSData() = dataUsingEncoding(NSUTF8StringEncoding)
