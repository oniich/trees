package BinarySearchTree

//Класс узла дерева со ссылками на родителя и двух потомков и с ключом и значением
class BSTnode<K: Comparable<K>, V>(var key: K, var value: V, var parent: BSTnode<K, V>? = null) {
    var left: BSTnode<K, V>? = null
    var right: BSTnode<K, V>? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as BSTnode<*, *>

        if (key != other.key) return false
        if (value != other.value) return false
        if (parent != other.parent) return false
        if (left != other.left) return false
        if (right != other.right) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (parent?.hashCode() ?: 0)
        result = 31 * result + (left?.hashCode() ?: 0)
        result = 31 * result + (right?.hashCode() ?: 0)
        return result
    }
 }
