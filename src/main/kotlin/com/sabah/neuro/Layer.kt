package com.sabah.neuro

class Layer(val neurons: List<Neuron>, val type: NeuronType = NeuronType.Normal) {
    val neuronsCount: Int = neurons.count()

    fun getSignals(): List<Double> {
        return neurons.map { n -> n.output }
    }

    override fun toString(): String {
        return type.toString()
    }
}