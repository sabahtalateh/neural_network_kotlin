package com.sabah.neuro

public class NeuralNetworkTest {
    /**
     * @see takoe/network.jpg
     */
    @org.junit.jupiter.api.Test
    fun feed() {
        // 1 - болеет
        // 0 - здоров
        //
        // "Опасная" температура - T
        // "Хороший" возраст - A
        // Курит - S
        // "Правильно" питается - F
        //
        val expected = listOf(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0)
        val dataset: List<List<Double>> = listOf(
            //      T    A    S    F
            listOf(0.0, 0.0, 0.0, 0.0),
            listOf(0.0, 0.0, 0.0, 1.0),
            listOf(0.0, 0.0, 1.0, 0.0),
            listOf(0.0, 0.0, 1.0, 1.0),
            listOf(0.0, 1.0, 0.0, 0.0),
            listOf(0.0, 1.0, 0.0, 1.0),
            listOf(0.0, 1.0, 1.0, 0.0),
            listOf(0.0, 1.0, 1.0, 1.0),
            listOf(1.0, 0.0, 0.0, 0.0),
            listOf(1.0, 0.0, 0.0, 1.0),
            listOf(1.0, 0.0, 1.0, 0.0),
            listOf(1.0, 0.0, 1.0, 1.0),
            listOf(1.0, 1.0, 0.0, 0.0),
            listOf(1.0, 1.0, 0.0, 1.0),
            listOf(1.0, 1.0, 1.0, 0.0),
            listOf(1.0, 1.0, 1.0, 1.0)
        )

        val topology = Topology(4, 1, arrayListOf(3), 0.1)
        val network = NeuralNetwork(topology)
        val diff = network.learn(expected, dataset, 10000)

        println("diff = $diff")

        for (i in 0 until expected.count()) {
            val result = network.feed(dataset[i]).output
            println("${expected[i]} / $result")
        }
    }

    /**
     * @see takoe/network.jpg
     */
    @org.junit.jupiter.api.Test
    fun gg() {
        PictureConverter().convert("a")
    }
}