package RedBlackTree

import java.util.*


fun main(args: Array<String>) {

    val input = Scanner(System.`in`)
    var switcher: String?
    val tree = RBTtree<Int, String>()

    println("What do you want me to do?\n" +
            "i - insert key and value (integer and string types accordingly)\n" +
            "f - find value by key\n" +
            "d - delete element by key\n" +
            "p - print tree\n" +
            "q - quit")
    menu@
    while (true) {
        switcher = readLine()
        when (switcher) {
            "i" -> {
                println("Enter 'b' to return back to menu")

                while (input.hasNext()) {
                    if (input.hasNextInt()) {
                        var key: Int = input.nextInt()
                        if (input.hasNext()) {
                            var value: String = input.next()
                            tree.insert(key, value)
                        }
                    }
                    else {
                        var buff: String = input.next()
                        if (buff == "b") {
                            println("Back to menu")
                            continue@menu
                        }
                        else {
                            println("Incorrect input\n")
                            println("Back to menu")
                            continue@menu
                        }
                    }
                }
            }
            "f" -> {
                print("\nEnter the key you want to find: ")
                try {
                    var key: Int = input.nextInt()
                    if (tree.find(key) != null) {
                        val b = tree.find(key)!!
                        println(b)
                    }
                    else {
                        println("There is no such element\n")
                    }
                    println("Back to menu")
                    continue@menu
                }
                catch (e: Throwable) {
                    println("Key should be integer")
                    println("Back to menu")
                    continue@menu
                }

            }
            "d" -> {
                print("\nEnter the key you want to delete: ")
                try {
                    var key: Int = input.nextInt()
                    if (tree.find(key) != null) {
                        tree.delete(key)
                    }
                    else {
                        println("There is no such element\n")
                    }
                    println("Back to menu")
                    continue@menu
                }
                catch (e: Throwable) {
                    println("Key should be integer")
                    println("Back to menu")
                    continue@menu
                }

            }
            "p" -> {
                tree.print()
                println("Back to menu")
                continue@menu
            }
            "h" -> println("\ni - insert key and value (integer and string types accordingly)\n" +
                    "f - find value by key\n" +
                    "d - delete element by key\n" +
                    "q - quit")
            "q" -> return
            else -> println("Incorrect input\n")
        }
    }
}