package RedBlackTree

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class RBTnodeTest {
    var node: RBTnode<Int, Int>? = null
    var nodeCheck: RBTnode<Int, Int>? = null
    val RBTree = RBTtree<Int, Int>()

    fun checkStructure(rootFirst: RBTnode<Int, Int>?, rootSecond: RBTnode<Int, Int>?): Boolean {
        if (rootFirst == null)
            return rootFirst == rootSecond

        if (rootFirst.key != rootSecond?.key || rootFirst.col != rootSecond.col)
            return false
        else
            return checkStructure(rootFirst.left, rootSecond.left) && checkStructure(rootFirst.right, rootSecond.right)
    }

    @Test
    fun rotateLeftOne() {
        node = RBTnode(1, 1, null)
        nodeCheck = RBTnode(1, 1, null)
        node?.rotateLeft()
        assertNotNull(node)
        assertTrue(checkStructure(node, nodeCheck))
    }

    @Test
    fun rotateRightOne() {
        node = RBTnode(1, 1, null)
        nodeCheck = RBTnode(1, 1, null)
        node?.rotateRight()
        assertNotNull(node)
        assertTrue(checkStructure(node, nodeCheck))
    }

    @Test
    fun rotateLeftLeaf() {
        node = RBTnode(1, 1, null)
        node!!.col = Colour.black
        node!!.left = RBTnode(-1, 1, node)
        nodeCheck = RBTnode(1, 1, null)
        nodeCheck!!.col = Colour.black
        nodeCheck!!.left = RBTnode(-1, 1, nodeCheck)
        node!!.left!!.rotateLeft()
        assertTrue(checkStructure(node, nodeCheck))
    }

    @Test
    fun rotateRightLeaf() {
        node = RBTnode(1, 1, null)
        node!!.col = Colour.black
        node!!.left = RBTnode(-1, 1, node)
        nodeCheck = RBTnode(1, 1, null)
        nodeCheck!!.col = Colour.black
        nodeCheck!!.left = RBTnode(-1, 1, nodeCheck)
        node!!.left!!.rotateRight()
        assertTrue(checkStructure(node, nodeCheck))
    }

    @Test
    fun RotateLeftNormal() {
        for (i in 1..100)
            RBTree.insert(i, i)

        node = RBTree.root!!.right
        RBTree.root!!.rotateLeft()
        assertEquals(RBTree.root!!.parent, node)
        assertEquals(RBTree.root, node?.left)
    }

    @Test
    fun RotateRightNormal() {
        for (i in 1..100)
            RBTree.insert(i, i)

        node = RBTree.root!!.left
        RBTree.root!!.rotateRight()
        assertEquals(RBTree.root!!.parent, node)
        assertEquals(RBTree.root, node?.right)
    }
}