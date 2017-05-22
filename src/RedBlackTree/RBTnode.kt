package RedBlackTree

enum class Colour {red, black}

class RBTnode<K: Comparable<K>, V>(var key: K, var value: V, var parent: RBTnode<K, V>? = null, var col: Colour = Colour.red) {
    var left: RBTnode<K, V>? = null

    var right: RBTnode<K, V>? = null

    fun isLeaf(): Boolean {
        return (this.left == null) && (this.right == null)
    }

    fun brother(): RBTnode<K, V>? {
        if (this == this.parent?.left)
            return this.parent!!.right

        return this.parent?.left
    }

    fun rotateLeft() {
        val rightChild = this.right ?: return
        val dad = this.parent

        this.swapColors(rightChild)

        rightChild.left?.parent = this
        this.right = rightChild.left

        rightChild.left = this

        when {
            this == dad?.left -> dad.left = rightChild
            this == dad?.right -> dad.right = rightChild
        }

        this.parent = rightChild
        rightChild.parent = dad
    }

    fun rotateRight() {
        val leftChild = this.left ?: return
        val dad = this.parent

        this.swapColors(leftChild)

        leftChild.right?.parent = this
        this.left = leftChild.right

        leftChild.right = this

        when {
            this == dad?.left -> dad.left = leftChild
            this == dad?.right -> dad.right = leftChild
        }

        this.parent = leftChild
        leftChild.parent = dad
    }

    private fun swapColors(node2: RBTnode<K, V>?) {
        val buf = this.col

        if (node2 != null) {
            this.col = node2.col
            node2.col = buf
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as RBTnode<*, *>

        if (key != other.key) return false
        if (value != other.value) return false
        if (parent != other.parent) return false
        if (col != other.col) return false
        if (left != other.left) return false
        if (right != other.right) return false

        return true
    }

}
