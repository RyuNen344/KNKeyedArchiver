//
//  Book.swift
//  iosApp
//
//  Created by RyuNen344 on 2023/03/16.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

class Book: NSObject, NSCoding {
  
    var ID: String!
    var author: String!
    
    init(ID: String, author: String) {
        self.ID = ID
        self.author = author
    }
    
    func encode(with coder: NSCoder) {
        ID = coder.decodeObject(forKey: "id") as? String
        author = coder.decodeObject(forKey: "author") as? String
    }
    
    required init?(coder: NSCoder) {
        coder.encode(ID, forKey: "id")
        coder.encode(author, forKey: "author")
    }
}


func archiveBook(from book: Book) -> Data? {
    do {
        NSKeyedArchiver.setClassName("Book", for: Book.classForKeyedArchiver()!)
        return try NSKeyedArchiver.archivedData(withRootObject: book, requiringSecureCoding: false)
    } catch {
        print(error)
        return nil
    }
}

func unarchiveBook(from data: Data) -> Book? {
    do {
        NSKeyedUnarchiver.setClass(Book.classForKeyedUnarchiver(), forClassName: "Book")
        return try NSKeyedUnarchiver
            .unarchivedObject(ofClasses: [Book.self, NSString.self], from: data) as? Book
    } catch {
        print(error)
        return nil
    }
}
