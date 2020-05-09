package com.sabah.neuro

import kotlin.math.pow
import kotlin.random.Random

class Neuron(
    inputCount: Int,
    private val neuronType: NeuronType = NeuronType.Normal
) {
    var weights: MutableList<Double> = mutableListOf()
    var output: Double = 0.0
    var inputSignals: MutableList<Double> = mutableListOf()
    var delta: Double = 0.0

    init {
        val rand = Random(198237)

        for (x in 0 until inputCount) {
            if (neuronType == NeuronType.Input) {
                weights.add(1.0)
            } else {
                weights.add(rand.nextDouble())
            }
            inputSignals.add(0.0)
        }
    }

    fun feedForward(inputs: List<Double>): Double {
        for (i in 0 until inputs.count()) {
            this.inputSignals[i] = inputs[i]
        }

        var sum = 0.0

        for (i in 0 until inputs.count()) {
            sum += inputs[i] * weights[i]
        }

        output = if (neuronType != NeuronType.Input) {
            sigmoid(sum)
        } else {
            sum
        }

        return output
    }

    fun rebalance(error: Double, rate: Double) {
        if (neuronType == NeuronType.Input) {
            return
        }

        delta = error * sigmoidDx(output)
        for (i in 0 until weights.count()) {
            val weight = weights[i]
            val signal = inputSignals[i]

            val newWeight = weight - (signal * delta * rate)
            weights[i] = newWeight
        }
    }

    private fun sigmoid(x: Double): Double {
        return 1.0 / (1.0 + Math.E.pow(-x))
    }

    private fun sigmoidDx(x: Double): Double {
        val sigmoid = sigmoid(x)
        return sigmoid * (1 - sigmoid)
//        return sigmoid / (1 - sigmoid)
    }
}