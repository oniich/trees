//Интерфейс деревьев
interface tree<K: Comparable<K>, V> {
    //Вставка ключа и значения
    fun insert(key: K, value: V)
    //Удаление по ключу
    fun delete(key: K)
    //Поиск по ключу. Возвращает (ключ, значение), либо null, если такового нет
    fun find(key: K): Pair<K, V>?
}
