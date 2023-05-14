package me.xx2bab.buildinaction.slackorchestra

import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class DAG<T> {
    private val inDegree: MutableMap<T, AtomicInteger> = HashMap()
    private val outDegree: MutableMap<T, MutableList<T>> = HashMap()

    /**
     * Build Dependent Relationship.
     *
     * @param obj          An object that is subject to T
     * @param prerequisite The dependency or prerequisite of obj
     */
    fun addPrerequisite(obj: T, prerequisite: T?) {
        if (!inDegree.containsKey(obj) || inDegree[obj] == null) {
            inDegree[obj] = AtomicInteger(0)
        }
        if (prerequisite != null) {
            inDegree[obj]!!.incrementAndGet()
        }
        if (prerequisite == null) {
            return
        }
        if (!outDegree.containsKey(prerequisite) || outDegree[prerequisite] == null) {
            outDegree[prerequisite] = ArrayList()
        }
        outDegree[prerequisite]!!.add(obj)
    }

    fun topologicalSort(): List<T>? {
        val num = inDegree.size
        val zeroDegreeStack: Deque<T> = LinkedList()
        for (key in inDegree.keys) {
            if (inDegree[key]!!.toInt() == 0) {
                zeroDegreeStack.addLast(key)
            }
        }
        val res: MutableList<T> = ArrayList()
        while (!zeroDegreeStack.isEmpty()) {
            val obj = zeroDegreeStack.removeLast()
            res.add(obj)
            if (outDegree[obj] == null) {
                continue
            }
            for (key in outDegree[obj]!!) {
                inDegree[key]!!.decrementAndGet()
                if (inDegree[key]!!.toInt() == 0) {
                    zeroDegreeStack.addLast(key)
                }
            }
        }
        return if (res.size != num) {
            null // Failed case
        } else res
        // Successful case
    }
}