package BTree

import java.util.*


class BTtree<K:Comparable<K>, V>(val t : Int = 2) : Iterable<BTnode<K, V>>{
    var root: BTnode<K, V> = BTnode()

    fun insert(key: K, data: V) {
        if (root.keys.size == 2*t-1) {
            var newRoot = BTnode<K, V>()
            newRoot.offspring.add(root)
            split(newRoot, 0, root)
            root = newRoot
        }

        insert_NotFull(root, key, data)
    }

    private fun insert_NotFull(curNode: BTnode<K, V>, key: K, data: V) {
        var i = 0
        while (i < curNode.keys.size && key > curNode.keys[i])
            ++i
        if(curNode.keys[i] == key)
            curNode.data[i] = data

        if (curNode.isLeaf()) {
            curNode.keys.add(i, key)
            curNode.data.add(i, data)
        } else {
            if (curNode.offspring[i].keys.size == 2*t-1) {
                split(curNode, i, curNode.offspring[i])
                if (key > curNode.keys[i]) {
                    ++i
                }
            }

            insert_NotFull(curNode.offspring[i], key, data)
        }
    }

    private fun split(parent: BTnode<K, V>, i: Int, child: BTnode<K, V>) {
        var buff = BTnode<K, V>()
        for (j in 0..t-2) {
            buff.keys.add(0, child.keys.removeAt(child.keys.size-1))
            buff.data.add(0, child.data.removeAt(child.data.size-1))
        }

        if (!child.isLeaf()) {
            for (j in 0..t-1)
                buff.offspring.add(0, child.offspring.removeAt(child.offspring.size-1))
        }

        parent.offspring.add(i+1, buff)
        parent.keys.add(i, child.keys.removeAt(t-1))
        parent.data.add(i, child.data.removeAt(t-1))
    }


    fun delete (key: K, curNode : BTnode<K, V> = root) {
        //Ищем ключ, больший, либо равный key
        var i = 0
        while (i < curNode.keys.size && key > curNode.keys[i]) {
            ++i
        }
        //Если равен
        if (i < curNode.keys.size && key == curNode.keys[i]) {
            //Если лист, то просто удаляем
            if (curNode.isLeaf()) {
                curNode.keys.removeAt(i)
                curNode.data.removeAt(i)
            } else {
                //Ищем донора
                var donor = curNode

                //Максимальный из левого
                if (donor.offspring[i].keys.size >= t) {
                    donor = donor.offspring[i]
                    while (!donor!!.isLeaf()) {
                        donor = donor.offspring[donor.offspring.size - 1]
                    }

                    var new_key = donor.keys[donor.keys.size - 1]
                    var new_data = donor.data[donor.data.size - 1]

                    delete(new_key, curNode)

                    curNode.keys[i] = new_key
                    curNode.data[i] = new_data

                } else {
                    //Минимальный из правого
                    if (donor.offspring[i + 1].keys.size >= t) {
                        donor = donor.offspring[i + 1]
                        while (!donor!!.isLeaf()) {
                            donor = donor.offspring[0]
                        }
                        var new_key = donor.keys[0]
                        var new_data = donor.data[0]

                        delete(new_key, curNode)

                        curNode.keys[i] = new_key
                        curNode.data[i] = new_data
                    } else {
                        //Если не хватает ключей, то сливаем
                        curNode.offspring[i].keys.add(curNode.keys[i])
                        curNode.offspring[i].data.add(curNode.data[i])

                        while (!curNode.offspring[i + 1].keys.isEmpty()) {
                            curNode.offspring[i].keys.add(curNode.offspring[i + 1].keys.removeAt(0))
                            curNode.offspring[i].data.add(curNode.offspring[i + 1].data.removeAt(0))
                        }

                        while (!curNode.offspring[i + 1].offspring.isEmpty()) {
                            curNode.offspring[i].offspring.add(curNode.offspring[i + 1].offspring.removeAt(0))
                        }

                        curNode.keys.removeAt(i)
                        curNode.data.removeAt(i)
                        curNode.offspring.removeAt(i + 1)
                        delete(key, curNode.offspring[i])
                    }
                }
            }
        //Если ключ не найден
        } else {
            if (curNode.isLeaf())
                return

            //Углубляемся и постоянно чекаем кол-во ключей
            if (curNode.offspring[i].keys.size < t) {

                //Берем ключ справа, если не самый правый
                if (i + 1 < curNode.offspring.size && curNode.offspring[i + 1].keys.size >= t) {
                    var key_parent = curNode.keys[i]
                    var data_parent = curNode.data[i]

                    curNode.keys[i] = curNode.offspring[i + 1].keys.removeAt(0)
                    curNode.data[i] = curNode.offspring[i + 1].data.removeAt(0)

                    curNode.offspring[i].keys.add(key_parent)
                    curNode.offspring[i].data.add(data_parent)

                    if (!curNode.offspring[i].isLeaf())
                        curNode.offspring[i].offspring.add(curNode.offspring[i + 1].offspring.removeAt(0))
                    delete(key, curNode.offspring[i])
                //Слева, если не самый левый
                } else if (i - 1 >= 0 && curNode.offspring[i - 1].keys.size >= t) {
                    var key_parent = curNode.keys[i - 1]
                    var data_parent = curNode.data[i - 1]

                    curNode.keys[i - 1] = curNode.offspring[i - 1].keys.removeAt(curNode.offspring[i - 1].keys.size - 1)
                    curNode.data[i - 1] = curNode.offspring[i - 1].data.removeAt(curNode.offspring[i - 1].data.size - 1)

                    curNode.offspring[i].keys.add(0, key_parent)
                    curNode.offspring[i].data.add(0, data_parent)
                    if (!curNode.offspring[i - 1].isLeaf())
                        curNode.offspring[i].offspring.add(0, curNode.offspring[i - 1].offspring.removeAt(curNode.offspring[i - 1].offspring.size - 1))
                    delete(key, curNode.offspring[i])
                } else {
                    //Если взять не получилось, сливаем с правым
                    if (i + 1 < curNode.offspring.size) {
                        curNode.offspring[i].keys.add(curNode.keys.removeAt(i))
                        curNode.offspring[i].data.add(curNode.data.removeAt(i))

                        while (!curNode.offspring[i + 1].keys.isEmpty()) {
                            curNode.offspring[i].keys.add(curNode.offspring[i + 1].keys.removeAt(0))
                            curNode.offspring[i].data.add(curNode.offspring[i + 1].data.removeAt(0))
                        }

                        if (!curNode.offspring[i + 1].isLeaf())
                            while (!curNode.offspring[i + 1].offspring.isEmpty()) {
                                curNode.offspring[i].offspring.add(curNode.offspring[i + 1].offspring.removeAt(0))
                            }
                        curNode.offspring.removeAt(i + 1)

                        delete(key, curNode.offspring[i])
                    } else {
                        //Или с левым
                        curNode.keys.add(curNode.keys.removeAt(i - 1))
                        curNode.offspring[i].data.add(curNode.data.removeAt(i - 1))

                        while (!curNode.offspring[i - 1].keys.isEmpty()) {
                            curNode.offspring[i].keys.add(0, curNode.offspring[i - 1].keys.removeAt(curNode.offspring[i - 1].keys.size - 1))
                            curNode.offspring[i].data.add(0, curNode.offspring[i - 1].data.removeAt(curNode.offspring[i - 1].data.size - 1))
                        }

                        if (!curNode.offspring[i - 1].isLeaf())
                            while (!curNode.offspring[i - 1].offspring.isEmpty()) {
                                curNode.offspring[i].offspring.add(0, curNode.offspring[i - 1].offspring.removeAt(curNode.offspring[i - 1].offspring.size - 1))
                            }
                        curNode.offspring.removeAt(i - 1)
                        delete(key, curNode)
                    }
                }
            } else
                delete(key, curNode.offspring[i])
        }
        if (root.isEmpty())
            if (!root.offspring.isEmpty())
                root = root.offspring.removeAt(0)
    }


    fun find(key: K, node : BTnode<K, V> = root): V? {
        var i = 0
        while (i < node.keys.size && key > node.keys[i]) {
            i++
        }
        if (i < node.keys.size && key == node.keys[i])
            return node.data[i]

        if (node.isLeaf())
            return null

        return find(key, node.offspring[i])
    }

    override fun iterator(): Iterator<BTnode<K, V>> {
        return (object : Iterator<BTnode<K, V>>
        {
            var nodes : Queue<BTnode<K, V>> = LinkedList()

            init {
                if (!root.isEmpty())
                    nodes.add(root)
            }
            override fun hasNext(): Boolean {
                return nodes.isEmpty()
            }

            override fun next(): BTnode<K, V> {
                var curNode = nodes.poll()
                if (!curNode.isLeaf()) {
                    for (child in curNode.offspring) {
                        nodes.add(child)
                    }
                }
                return curNode
            }

        })
    }

}