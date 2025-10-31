package com.example.starwarsplanetsassignment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.starwarsplanetsassignment.data.model.Planet
import com.example.starwarsplanetsassignment.databinding.FragmentPlanetDetailBinding
import com.google.gson.Gson

class PlanetDetailFragment : Fragment() {

    private val mBinding: FragmentPlanetDetailBinding by lazy {
        FragmentPlanetDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedPlanet: Planet = arguments?.getParcelable(arg) ?: Planet()
        Log.i("selectedPlanet", "Gson=" + Gson().toJson(selectedPlanet))

        mBinding.planetNameTV.text = getString(R.string.planet_name, selectedPlanet.name.toString())
        mBinding.orbitalPeriodTV.text =
            getString(R.string.planet_name, selectedPlanet.orbitalPeriod.toString())
        mBinding.gravityTV.text = getString(R.string.planet_name, selectedPlanet.gravity.toString())


        Glide.with(mBinding.root.context)
            .load("https://picsum.photos/200")
            .placeholder(R.drawable.baseline_error_24) // optional
            .error(R.drawable.ic_error_24)             // shows if load fails
            .into(mBinding.planetImage)

    }

    companion object {
        const val arg: String = "selected_planet"

        fun newInstance(selectedPlanet: Planet) = PlanetDetailFragment().apply {
            arguments = bundleOf(arg to selectedPlanet)
        }
    }

}