package com.sabah.neuro

import kotlin.math.pow
import kotlin.math.sqrt

class NeuralNetwork(private val topology: Topology) {
    val layers: MutableList<Layer> = mutableListOf()

    init {
        createInputLayer()
        createHiddenLayers()
        createOutputLayer()
    }

    fun feed(signals: List<Double>): Neuron {
        feedInput(signals)
        for (i in 1 until layers.count()) {
            val layer = layers[i]
            val prevLayerSignals = layers[i - 1].getSignals()

            layer.neurons.forEach { n -> n.feedForward(prevLayerSignals) }
        }

        return if (layers.last().neurons.count() == 1) {
            layers.last().neurons[0]
        } else {
            layers.last().neurons.maxBy { n -> n.output }!!
        }
    }

    fun learn(expected: List<Double>, dataset: List<List<Double>>, epochs: Int): Double {
//        exportNetwork(this)
        var error = 0.0
        for (i in 0 until epochs) {
            for (i in 0 until expected.count()) {
                error += backPropagate(expected[i], dataset[i])
            }
//            exportNetwork(this)
        }
        return error / epochs
    }

    fun scale(inputs: List<List<Double>>): MutableList<MutableList<Double>> {
        val result: MutableList<MutableList<Double>> = mutableListOf()
        inputs.forEach { _ -> result.add(mutableListOf()) }

        val rows = inputs.count()
        val cols = inputs[0].count()

        for (col in 0 until cols) {
            var min = inputs[0][col]
            var max = inputs[0][col]

            for (row in 1 until rows) {
                val input = inputs[row][col]
                if (input < min) min = input
                if (input > max) max = input
            }

            val divider = max - min
            for (row in 1 until rows) {
                result[row][col] = (inputs[row][col] - min) / divider
            }
        }

        return result
    }

    // https://youtu.be/3esYbQ9PHrM?t=3730
    fun normalize(inputs: List<List<Double>>): MutableList<MutableList<Double>> {
        val result: MutableList<MutableList<Double>> = mutableListOf()

        val rows = inputs.count()
        val cols = inputs[0].count()

        for (col in 0 until cols) {
            var sum = 0.0
            for (row in 0 until rows) {
                sum += inputs[row][col]
            }
            var avg = sum / rows

            var delta = 0.0
            for (row in 0 until rows) {
                delta += (inputs[row][col] - avg).pow(2.0)
            }
            val standardDeviation = sqrt(delta / rows)

            for (row in 0 until rows) {
                result[row][col] = (inputs[row][col] - avg) / standardDeviation
            }
        }

        return result
    }

    // @see takoe/learn
    private fun backPropagate(expected: Double, inputs: List<Double>): Double {
        val actual = feed(inputs).output
        val outputLayerError = actual - expected

        // Balance output layer
        for (neuron in layers.last().neurons) {
            neuron.rebalance(outputLayerError, topology.balancingRate)
        }

        // Balance internal layers
        for (i in layers.count() - 2 downTo 0) {
            // layer - Currently balancing layer
            val currentlyLearningLayer = layers[i]
            // prevLayer - Previous layer (Already balanced)
            val prevLayer = layers[i + 1]

            // For each neuron on "Currently balancing layer"
            for (currentlyLearningNeuronIdx in 0 until currentlyLearningLayer.neuronsCount) {
                // currentlyBalancingNeuron - Currently balancing neuron on "Currently balancing layer"
                val currentlyBalancingNeuron = currentlyLearningLayer.neurons[currentlyLearningNeuronIdx]
                for (prevLayerNeuronIdx in 0 until prevLayer.neuronsCount) {
                    // prevLayerNeuron - Neuron from previous layer
                    val prevLayerNeuron = prevLayer.neurons[prevLayerNeuronIdx]
                    // Weight of the connection between every previous layer neuron and currently learning neuron
                    val prevLayerNeuronToCurrentlyLearningLayerNeuronConnectionWeight =
                        prevLayerNeuron.weights[currentlyLearningNeuronIdx]
                    // Calculate error for currently balancing neuron
                    val error = prevLayerNeuronToCurrentlyLearningLayerNeuronConnectionWeight * prevLayerNeuron.delta
                    // Rebalance currently balancing neuron
                    currentlyBalancingNeuron.rebalance(error, topology.balancingRate)
                }
            }
        }

        return outputLayerError * outputLayerError
    }

    private fun feedInput(signals: List<Double>) {
        for (i in 0 until signals.count()) {
            layers[0].neurons[i].feedForward(listOf(signals[i]))
        }
    }

    private fun createInputLayer() {
        val inputNeurons: MutableList<Neuron> = mutableListOf()
        for (i in 0 until topology.networkInputs) {
            inputNeurons.add(Neuron(1, NeuronType.Input))
        }
        layers.add(Layer(inputNeurons, NeuronType.Input))
    }

    private fun createHiddenLayers() {
        for (hiddenLayerNeuronsCount in topology.hiddenLayers) {
            val hiddenNeurons: MutableList<Neuron> = mutableListOf()
            for (i in 0 until hiddenLayerNeuronsCount) {
                val lastLayer = layers.last()
                hiddenNeurons.add(Neuron(lastLayer.neuronsCount))
            }
            layers.add(Layer(hiddenNeurons, NeuronType.Normal))
        }
    }

    private fun createOutputLayer() {
        val outputNeurons: MutableList<Neuron> = mutableListOf()
        val lastLayer = layers.last()
        for (i in 0 until topology.networkOutputs) {
            outputNeurons.add(Neuron(lastLayer.neuronsCount, NeuronType.Output))
        }
        layers.add(Layer(outputNeurons, NeuronType.Output))
    }
}