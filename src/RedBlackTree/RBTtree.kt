package RedBlackTree

import tree

class RBTtree<K: Comparable<K>, V>: tree<K, V>, Iterable<Pair<K, V>> {
    var root: RBTnode<K, V>? = null

    override fun insert(key: K, value: V) {
        var father: RBTnode<K, V>? = null
        var cur: RBTnode<K, V>? = root

        while (cur != null) {
            father = cur

            when {
                key < cur.key -> cur = cur.left

                key > cur.key -> cur = cur.right

                key == cur.key -> {
                    cur.value = value
                    return
                }
            }
        }

        if (father == null) {
            root = RBTnode(key, value, father, Colour.black)
            return
        }

        if (key < father.key) {
            father.left = RBTnode(key, value, father, Colour.red)
            FixAfterInsert(father.left)
        }
        else {
            father.right = RBTnode(key, value, father, Colour.red)
            FixAfterInsert(father.right)
        }
    }

    private fun FixAfterInsert(node: RBTnode<K, V>?) {
        var uncle: RBTnode<K, V>?
        var cur: RBTnode<K, V>? = node

        while(cur?.parent?.col == Colour.red) {
            if (cur.parent == cur.parent?.parent?.left) {
                uncle = cur.parent?.parent?.right

                when {
                    uncle?.col == Colour.red -> {
                        cur.parent?.col = Colour.black
                        uncle.col = Colour.black
                        cur.parent?.parent?.col = Colour.red
                        cur = cur.parent?.parent
                    }

                    cur == cur.parent?.right -> {
                        cur = cur.parent
                        if (cur!!.parent?.parent == null) root = cur.parent
                        cur.rotateLeft()
                    }

                    cur == cur.parent?.left -> {
                        if (cur.parent?.parent?.parent == null) root = cur.parent
                        cur.parent?.parent?.rotateRight()
                    }
                }
            }

            else {
                uncle = cur.parent?.parent?.left

                when {
                    uncle?.col == Colour.red -> {
                        cur.parent?.col = Colour.black
                        uncle.col = Colour.black
                        cur.parent?.parent?.col = Colour.red
                        cur = cur.parent?.parent
                    }

                    cur == cur.parent?.left -> {
                        cur = cur.parent
                        if (cur!!.parent?.parent == null) root = cur.parent
                        cur.rotateRight()
                    }

                    cur == cur.parent?.right -> {
                        if (cur.parent?.parent?.parent == null) root = cur.parent
                        cur.parent?.parent?.rotateLeft()
                    }
                }
            }
        }
        root?.col = Colour.black
    }

    override fun delete(key: K) {
        val node = findNode(key) ?: return

        deleteNode(node)
    }

    private fun deleteNode(node: RBTnode<K, V>) {

        val prev = max(node.left)

        when {
            (node.right != null && node.left != null) -> {
                node.key = prev!!.key
                node.value = prev.value
                deleteNode(prev)
                return
            }

            (node == root && node.isLeaf()) -> {
                root = null
                return
            }

            (node.col == Colour.red && node.isLeaf()) -> {
                if (node == node.parent!!.left)
                    node.parent!!.left = null
                else
                    node.parent!!.right = null

                return
            }

            (node.col == Colour.black && node.left != null && node.left!!.col == Colour.red) -> {
                node.key = node.left!!.key
                node.value = node.left!!.value
                node.left = null
                return
            }

            (node.col == Colour.black && node.right != null && node.right!!.col == Colour.red) -> {
                node.key = node.right!!.key
                node.value = node.right!!.value
                node.right = null
                return
            }

            else -> {
                delete1(node)
            }
        }

        if (node == node.parent!!.left)
            node.parent!!.left = null
        else
            node.parent!!.right = null
    }

    private fun delete1(node: RBTnode<K, V>) {
        if (node.parent != null)
            delete2(node)
    }

    private fun delete2(node: RBTnode<K, V>) {
        val brother = node.brother()

        if (brother!!.col == Colour.red) {
            if (node == node.parent!!.left)
                node.parent!!.rotateLeft()
            else if (node == node.parent!!.right)
                node.parent!!.rotateRight()

            if (root == node.parent)
                root = node.parent!!.parent
        }
        delete3(node)
    }

    private fun delete3(node: RBTnode<K, V>) {
        val brother = node.brother()

        val x: Boolean = brother!!.left == null || brother.left!!.col == Colour.black
        val y: Boolean = brother.right == null || brother.right!!.col == Colour.black

        if (x && y && brother.col == Colour.black && node.parent!!.col == Colour.black) {
            brother.col = Colour.red
            delete1(node.parent!!)
        }
        else
            delete4(node)
    }

    private fun delete4(node: RBTnode<K, V>) {
        val brother = node.brother()

        val x: Boolean = brother!!.left == null || brother.left!!.col == Colour.black
        val y: Boolean = brother.right == null || brother.right!!.col == Colour.black

        if (x && y && brother.col == Colour.black && node.parent!!.col == Colour.red) {
            brother.col = Colour.red
            node.parent!!.col = Colour.black
        }
        else
            delete5(node)
    }

    private fun delete5(node: RBTnode<K, V>) {
        val brother = node.brother()

        val x: Boolean = brother!!.left == null || brother.left!!.col == Colour.black
        val y: Boolean = brother.right == null || brother.right!!.col == Colour.black

        if (brother.col == Colour.black) {
            if (brother.left?.col == Colour.red && y && node == node.parent?.left)
                brother.rotateRight()

            else if (brother.right?.col == Colour.red && x && node == node.parent?.right)
                brother.rotateLeft()
        }
        delete6(node)
    }

    private fun delete6(node: RBTnode<K, V>) {
        val brother = node.brother()

        if (node == node.parent!!.left) {
            brother?.right?.col = Colour.black
            node.parent?.rotateLeft()
        }
        else {
            brother?.left?.col = Colour.black
            node.parent?.rotateRight()
        }

        if (root == node.parent)
            root = node.parent!!.parent
    }

    override fun find(key: K): V? {
        val result = findNode(key)

        if (result == null)
            return null
        else
            return result.value
    }

    private fun findNode(key: K): RBTnode<K, V>? {
        var cur = root

        while (cur != null ) {
            if (key == cur.key)
                return cur

            if (key < cur.key)
                cur = cur.left
            else
                cur = cur.right
        }
        return null
    }

    fun print(height: Int = 0, node: RBTnode<K, V>? = root) {
        if (node == null)
            return

        print(height + 1, node.right)

        for (i in 1..height)
            print(" |")

        if (node.col == Colour.red)
            println(27.toChar() + "[31m${node.key} (" + node.value + ")" + 27.toChar() + "[0m")
        else
            println(27.toChar() + "[30m${node.key} (" + node.value + ")" + 27.toChar() + "[0m")

        print(height + 1, node.left)
    }

    override fun iterator(): Iterator<Pair<K, V>> {
        return (object: Iterator<Pair<K, V>> {
            var node = max(root)
            var next = max(root)
            val last = min(root)

            override fun hasNext(): Boolean {
                return node != null && node!!.key >= last!!.key
            }

            override fun next(): Pair<K, V> {
                next = node
                node = nextSmaller(node)
                return Pair(next!!.key, next!!.value)
            }
        })
    }

    private fun nextSmaller(node: RBTnode<K, V>?): RBTnode<K, V>? {
        var less = node ?: return null

        if (less.left != null)
            return max(less.left!!)

        else if (less == less.parent?.left) {
            while (less == less.parent?.left)
                less = less.parent!!
        }
        return less.parent
    }

    private fun min(node: RBTnode<K, V>?): RBTnode<K, V>? {
        if (node?.left == null)
            return node
        else
            return min(node.left)
    }

    private fun max(node: RBTnode<K, V>?): RBTnode<K, V>? {
        if (node?.right == null)
            return node
        else
            return max(node.right)
    }
}
