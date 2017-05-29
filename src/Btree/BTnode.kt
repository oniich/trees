package BTree


class BTnode<K : Comparable<K>, V> {
    var keys = ArrayList<K>()
    var data = ArrayList<V>()
    var offspring = ArrayList<BTnode<K, V>>()

    fun isLeaf() = offspring.size == 0

    fun isEmpty() = keys.size == 0

}
