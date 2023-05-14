package me.xx2bab.buildinaction.slackorchestra

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DAGTest {
    private var dag: DAG<String>? = null
    @Before
    fun setup() {
        dag = DAG()
    }

    @Test
    fun topoSort_Regular() {
        dag!!.addPrerequisite("A", "B")
        dag!!.addPrerequisite("B", null)
        val list = dag!!.topologicalSort()
        Assert.assertEquals(list!![0], "B")
        Assert.assertEquals(list[1], "A")
    }

    @Test
    fun topoSort_Complicated() {
        val list = arrayOf("1", "2", "3", "4", "5", "6")
        for (i in 0 until list.size - 1) {
            dag!!.addPrerequisite(list[i], list[i + 1])
        }
        dag!!.addPrerequisite("6", null)
        val result = dag!!.topologicalSort()
        for (i in list.indices) {
            Assert.assertEquals(list[list.size - 1 - i], result!![i])
        }
    }

    @Test
    fun topoSort_Loop() {
        val list = arrayOf("1", "2", "3", "4", "5", "6")
        for (i in 0 until list.size - 1) {
            dag!!.addPrerequisite(list[i], list[i + 1])
        }
        dag!!.addPrerequisite("6", "1")
        val result = dag!!.topologicalSort()
        Assert.assertNull(result)
    }
}