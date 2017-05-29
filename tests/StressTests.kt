import BinarySearchTree.*
import RedBlackTree.*
import BTree.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.*

internal class StressTests {
    var btree = BTtree<Int, Char>(100)
    var tree = BSTtree<Int, Char>()
    var rbtree = RBTtree<Int, Char>()

    companion object{
        var testingSet = mutableSetOf<Int>()

        @BeforeAll @JvmStatic
        fun onStart() {
            while (testingSet.size < 1000000){
                testingSet.add(Random().nextInt())
            }
        }
    }

    @Test
    fun addBinarySearchTree() {
        for (i in testingSet)
        tree.insert(i, 'a')
    }

    @Test
    fun addRedBlackTree() {
        for (i in testingSet)
        rbtree.insert(i, 'a')
    }

    @Test
    fun findBinarySearchTree() {
        for (i in testingSet)
        tree.find(i)
    }

    @Test
    fun findRedBlackTree() {
        for (i in testingSet)
        rbtree.find(i)
    }

    @Test
    fun addBTree() {
        for (i in testingSet)
            btree.insert(i, 'a')
    }

    @Test
    fun findBTree() {
        for (i in testingSet)
            btree.find(i)
    }
}