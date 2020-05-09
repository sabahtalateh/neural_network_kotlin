package com.sabah.neuro

class Topology(
    val networkInputs: Int,
    val networkOutputs: Int,
    val hiddenLayers: List<Int> = arrayListOf(),
    val balancingRate: Double
) {
    fun export() {
        exportTopology(this)
    }
}