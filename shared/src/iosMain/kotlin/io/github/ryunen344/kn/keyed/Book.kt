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
import platform.Foundation.NSKeyedArchiver
import platform.Foundation.NSKeyedUnarchiver
import platform.Foundation.NSString
import platform.Foundation.classForKeyedArchiver
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

fun archiveBook(book: Book): NSData? {
    return memScoped {
        val archiveError = alloc<ObjCObjectVar<NSError?>>()
        NSKeyedArchiver.setClassName(
            codedName = "Book",
            forClass = book.classForKeyedArchiver()!!
        )
        val archived = NSKeyedArchiver.archivedDataWithRootObject(
            `object` = book,
            requiringSecureCoding = false,
            error = archiveError.ptr
        )
        archiveError.value?.let {
            // NSErrorがnon nullのときはエラーが発生してる
            error(it.localizedDescription)
        }
        archived
    }
}

fun unarchiveBook(data: NSData): Book? {
    return memScoped {
        val unarchiveError = alloc<ObjCObjectVar<NSError?>>()
        NSKeyedUnarchiver.setClass(
            cls = Book.classForKeyedUnarchiver(),
            forClassName = "Book"
        )
        val unarchived = NSKeyedUnarchiver.unarchivedObjectOfClasses(
            classes = setOf(Book, NSString),
            fromData = data,
            error = unarchiveError.ptr
        ) as? Book
        unarchiveError.value?.let {
            // NSErrorがnon nullのときはエラーが発生してる
            error(it.localizedDescription)
        }
        unarchived
    }
}
