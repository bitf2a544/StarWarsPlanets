package com.example.starwarsplanetsassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.starwarsplanetsassignment.data.model.Planet
import com.example.starwarsplanetsassignment.databinding.FragmentPlanetDetailBinding

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

        mBinding.planetNameTV.text = getString(R.string.planet_name, selectedPlanet.name.toString())
        mBinding.orbitalPeriodTV.text =
            getString(R.string.planet_orbital, selectedPlanet.orbitalPeriod.toString())
        mBinding.gravityTV.text =
            getString(R.string.planet_gravity, selectedPlanet.gravity.toString())

        loadImage()

    }

    fun loadImage() {
         var url = BuildConfig.BASE_IMAGE_URL + "id/237/200/200"

        Glide.with(mBinding.root.context)
            .load(url)
             .override(250, 250) // 👈 forces Glide to resize to 200x200 instead of full image
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 👈 caches both original & resized
            .placeholder(R.drawable.baseline_error_24) // use lightweight image here
            .error(R.drawable.ic_error_24)
            .into(mBinding.planetImage)
    }

    companion object {
        const val arg: String = "selected_planet"

        fun newInstance(selectedPlanet: Planet) = PlanetDetailFragment().apply {
            arguments = bundleOf(arg to selectedPlanet)
        }
    }

}