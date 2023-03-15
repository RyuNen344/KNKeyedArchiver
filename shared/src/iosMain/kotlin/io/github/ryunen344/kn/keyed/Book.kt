package io.github.ryunen344.kn.keyed

import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSCoder
import platform.Foundation.NSCodingProtocol
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSKeyedUnarchiver
import platform.Foundation.NSString
import platform.Foundation.classForKeyedUnarchiver
import platform.Foundation.decodeObjectForKey
import platform.Foundation.encodeObject
import platform.darwin.NSObject
import platform.darwin.NSObjectMeta

class Book(
    val id: String,
    val author: String
) : NSObject(), NSCodingProtocol {
    override fun encodeWithCoder(coder: NSCoder) {
        coder.encodeObject(id.toNSString(), "id")
        coder.encodeObject(author.toNSString(), "id")
    }

    override fun initWithCoder(coder: NSCoder): NSCodingProtocol? {
        val id = coder.decodeObjectForKey("id") as? String
        val author = coder.decodeObjectForKey("author") as? String
        return if (id != null && author != null) Book(id, author) else null
    }

    companion object : NSObjectMeta()
}

fun unarchiveBook(data: NSData): Book? {
    return memScoped {
        val unarchiveError = alloc<ObjCObjectVar<NSError?>>()
        NSKeyedUnarchiver.setClass(
            Book.classForKeyedUnarchiver(),
            "Book"
        )
        val unarchived = NSKeyedUnarchiver.unarchivedObjectOfClasses(
            setOf(Book, NSString),
            data,
            unarchiveError.ptr
        ) as? Book
        unarchiveError.value?.let {
            // NSErrorがnon nullのときはエラーが発生してる
            error(it.localizedDescription)
        }
        unarchived
    }
}
