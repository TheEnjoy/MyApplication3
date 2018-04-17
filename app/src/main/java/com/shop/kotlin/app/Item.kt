package com.shop.kotlin.app

class Item {

    var id: Int? = null
    var title: String? = null
    var content: String? = null
    var cost: Int? = null
    var qvc: Int? = null

    constructor(id: Int, title: String, content: String, cost: Int, qvc: Int) {
        this.id = id
        this.title = title
        this.content = content
        this.cost = cost
        this.qvc = qvc

    }

}
