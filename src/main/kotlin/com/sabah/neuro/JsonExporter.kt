package com.sabah.neuro

import com.google.gson.Gson
import java.io.File

const val EXPORT_PATH = "/Users/sabahtalateh/Code/snav/networks/1"

fun exportTopology(topology: Topology) {
    val export = mutableListOf<Int>()
    export.add(topology.networkInputs)
    export.addAll(topology.hiddenLayers)
    export.add(topology.networkOutputs)
    val json = Gson().toJson(export)

    File("$EXPORT_PATH/topology.json").writeText(json.toString())
}

fun exportNetwork(network: NeuralNetwork) {
    val layers: MutableList<MutableList<MutableList<Double>>> = mutableListOf()

    network.layers.forEach { it ->
        val layer: MutableList<MutableList<Double>> = mutableListOf()
        it.neurons.forEach {
            val weights: MutableList<Double> = mutableListOf()
            it.weights.forEach {
                weights.add(it)
            }
            layer.add(weights)
        }
        layers.add(layer)
    }

    val json = Gson().toJson(layers)
    File("${EXPORT_PATH}/epochs").mkdirs()
    var i = 0
    while (true) {
        val file = File("${EXPORT_PATH}/epochs/${i}.json")
        if (!file.exists()) {
            file.writeText(json.toString())
            break
        }
        i++
    }
}
