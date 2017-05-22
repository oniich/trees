package RedBlackTree

import tree

class RBTprint<K: Comparable<K>, V> {
    fun printTree(tree: tree<K, V>) {
        printNode(node = (tree as RBTtree<K, V>).root)
    }

    fun printNode(height: Int = 0, node: RBTnode<K, V>?) {
        if (node == null)
            return

        printNode(height + 1, node.right)

        for (i in 1..height)
            print(" |")

        if (node.col == Colour.red)
            println(27.toChar() + "[31m${node.key}" + node.value + 27.toChar() + "[0m")
        else
            println(27.toChar() + "[30m${node.key}" + node.value + 27.toChar() + "[0m")

        printNode(height + 1, node.left)
    }
}
