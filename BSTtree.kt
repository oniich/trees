package BinarySearchTree

import tree

//Класс бинарного дерева, наследующего интерфейс tree
class BinarySearchTree<K: Comparable<K>, V>: tree<K, V>, Iterable<Pair<K, V>> {
    var root: Node<K, V>? = null

    //Вставка нового узла в дерево
    override fun insert(key: K, value: V) {
        //Условие для вставки корня
        if (this.root == null) {
            root = Node(key, value)
            return
        }
        //Ищет родителя для нового узла
        var parent: Node<K, V>? = null
        //Бегает по дереву и сравнивает
        var cur: Node<K, V>? = root
        while (cur != null) {
            parent = cur

            when {
                key < cur.key -> cur = cur.left

                key > cur.key -> cur = cur.right
                //Если ключи совпадают, то просто обновляется значение
                key == cur.key -> {
                    cur.value = value
                    return
                }
            }
        }

        if (key < parent!!.key)
            parent.left = Node(key, value, parent)
        else
            parent.right = Node(key, value, parent)
    }

    override fun delete(key: K) {
        //Ищем ссылку на удаляемый узел, если не нашли, то выходим
        val delNode: Node<K, V> = findNode(key) ?: return

        val delParent: Node<K, V>? = delNode.parent

        //Если у удаляемого узла нет потомков
        if (delNode.left == null && delNode.right == null) {
            //Когда есть только корень
            if (delNode.parent == null) {
                root = null
                return
            }

            if (delNode == delParent?.left)
                delParent.left = null

            if (delNode == delParent?.right)
                delParent.right = null
        }
        //Хотя бы один потомок
        else if (delNode.left == null || delNode.right == null) {
            //Определяем, какой потомок и ставим его на место удаляемого узла
            if (delNode.left == null) {
                if (delParent?.left == delNode)
                    delParent.left = delNode.right
                else
                    delParent?.right = delNode.right

                delNode.right?.parent = delParent
            } else {
                if (delParent?.left == delNode)
                    delParent.left = delNode.left
                else
                    delParent?.right = delNode.left

                delNode.left?.parent = delParent
            }
        }
        //Есть оба потомка
        else {
            //Ищем минимальный узел в правом поддереве для замены
            val repla: Node<K, V> = min(delNode.right)!!
            //Заменяем ключ и значение
            delNode.key = repla.key
            delNode.value = repla.value

            //Правим ссылки
            if (repla.parent?.left == repla) {
                repla.parent?.left = repla.right

                if (repla.right != null)
                    repla.right!!.parent = repla.parent
            } else {
                repla.parent?.right = repla.right

                if (repla.right != null)
                    repla.right!!.parent = repla.parent
            }
        }
    }

    override fun find(key: K): Pair<K, V>? {
        val result = findNode(key)

        if (result == null)
            return null
        else
            return Pair(result.key, result.value)
    }

    //Метод поиска узла по ключу
    private fun findNode(key: K): Node<K, V>? {
        var cur = root

        while (cur != null) {
            if (key == cur.key)
                return cur

            if (key < cur.key)
                cur = cur.left
            else
                cur = cur.right
        }
        return null
    }

    //Ищет минимальный узел
    private fun min(rootNode: Node<K, V>?): Node<K, V>? {
        if (rootNode?.left == null)
            return rootNode
        else
            return min(rootNode.left)
    }

    //Ищет максимальный узел
    private fun max(rootNode: Node<K, V>?): Node<K, V>? {
        if (rootNode?.right == null)
            return rootNode
        else
            return max(rootNode.right)
    }

    override fun iterator(): Iterator<Pair<K, V>> {
        return (object : Iterator<Pair<K, V>> {
            var cur = min(root)
            var next = min(root)
            val last = max(root)

            override fun hasNext(): Boolean = next != null && next!!.key <= last!!.key

            override fun next(): Pair<K, V> {
                cur = next
                next = nextKey(next)
                return Pair(cur!!.key, cur!!.value)
            }
        })
    }

    //Ищет узел, минимально больший текущего
    private fun nextKey(node: Node<K, V>?): Node<K, V>? {
        var next = node ?: return null

        if (next.right != null)
            return min(next.right!!)

        while (next == next.parent?.right)
            next = next.parent!!

        return next.parent
    }
}